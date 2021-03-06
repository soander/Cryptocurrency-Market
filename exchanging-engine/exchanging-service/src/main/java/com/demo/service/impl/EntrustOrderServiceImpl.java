package com.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.service.EntrustOrderService;
import com.demo.service.MarketService;
import com.demo.service.TurnoverOrderService;
import com.demo.config.rocket.Source;
import com.demo.domain.EntrustOrder;
import com.demo.domain.ExchangeTrade;
import com.demo.domain.Market;
import com.demo.domain.TurnoverOrder;
import com.demo.feign.AccountServiceFeign;
import com.demo.mapper.EntrustOrderMapper;
import com.demo.param.OrderParam;
import com.demo.vo.TradeEntrustOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class EntrustOrderServiceImpl extends ServiceImpl<EntrustOrderMapper, EntrustOrder> implements EntrustOrderService {

    @Autowired
    private MarketService marketService;
    @Autowired
    private TurnoverOrderService turnoverOrderService;
    @Autowired
    private AccountServiceFeign accountServiceFeign;
    @Autowired
    private Source source;

    /**
    * @Author Yaozheng Wang
    * @Description Query user's entrust order list
    * @Date 2022/6/5 12:23
    **/
    @Override
    public Page<EntrustOrder> findByPage(Page<EntrustOrder> page, Long userId, String symbol, Integer type) {
        return page(page, new LambdaQueryWrapper<EntrustOrder>()
                .eq(EntrustOrder::getUserId, userId)
                .eq(!StringUtils.isEmpty(symbol), EntrustOrder::getSymbol, symbol)
                .eq(type != null && type != 0, EntrustOrder::getType, type)
                .orderByDesc(EntrustOrder::getCreated)
        );
    }

    /**
    * @Author Yaozheng Wang
    * @Description Query user's entrust order history list
    * @Date 2022/6/6 10:24
    **/
    @Override
    public Page<TradeEntrustOrderVo> getHistoryEntrustOrder(Page<EntrustOrder> page, String symbol, Long userId) {
        Page<EntrustOrder> entrustOrderPage = page(page, new LambdaQueryWrapper<EntrustOrder>()
                .eq(EntrustOrder::getUserId, userId)
                .eq(EntrustOrder::getSymbol, symbol)
        );
        Page<TradeEntrustOrderVo> tradeEntrustOrderVoPage = new Page<>(page.getCurrent(), page.getSize());
        List<EntrustOrder> entrustOrders = entrustOrderPage.getRecords();
        if (CollectionUtils.isEmpty(entrustOrders)) {
            tradeEntrustOrderVoPage.setRecords(Collections.emptyList());
        } else {
            List<TradeEntrustOrderVo> tradeEntrustOrderVos = entrustOrders2tradeEntrustOrderVos(entrustOrders);
            tradeEntrustOrderVoPage.setRecords(tradeEntrustOrderVos);
        }

        return tradeEntrustOrderVoPage;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Query user's unfinished entrust order list
    * @Date 2022/6/6 10:31
    **/
    @Override
    public Page<TradeEntrustOrderVo> getEntrustOrder(Page<EntrustOrder> page, String symbol, Long userId) {
        Page<EntrustOrder> entrustOrderPage = page(page, new LambdaQueryWrapper<EntrustOrder>()
                .eq(EntrustOrder::getUserId, userId)
                .eq(EntrustOrder::getSymbol, symbol)
                .eq(EntrustOrder::getStatus, 0)
        );
        Page<TradeEntrustOrderVo> tradeEntrustOrderVoPage = new Page<>(page.getCurrent(), page.getSize());
        List<EntrustOrder> entrustOrders = entrustOrderPage.getRecords();
        if (CollectionUtils.isEmpty(entrustOrders)) {
            tradeEntrustOrderVoPage.setRecords(Collections.emptyList());
        } else {
            List<TradeEntrustOrderVo> tradeEntrustOrderVos = entrustOrders2tradeEntrustOrderVos(entrustOrders);
            tradeEntrustOrderVoPage.setRecords(tradeEntrustOrderVos);
        }
        return tradeEntrustOrderVoPage;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Entrust order list transfer to trade entrust order list
    * @Date 2022/6/6 10:25
    **/
    private List<TradeEntrustOrderVo> entrustOrders2tradeEntrustOrderVos(List<EntrustOrder> entrustOrders) {
        List<TradeEntrustOrderVo> tradeEntrustOrderVos = new ArrayList<>(entrustOrders.size());
        for (EntrustOrder entrustOrder : entrustOrders) {
            tradeEntrustOrderVos.add(entrustOrder2TradeEntrustOrderVo(entrustOrder));
        }
        return tradeEntrustOrderVos;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Entrust order transfer to trade entrust order
    * @Date 2022/6/6 10:25

    **/
    private TradeEntrustOrderVo entrustOrder2TradeEntrustOrderVo(EntrustOrder entrustOrder) {
        TradeEntrustOrderVo tradeEntrustOrderVo = new TradeEntrustOrderVo();
        tradeEntrustOrderVo.setOrderId(entrustOrder.getId());
        tradeEntrustOrderVo.setCreated(entrustOrder.getCreated());
        tradeEntrustOrderVo.setStatus(entrustOrder.getStatus().intValue());
        tradeEntrustOrderVo.setAmount(entrustOrder.getAmount());
        tradeEntrustOrderVo.setDealVolume(entrustOrder.getDeal());
        tradeEntrustOrderVo.setPrice(entrustOrder.getPrice());
        tradeEntrustOrderVo.setVolume(entrustOrder.getVolume());

        tradeEntrustOrderVo.setType(entrustOrder.getType().intValue());
        // Get the deal amount and volume
        BigDecimal dealAmount = BigDecimal.ZERO;
        BigDecimal dealVolume = BigDecimal.ZERO;
        if (tradeEntrustOrderVo.getType() == 1) {
            List<TurnoverOrder> buyTurnoverOrders = turnoverOrderService.getBuyTurnoverOrder(entrustOrder.getId(), entrustOrder.getUserId());
            if (!CollectionUtils.isEmpty(buyTurnoverOrders)) {
                for (TurnoverOrder buyTurnoverOrder : buyTurnoverOrders) {
                    BigDecimal amount = buyTurnoverOrder.getAmount();
                    dealAmount = dealAmount.add(amount);
                }
            }
        }
        if (tradeEntrustOrderVo.getType() == 2) {
            List<TurnoverOrder> sellTurnoverOrders = turnoverOrderService.getSellTurnoverOrder(entrustOrder.getId(), entrustOrder.getUserId());
            if (!CollectionUtils.isEmpty(sellTurnoverOrders)) {
                for (TurnoverOrder sellTurnoverOrder : sellTurnoverOrders) {
                    BigDecimal amount = sellTurnoverOrder.getAmount();
                    dealAmount = dealAmount.add(amount);
                }
            }
        }

        tradeEntrustOrderVo.setDealAmount(dealAmount); // Set the deal amount
        tradeEntrustOrderVo.setDealVolume(entrustOrder.getDeal()); // Set the deal volume
        BigDecimal dealAvgPrice = BigDecimal.ZERO;
        if (dealAmount.compareTo(BigDecimal.ZERO) > 0) {
            dealAvgPrice = dealAmount.divide(entrustOrder.getDeal(), 8, RoundingMode.HALF_UP);
        }
        tradeEntrustOrderVo.setDealAvgPrice(dealAvgPrice); // Set the deal average price
        return tradeEntrustOrderVo;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Create a entrusted order
    * @Date 2022/6/7 10:15
    **/
    @Transactional
    @Override
    public Boolean createEntrustOrder(Long userId, OrderParam orderParam) {

        // Get market information
        @NotBlank String symbol = orderParam.getSymbol();
        Market markerBySymbol = marketService.getMarkerBySymbol(symbol);
        if (markerBySymbol == null) {
            throw new IllegalArgumentException("Symbol is null");
        }
        BigDecimal price = orderParam.getPrice().setScale(markerBySymbol.getPriceScale(), RoundingMode.HALF_UP);
        BigDecimal volume = orderParam.getVolume().setScale(markerBySymbol.getNumScale(), RoundingMode.HALF_UP);
        BigDecimal mum = price.multiply(volume);

        // Get the volume
        @NotNull BigDecimal numMax = markerBySymbol.getNumMax();
        @NotNull BigDecimal numMin = markerBySymbol.getNumMin();
        if (volume.compareTo(numMax) > 0 || volume.compareTo(numMin) < 0) {
            throw new IllegalArgumentException("The volume is out of range");
        }

        // Get the trade amount
        BigDecimal tradeMin = markerBySymbol.getTradeMin();
        BigDecimal tradeMax = markerBySymbol.getTradeMax();
        if (mum.compareTo(tradeMin) < 0 || mum.compareTo(tradeMax) > 0) {
            throw new IllegalArgumentException("The trade amount is out of range");
        }

        // Calculate the fee
        BigDecimal fee;
        BigDecimal feeRate;
        @NotNull Integer type = orderParam.getType();
        if (type == 1) { // Buy
            feeRate = markerBySymbol.getFeeBuy();
            fee = mum.multiply(markerBySymbol.getFeeBuy());
        } else { // Sell
            feeRate = markerBySymbol.getFeeSell();
            fee = mum.multiply(markerBySymbol.getFeeSell());
        }

        // Create a new entrust order
        EntrustOrder entrustOrder = new EntrustOrder();
        entrustOrder.setUserId(userId);
        entrustOrder.setAmount(mum);
        entrustOrder.setType(orderParam.getType().byteValue());
        entrustOrder.setPrice(price);
        entrustOrder.setVolume(volume);
        entrustOrder.setFee(fee);
        entrustOrder.setCreated(new Date());
        entrustOrder.setStatus((byte) 0);
        entrustOrder.setMarketId(markerBySymbol.getId());
        entrustOrder.setMarketName(markerBySymbol.getName());
        entrustOrder.setMarketType(markerBySymbol.getType());
        entrustOrder.setSymbol(markerBySymbol.getSymbol());
        entrustOrder.setFeeRate(feeRate);
        entrustOrder.setDeal(BigDecimal.ZERO);
        entrustOrder.setFreeze(entrustOrder.getAmount().add(entrustOrder.getFee()));

        boolean save = save(entrustOrder);
        if (save) {
            @NotNull Long coinId;
            if (type == 1) { // Buy
                coinId = markerBySymbol.getBuyCoinId();
            } else { // Sell
                coinId = markerBySymbol.getSellCoinId();
            }
            if (entrustOrder.getType() == (byte) 1) {
                accountServiceFeign.lockUserAmount(userId, coinId, entrustOrder.getFreeze(), "trade_create", entrustOrder.getId(), fee);
            }

            MessageBuilder<EntrustOrder> entrustOrderMessageBuilder = MessageBuilder.withPayload(entrustOrder).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
            source.outputMessage().send(entrustOrderMessageBuilder.build());
        }
        return save;
    }

    /**
    * @Author Yaozheng Wang
    * @Description Update entrusted order about exchange trade
    * @Date 2022/6/14 21:37
    **/
    @Override
    @Transactional
    public void doMatch(ExchangeTrade exchangeTrade) {
        String sellOrderId = exchangeTrade.getSellOrderId();
        String buyOrderId = exchangeTrade.getBuyOrderId();
        EntrustOrder sellOrder = getById(sellOrderId);
        EntrustOrder buyOrder = getById(buyOrderId);
        Long marketId = sellOrder.getMarketId();
        Market market = marketService.getById(marketId);

        // 1 Add the turnover order
        addTurnOverOrderRecord(sellOrder, buyOrder, market, exchangeTrade);
        // 2 Update entrust order
        updateEntrustOrder(sellOrder, buyOrder, exchangeTrade);
        // 3 Roll back the account
        rollBackAccount(sellOrder, buyOrder, exchangeTrade, market);
    }

    /**
    * @Author Yaozheng Wang
    * @Description Add the turnover order
    * @Date 2022/6/14 21:40
    **/
    private void addTurnOverOrderRecord(EntrustOrder sellOrder, EntrustOrder buyOrder, Market market, ExchangeTrade exchangeTrade) {

        // Add the sell turnover order
        TurnoverOrder sellTurnoverOrder = new TurnoverOrder();
        sellTurnoverOrder.setSellOrderId(sellOrder.getId());
        sellTurnoverOrder.setBuyCoinId(buyOrder.getId());
        sellTurnoverOrder.setBuyVolume(exchangeTrade.getAmount());
        sellTurnoverOrder.setAmount(exchangeTrade.getSellTurnover());
        sellTurnoverOrder.setBuyCoinId(market.getBuyCoinId());
        sellTurnoverOrder.setSellCoinId(market.getSellCoinId());
        sellTurnoverOrder.setCreated(new Date());
        sellTurnoverOrder.setBuyUserId(buyOrder.getUserId());
        sellTurnoverOrder.setSellUserId(sellOrder.getUserId());
        sellTurnoverOrder.setPrice(exchangeTrade.getPrice());
        sellTurnoverOrder.setBuyPrice(buyOrder.getPrice());
        sellTurnoverOrder.setTradeType(2);
        turnoverOrderService.save(sellTurnoverOrder);

        // Add the buy turnover order
        TurnoverOrder buyTurnoverOrder = new TurnoverOrder();
        buyTurnoverOrder.setBuyOrderId(buyOrder.getId());
        buyTurnoverOrder.setSellOrderId(sellOrder.getId());
        buyTurnoverOrder.setAmount(exchangeTrade.getBuyTurnover());
        buyTurnoverOrder.setBuyVolume(exchangeTrade.getAmount());
        buyTurnoverOrder.setSellUserId(sellOrder.getUserId());
        buyTurnoverOrder.setBuyUserId(buyOrder.getUserId());
        buyTurnoverOrder.setSellCoinId(market.getSellCoinId());
        buyTurnoverOrder.setBuyCoinId(market.getBuyCoinId());
        buyTurnoverOrder.setCreated(new Date());
        sellTurnoverOrder.setTradeType(1);
        turnoverOrderService.save(sellTurnoverOrder);
    }

    /**
    * @Author Yaozheng Wang
    * @Description Update entrust order
    * @Date 2022/6/14 21:40
    **/
    private void updateEntrustOrder(EntrustOrder sellOrder, EntrustOrder buyOrder, ExchangeTrade exchangeTrade) {

        sellOrder.setDeal(exchangeTrade.getAmount());
        buyOrder.setDeal(exchangeTrade.getAmount());
        BigDecimal volume = sellOrder.getVolume();
        BigDecimal amount = exchangeTrade.getAmount();

        if (amount.compareTo(volume) == 0) {
            sellOrder.setStatus((byte) 1);
        }
        BigDecimal buyOrderVolume = buyOrder.getVolume();
        if (buyOrderVolume.compareTo(volume) == 0) {
            buyOrder.setStatus((byte) 1);
        }
        updateById(sellOrder);
        updateById(buyOrder);
    }

    /**
    * @Author Yaozheng Wang
    * @Description Roll back the account
    * @Date 2022/6/14 21:41
    **/
    private void rollBackAccount(EntrustOrder sellOrder, EntrustOrder buyOrder, ExchangeTrade exchangeTrade, Market market) {
        accountServiceFeign.transferBuyAmount(buyOrder.getUserId(), sellOrder.getUserId(),
                market.getBuyCoinId(), exchangeTrade.getBuyTurnover(), "????????????",
                Long.valueOf(exchangeTrade.getBuyOrderId()));

        accountServiceFeign.transferSellAmount(sellOrder.getUserId(), sellOrder.getUserId(),
                market.getSellCoinId(), exchangeTrade.getSellTurnover(), "????????????",
                Long.valueOf(exchangeTrade.getSellOrderId()));
    }

    /**
    * @Author Yaozheng Wang
    * @Description Cancel entrusted order
    * @Date 2022/6/14 22:30
    **/
    @Override
    public void cancleEntrustOrder(Long orderId) {
        EntrustOrder entrustOrder = new EntrustOrder();
        entrustOrder.setStatus((byte) 2);
        entrustOrder.setId(orderId);
        Message<EntrustOrder> message = MessageBuilder.withPayload(entrustOrder).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build();
        source.outputMessage().send(message);
    }

    /**
    * @Author Yaozheng Wang
    * @Description Cancel entrusted order in database
    * @Date 2022/6/14 22:41
    **/
    @Override
    public void cancleEntrustOrderToDb(String orderId) {
        if (StringUtils.hasText(orderId)) {
            Long orderIdVal = Long.valueOf(orderId);
            EntrustOrder entrustOrder = getById(orderId);
            entrustOrder.setStatus((byte) 2);
            updateById(entrustOrder);
        }
    }
}
