package com.demo.service.impl;

import com.demo.mapper.TurnoverRecordMapper;
import com.demo.service.TurnoverRecordService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.TurnoverRecord;

@Service
public class TurnoverRecordServiceImpl extends ServiceImpl<TurnoverRecordMapper, TurnoverRecord> implements TurnoverRecordService {

}
