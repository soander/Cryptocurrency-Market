package com.demo.event;

import com.alibaba.fastjson.JSONObject;
import com.demo.dto.MarketDto;
import com.demo.enums.DepthMergeType;
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

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 深度盘口数据事件
 */
@Component
@Slf4j
public class DepthEvent implements Event {

    @Autowired
    private Source source;

    @Autowired
    private MarketServiceFeign marketServiceFeign;

    private static final String DEPTH_GROUP = "market.%s.depth.step%s";

    @Override
    public void handle() {

        List<MarketDto> marketDtoList = marketServiceFeign.tradeMarkets();
        if (CollectionUtils.isEmpty(marketDtoList)) {
            return;
        }

        for (MarketDto marketDto : marketDtoList) {
            @NotNull String symbol = marketDto.getSymbol();
            for (DepthMergeType mergeType : DepthMergeType.values()) {
                String data = marketServiceFeign.depthData(symbol, mergeType.getValue());
                MessagePayload messagePayload = new MessagePayload();
                messagePayload.setChannel(String.format(DEPTH_GROUP, symbol.toLowerCase(), mergeType.getValue()));
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("tick", data);
                messagePayload.setBody(jsonObject.toJSONString());
                source.subscribeGroupOutput()
                        .send(
                                MessageBuilder
                                .withPayload(messagePayload)
                                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build()
                        );
            }
        }
    }
}
