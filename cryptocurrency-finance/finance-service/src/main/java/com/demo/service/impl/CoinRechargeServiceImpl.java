package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.CoinRecharge;
import com.demo.dto.UserDto;
import com.demo.feign.UserServiceFeign;
import com.demo.mapper.CoinRechargeMapper;
import com.demo.service.CoinRechargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CoinRechargeServiceImpl extends ServiceImpl<CoinRechargeMapper, CoinRecharge> implements CoinRechargeService {

    @Autowired
    private UserServiceFeign userServiceFeign;

    /**
    * @Author Yaozheng Wang
    * @Description Query recharge records
    * @Date 2022/6/1 13:13
    **/
    @Override
    public Page<CoinRecharge> findByPage(Page<CoinRecharge> page, Long coinId, Long userId, String userName, String mobile, Byte status, String numMin, String numMax, String startTime, String endTime) {
        LambdaQueryWrapper<CoinRecharge> coinRechargeLambdaQueryWrapper = new LambdaQueryWrapper<>();

        Map<Long, UserDto> basicUsers = null;
        if (userId != null || !StringUtils.isEmpty(userName) || !StringUtils.isEmpty(mobile)) {

            basicUsers = userServiceFeign.getBasicUsers(userId == null ? null : Arrays.asList(userId), userName, mobile);
            if (CollectionUtils.isEmpty(basicUsers)) {
                return page;
            }
            Set<Long> userIds = basicUsers.keySet();
            coinRechargeLambdaQueryWrapper.in(!CollectionUtils.isEmpty(userIds), CoinRecharge::getUserId, userIds);
        }

        coinRechargeLambdaQueryWrapper.eq(coinId != null, CoinRecharge::getCoinId, coinId)
                .eq(status != null, CoinRecharge::getStatus, status)
                .between(
                        !(StringUtils.isEmpty(numMin) || StringUtils.isEmpty(numMax)),
                        CoinRecharge::getAmount,
                        new BigDecimal(StringUtils.isEmpty(numMin) ? "0" : numMin),
                        new BigDecimal(StringUtils.isEmpty(numMax) ? "0" : numMax)
                )
                .between(
                        !(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)),
                        CoinRecharge::getCreated,
                        startTime, endTime + " 23:59:59"
                );
        Page<CoinRecharge> coinRechargePage = page(page, coinRechargeLambdaQueryWrapper);
        List<CoinRecharge> records = coinRechargePage.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            List<Long> userIds = records.stream().map(CoinRecharge::getUserId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(basicUsers)) {
                basicUsers = userServiceFeign.getBasicUsers(userIds, null, null);
            }
            Map<Long, UserDto> finalBasicUsers = basicUsers;
            records.forEach(coinRecharge -> {
                UserDto userDto = finalBasicUsers.get(coinRecharge.getUserId());
                if (userDto != null) {
                    coinRecharge.setUsername(userDto.getUsername());
                    coinRecharge.setRealName(userDto.getRealName());
                }
            });
        }
        return coinRechargePage;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Query user recharge records
    * @Date 2022/6/1 13:13
    **/
    @Override
    public Page<CoinRecharge> findUserCoinRecharge(Page<CoinRecharge> page, Long coinId, Long userId) {
        return page(page, new LambdaQueryWrapper<CoinRecharge>()
                .eq(coinId!=null,CoinRecharge::getCoinId,coinId)
                .eq(CoinRecharge::getUserId ,userId)
        );
    }
}

