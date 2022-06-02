package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.CoinWithdraw;
import com.demo.dto.UserDto;
import com.demo.feign.UserServiceFeign;
import com.demo.mapper.CoinWithdrawMapper;
import com.demo.service.CoinWithdrawService;
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
public class CoinWithdrawServiceImpl extends ServiceImpl<CoinWithdrawMapper, CoinWithdraw> implements CoinWithdrawService {

    @Autowired
    private UserServiceFeign userServiceFeign;

    /**
     * Query coin withdraw records by page
     *
     * @param page
     * @param coinId
     * @param userId
     * @param userName
     * @param mobile
     * @param status
     * @param numMin
     * @param numMax
     * @param startTime
     * @param endTime
     */
    @Override
    public Page<CoinWithdraw> findByPage(Page<CoinWithdraw> page, Long coinId, Long userId, String userName, String mobile, Byte status, String numMin, String numMax, String startTime, String endTime) {
        LambdaQueryWrapper<CoinWithdraw> coinWithdrawLambdaQueryWrapper = new LambdaQueryWrapper<>();

        Map<Long, UserDto> basicUsers = null;
        if (userId != null || !StringUtils.isEmpty(userName) | !StringUtils.isEmpty(mobile)) {

            basicUsers = userServiceFeign.getBasicUsers(userId == null ? null : Arrays.asList(userId), userName, mobile);
            if (CollectionUtils.isEmpty(basicUsers)) {
                return page;
            }
            Set<Long> userIds = basicUsers.keySet();
            coinWithdrawLambdaQueryWrapper.in(!CollectionUtils.isEmpty(userIds), CoinWithdraw::getUserId, userIds);
        }

        coinWithdrawLambdaQueryWrapper.eq(coinId != null, CoinWithdraw::getCoinId, coinId)
                .eq(status != null, CoinWithdraw::getStatus, status)
                .between(
                        !(StringUtils.isEmpty(numMin) || StringUtils.isEmpty(numMax)),
                        CoinWithdraw::getNum,
                        new BigDecimal(StringUtils.isEmpty(numMin) ? "0" : numMin),
                        new BigDecimal(StringUtils.isEmpty(numMax) ? "0" : numMax))
                .between(
                        !(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)),
                        CoinWithdraw::getCreated,
                        startTime, endTime + " 23:59:59");

        Page<CoinWithdraw> coinWithdrawPage = page(page, coinWithdrawLambdaQueryWrapper);
        List<CoinWithdraw> records = coinWithdrawPage.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            List<Long> userIds = records.stream().map(CoinWithdraw::getUserId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(basicUsers)) {
                basicUsers = userServiceFeign.getBasicUsers(userIds, null, null);
            }
            Map<Long, UserDto> finalBasicUsers = basicUsers;
            records.forEach(coinWithdraw -> {
                UserDto userDto = finalBasicUsers.get(coinWithdraw.getUserId());
                if (userDto != null) {
                    coinWithdraw.setUsername(userDto.getUsername());
                    coinWithdraw.setRealName(userDto.getRealName());
                }
            });
        }
        return coinWithdrawPage;
    }

    /**
     * Query user coin withdraw records by page
     * @param userId
     * @param coinId
     * @param page
     * @return
     */
    @Override
    public Page<CoinWithdraw> findUserCoinWithdraw(Long userId, Long coinId, Page<CoinWithdraw> page) {
        return page(page,new LambdaQueryWrapper<CoinWithdraw>().eq(CoinWithdraw::getUserId,userId)
                .eq(coinId!=null,CoinWithdraw::getCoinId,coinId)
        );
    }
}

