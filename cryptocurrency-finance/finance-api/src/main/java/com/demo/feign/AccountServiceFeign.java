package com.demo.feign;

import com.demo.config.feign.OAuth2FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "finance-service", contextId = "accountServiceFeign", configuration = OAuth2FeignConfig.class, path = "/account")
public interface AccountServiceFeign {

    // Lock user account balance
    @PostMapping("/lockUserAmount")
    void lockUserAmount(@RequestParam("userId") Long userId, @RequestParam("coinId") Long coinId,
                        @RequestParam("mum") BigDecimal mum, @RequestParam("type")String type,
                        @RequestParam("orderId") Long orderId, @RequestParam("fee") BigDecimal fee);

    // Transfer user buy amount to account balance
    @PostMapping("/transferBuyAmount")
    void transferBuyAmount(@RequestParam("fromUserId") Long fromUserId, @RequestParam("toUserId")Long toUserId,
                           @RequestParam("coinId")Long coinId, @RequestParam("amount")BigDecimal amount,
                           @RequestParam("businessType")String businessType, @RequestParam("orderId")Long orderId);

    // Transfer user sell amount to account balance
    @PostMapping("/transferSellAmount")
    void transferSellAmount(@RequestParam("fromUserId") Long fromUserId, @RequestParam("toUserId")Long toUserId,
                            @RequestParam("coinId")Long coinId, @RequestParam("amount")BigDecimal amount,
                            @RequestParam("businessType")String businessType, @RequestParam("orderId")Long orderId);
}
