package com.demo.event;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.model.MessagePayload;
import com.demo.rocket.Source;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MimeTypeUtils;

import java.util.List;

@Data
@Slf4j
public class KlineEvent implements Runnable,Event {

    private String symbol;

    private String channel;

    private String keyPrefix;

    private static final String KLINE_GROUP = "market.%s.kline.%s";

    private StringRedisTemplate redisTemplate;

    private Source source;

    public KlineEvent() {
    }

    public KlineEvent(String symbol, String channel, String keyPrefix) {
        this.symbol = symbol;
        this.channel = channel;
        this.keyPrefix = keyPrefix;
    }

    // K line event handle method
    @Override
    public void handle() {
        // 1 循环所有的K 线类型
//        for (KlineType klineType : KlineType.values()) { // 计算机性能差 ,我们只能推一种
            // 2 获取特定的K 线类型 keyPrefix:etcgcn:1min klineType.getValue().toLowerCase()
            String key = new StringBuilder(keyPrefix).append(symbol.toLowerCase()).append(":").append("1min").toString();
            List<String> lines = redisTemplate.opsForList().range(key, 0, 1);
            if(!CollectionUtils.isEmpty(lines)) {
                String lineData = lines.get(0);
                MessagePayload messagePayload = new MessagePayload();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("tick", JSON.parseArray(lineData).toString());
                // market.%s.kline.%s klineType.getValue().toLowerCase()
                messagePayload.setChannel(String.format(KLINE_GROUP, symbol, "1min"));
                messagePayload.setBody(jsonObject.toString());
                source.subscribeGroupOutput()
                        .send(MessageBuilder
                                .withPayload(messagePayload)
                                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build()
                        );
            }

        }

//    }

    /**
     * 让线程池调度
     */
    @Override
    public void run() {
        handle();
    }
}