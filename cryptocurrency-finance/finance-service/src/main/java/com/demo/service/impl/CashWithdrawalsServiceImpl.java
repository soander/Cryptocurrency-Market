package com.demo.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.Account;
import com.demo.domain.CashWithdrawAuditRecord;
import com.demo.domain.CashWithdrawals;
import com.demo.domain.Config;
import com.demo.dto.UserBankDto;
import com.demo.dto.UserDto;
import com.demo.feign.UserBankServiceFeign;
import com.demo.feign.UserServiceFeign;
import com.demo.mapper.CashWithdrawAuditRecordMapper;
import com.demo.mapper.CashWithdrawalsMapper;
import com.demo.model.CashSellParam;
import com.demo.service.AccountService;
import com.demo.service.CashWithdrawalsService;
import com.demo.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CashWithdrawalsServiceImpl extends ServiceImpl<CashWithdrawalsMapper, CashWithdrawals> implements CashWithdrawalsService {

    @Autowired
    private UserServiceFeign userServiceFeign;
    @Autowired
    private ConfigService configService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private UserBankServiceFeign userBankServiceFeign;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CashWithdrawAuditRecordMapper cashWithdrawAuditRecordMapper;

    @CreateCache(name = "CASH_WITHDRAWALS_LOCK:", expire = 100, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.BOTH)
    private Cache<String, String> lock;

    /**
    * @Author Yaozheng Wang
    * @Description Query cash withdrawals records by page
    * @Date 2022/6/2 10:36
    **/
    @Override
    public Page<CashWithdrawals> findByPage(Page<CashWithdrawals> page, Long userId, String userName, String mobile, Byte status, String numMin, String numMax, String startTime, String endTime) {

        Map<Long, UserDto> basicUsers = null;
        LambdaQueryWrapper<CashWithdrawals> cashWithdrawalsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (userId != null || !StringUtils.isEmpty(userName) || !StringUtils.isEmpty(mobile)) {
            basicUsers = userServiceFeign.getBasicUsers(userId == null ? null : Arrays.asList(userId), userName, mobile);
            if (CollectionUtils.isEmpty(basicUsers)) {
                return page;
            }
            Set<Long> userIds = basicUsers.keySet();
            cashWithdrawalsLambdaQueryWrapper.in(CashWithdrawals::getUserId, userIds);
        }

        cashWithdrawalsLambdaQueryWrapper.eq(status != null, CashWithdrawals::getStatus, status)
                .between(
                        !(StringUtils.isEmpty(numMin) || StringUtils.isEmpty(numMax)),
                        CashWithdrawals::getNum,
                        new BigDecimal(StringUtils.isEmpty(numMin) ? "0" : numMin),
                        new BigDecimal(StringUtils.isEmpty(numMax) ? "0" : numMax)
                )
                .between(
                        !(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)),
                        CashWithdrawals::getCreated,
                        startTime, endTime + " 23:59:59"
                );
        Page<CashWithdrawals> pageDate = page(page, cashWithdrawalsLambdaQueryWrapper);
        List<CashWithdrawals> records = pageDate.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            List<Long> userIds = records.stream().map(CashWithdrawals::getUserId).collect(Collectors.toList());
            if (basicUsers == null) {
                basicUsers = userServiceFeign.getBasicUsers(userIds, null, null);
            }
            Map<Long, UserDto> finalBasicUsers = basicUsers;
            records.forEach(cashWithdrawals -> {
                UserDto userDto = finalBasicUsers.get(cashWithdrawals.getUserId());
                if (userDto != null) {
                    cashWithdrawals.setUsername(userDto.getUsername());
                    cashWithdrawals.setRealName(userDto.getRealName());
                }
            });
        }
        return pageDate;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Query user's cash withdrawals records by page
    * @Date 2022/6/2 11:13
    **/
    @Override
    public Page<CashWithdrawals> findCashWithdrawals(Page<CashWithdrawals> page, Long userId, Byte status) {
        return page(page, new LambdaQueryWrapper<CashWithdrawals>()
                .eq(CashWithdrawals::getUserId, userId)
                .eq(status != null, CashWithdrawals::getStatus, status));
    }

    /**
    * @Author Yaozheng Wang
    * @Description Sell GCN
    * @Date 2022/6/3 11:01
    **/
    @Override
    public boolean sell(Long userId, CashSellParam cashSellParam) {
        //1 Check cash sell params
        checkCashSellParam(cashSellParam);
        Map<Long, UserDto> basicUsers = userServiceFeign.getBasicUsers(Arrays.asList(userId), null, null);
        if (CollectionUtils.isEmpty(basicUsers)) {
            throw new IllegalArgumentException("User id is not exist");
        }
        UserDto userDto = basicUsers.get(userId);
        // 2 Check validate code
        validatePhoneCode(userDto.getMobile(), cashSellParam.getValidateCode());
        // 3 Check user pay password
        checkUserPayPassword(userDto.getPaypassword(), cashSellParam.getPayPassword());
        // 4 Check user's bank card
        UserBankDto userBankInfo = userBankServiceFeign.getUserBankInfo(userId);
        if (userBankInfo == null) {
            throw new IllegalArgumentException("User bank info is not exist");
        }
        String remark = RandomUtil.randomNumbers(6);
        // 5 Get the cash sell amount
        BigDecimal amount = getCashWithdrawalsAmount(cashSellParam.getNum());
        // 6 Calculate the fee
        BigDecimal fee = getCashWithdrawalsFee(amount);
        Account account = accountService.findByUserAndCoin(userId, "GCN");
        // 7 Create cash withdrawals record
        CashWithdrawals cashWithdrawals = new CashWithdrawals();
        cashWithdrawals.setUserId(userId);
        cashWithdrawals.setAccountId(account.getId());
        cashWithdrawals.setCoinId(cashSellParam.getCoinId());
        cashWithdrawals.setStatus((byte) 0);
        cashWithdrawals.setStep((byte) 1);
        cashWithdrawals.setNum(cashSellParam.getNum());
        cashWithdrawals.setMum(amount.subtract(fee));
        cashWithdrawals.setFee(fee);
        cashWithdrawals.setBank(userBankInfo.getBank());
        cashWithdrawals.setBankCard(userBankInfo.getBankCard());
        cashWithdrawals.setBankAddr(userBankInfo.getBankAddr());
        cashWithdrawals.setBankProv(userBankInfo.getBankProv());
        cashWithdrawals.setBankCity(userBankInfo.getBankCity());
        cashWithdrawals.setTruename(userBankInfo.getRealName());
        cashWithdrawals.setRemark(remark);
        boolean save = save(cashWithdrawals);
        if (save) {
            accountService.lockUserAmount(userId, cashWithdrawals.getCoinId(), cashWithdrawals.getMum(), "withdrawals_out", cashWithdrawals.getId(), cashWithdrawals.getFee());
        }
        return save;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Check cash sell param
    * @Date 2022/6/3 11:03
    **/
    private void checkCashSellParam(CashSellParam cashSellParam) {

        Config cashWithdrawalsStatus = configService.getConfigByCode("WITHDRAW_STATUS");
        if (Integer.valueOf(cashWithdrawalsStatus.getValue()) != 1) {
            throw new IllegalArgumentException("Cash withdrawals is not open");
        }
        @NotNull BigDecimal cashSellParamNum = cashSellParam.getNum();
        // Minimum withdrawal amount
        Config cashWithdrawalsConfigMin = configService.getConfigByCode("WITHDRAW_MIN_AMOUNT");
        if (cashSellParamNum.compareTo(new BigDecimal(cashWithdrawalsConfigMin.getValue())) < 0) {
            throw new IllegalArgumentException("Please enter the minimum withdrawal amount");
        }

        // Maximum withdrawal amount
        Config cashWithdrawalsConfigMax = configService.getConfigByCode("WITHDRAW_MAX_AMOUNT");
        if (cashSellParamNum.compareTo(new BigDecimal(cashWithdrawalsConfigMax.getValue())) >= 0) {
            throw new IllegalArgumentException("Please enter the maximum withdrawal amount");
        }
    }

    /**
     * @Author Yaozheng Wang
     * @Description Check user pay password
     * @Date 2022/6/3 11:10
     **/
    private void checkUserPayPassword(String payDBPassword, String payPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matches = bCryptPasswordEncoder.matches(payPassword, payDBPassword);
        if (!matches) {
            throw new IllegalArgumentException("Pay password is not correct");
        }
    }

    /**
     * @Author Yaozheng Wang
     * @Description Check validate phone code
     * @Date 2022/6/3 13:06
     **/
    private void validatePhoneCode(String mobile, String validateCode) {

        String code = redisTemplate.opsForValue().get("SMS:CASH_WITHDRAWS:" + mobile);
        if (!validateCode.equals(code)) {
            throw new IllegalArgumentException("Validate code is not correct");
        }

    }

    /**
    * @Author Yaozheng Wang
    * @Description Get cash withdrawals amount by num
    * @Date 2022/6/3 13:25
    **/
    private BigDecimal getCashWithdrawalsAmount(BigDecimal num) {
        Config rateConfig = configService.getConfigByCode("USDT2CNY");
        return num.multiply(new BigDecimal(rateConfig.getValue())).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * @Author Yaozheng Wang
     * @Description Calculate the fee
     * @Date 2022/6/3 13:25
     **/
    private BigDecimal getCashWithdrawalsFee(BigDecimal amount) {

        // The minimum withdrawal amount
        Config withdrawMinPoundage = configService.getConfigByCode("WITHDRAW_MIN_POUNDAGE");
        BigDecimal withdrawMinPoundageFee = new BigDecimal(withdrawMinPoundage.getValue());

        // The poundage rate
        Config withdrawPoundageRate = configService.getConfigByCode("WITHDRAW_POUNDAGE_RATE");

        // Calculate the fee by the poundage rate
        BigDecimal poundageFee = amount.multiply(new BigDecimal(withdrawPoundageRate.getValue())).setScale(2, RoundingMode.HALF_UP);

        // Return the minimum fee
        return poundageFee.min(withdrawMinPoundageFee).equals(poundageFee) ? withdrawMinPoundageFee : poundageFee;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Cash withdrawals status audit
    * @Date 2022/6/2 10:37
    * @Param
    * @Return * @return null
    **/
    @Override
    public boolean updateWithdrawalsStatus(Long userId, CashWithdrawAuditRecord cashWithdrawAuditRecord) {

        boolean isOk = lock.tryLockAndRun(cashWithdrawAuditRecord.getId() + "", 300, TimeUnit.SECONDS, () -> {
            CashWithdrawals cashWithdrawals = getById(cashWithdrawAuditRecord.getId());
            if (cashWithdrawals == null) {
                throw new IllegalArgumentException("Cash withdrawals does not exist");
            }

            // Create a new cash withdrawal audit record
            CashWithdrawAuditRecord cashWithdrawAuditRecordNew = new CashWithdrawAuditRecord();
            cashWithdrawAuditRecordNew.setAuditUserId(userId);
            cashWithdrawAuditRecordNew.setRemark(cashWithdrawAuditRecord.getRemark());
            cashWithdrawAuditRecordNew.setCreated(new Date());
            cashWithdrawAuditRecordNew.setStatus(cashWithdrawAuditRecord.getStatus());
            Integer step = cashWithdrawals.getStep() + 1;
            cashWithdrawAuditRecordNew.setStep(step.byteValue());
            cashWithdrawAuditRecordNew.setOrderId(cashWithdrawals.getId());

            // Save cash withdrawal audit record
            int count = cashWithdrawAuditRecordMapper.insert(cashWithdrawAuditRecordNew);
            if (count > 0) {
                cashWithdrawals.setStatus(cashWithdrawAuditRecord.getStatus());
                cashWithdrawals.setRemark(cashWithdrawAuditRecord.getRemark());
                cashWithdrawals.setLastTime(new Date());
                cashWithdrawals.setAccountId(userId);
                cashWithdrawals.setStep(step.byteValue());
                boolean updateById = updateById(cashWithdrawals);
                if (updateById) {
                    accountService.decreaseAccountAmount(userId, cashWithdrawals.getUserId(),
                            cashWithdrawals.getCoinId(), cashWithdrawals.getId(),
                            cashWithdrawals.getNum(), cashWithdrawals.getFee(),
                            cashWithdrawals.getRemark(), "withdrawals_out", (byte) 2
                    );
                }
            }
        });
        return isOk;
    }
}

