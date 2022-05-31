package com.demo.service;

import com.demo.domain.Sms;
import com.baomidou.mybatisplus.extension.service.IService;
public interface SmsService extends IService<Sms>{

    boolean sendSms(Sms sms);
}
