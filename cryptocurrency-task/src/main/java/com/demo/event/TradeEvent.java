package com.demo.event;

import com.alibaba.fastjson.JSONObject;
import com.demo.dto.MarketDto;
import com.demo.feign.MarketServiceFeign;
import com.demo.model.MessagePayload;
import com.demo.rocket.Source;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MimeTypeUtils;

import java.util.List;

@Component
@Slf4j
public class TradeEvent implements Event {

    @Autowired
    private Source source;

    private static final String TRADE_GROUP = "market.%s.trade.detail";

    @Autowired
    private MarketServiceFeign marketServiceFeign;

    @Override
    public void handle() {
        // 1 获取所有的交易市场
        List<MarketDto> marketDtos = marketServiceFeign.tradeMarkets();
        if (CollectionUtils.isEmpty(marketDtos)) {
            return;
        }

        for (MarketDto marketDto : marketDtos) {
            // 2 查询该交易对下的交易数据
            String data = marketServiceFeign.trades(marketDto.getSymbol());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", data);
            MessagePayload messagePayload = new MessagePayload();
            messagePayload.setChannel(String.format(TRADE_GROUP, marketDto.getSymbol().toLowerCase()));
            messagePayload.setBody(jsonObject.toString());
            source.subscribeGroupOutput()
                    .send(
                            MessageBuilder
                                    .withPayload(messagePayload)
                                    .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                                    .build()
            );
        }

    }
}