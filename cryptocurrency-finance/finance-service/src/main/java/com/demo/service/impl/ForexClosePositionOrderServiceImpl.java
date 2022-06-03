package com.demo.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.mapper.ForexClosePositionOrderMapper;
import com.demo.domain.ForexClosePositionOrder;
import com.demo.service.ForexClosePositionOrderService;

@Service
public class ForexClosePositionOrderServiceImpl extends ServiceImpl<ForexClosePositionOrderMapper, ForexClosePositionOrder> implements ForexClosePositionOrderService {

}

