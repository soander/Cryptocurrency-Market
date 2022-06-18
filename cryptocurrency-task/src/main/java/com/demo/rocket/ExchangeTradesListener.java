package com.demo.rocket;

import com.demo.dto.CreateKLineDto;
import com.demo.model.ExchangeTrade;
import com.demo.service.TradeKlineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
@Slf4j
public class ExchangeTradesListener {

    @StreamListener("exchange_trades_in")
    public void handlerExchangeTrades(List<ExchangeTrade> exchangeTrades) {
        log.info("Receive matching data ==>{}", exchangeTrades);
        if (!CollectionUtils.isEmpty(exchangeTrades)) {
            for (ExchangeTrade exchangeTrade : exchangeTrades) {
                if(exchangeTrade == null) {
                    return;
                }
//                mongoTemplate.insert(exchangeTrade) ;
                CreateKLineDto createKLineDto = exchangeTrade2CreateKLineDto(exchangeTrade);
                TradeKlineService.queue.offer(createKLineDto);
            }
        }
    }

    private CreateKLineDto exchangeTrade2CreateKLineDto(ExchangeTrade exchangeTrade) {
        CreateKLineDto createKLineDto = new CreateKLineDto();
        createKLineDto.setPrice(exchangeTrade.getPrice());
        createKLineDto.setSymbol(exchangeTrade.getSymbol());
        createKLineDto.setVolume(exchangeTrade.getAmount());
        return createKLineDto;
    }

}
