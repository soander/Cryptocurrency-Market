package com.demo.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.alicloud.sms.ISmsService;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.domain.Config;
import com.demo.domain.Sms;
import com.demo.mapper.SmsMapper;
import com.demo.service.ConfigService;
import com.demo.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SmsServiceImpl extends ServiceImpl<SmsMapper, Sms> implements SmsService {

    @Autowired
    private ISmsService smsService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    // Send sms
    @Override
    public boolean sendSms(Sms sms) {
        log.info("Send sms{}", JSON.toJSONString(sms, true));
        SendSmsRequest request = buildSmsRequest(sms);
        try {
            SendSmsResponse sendSmsResponse = smsService.sendSmsRequest(request);
            log.info("Send result is {}", JSON.toJSONString(sendSmsResponse, true));
            String code = sendSmsResponse.getCode();
            if ("OK".equals(code)) {
                sms.setStatus(1);
                return save(sms);
            } else {
                return false;
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    private SendSmsRequest buildSmsRequest(Sms sms) {
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(sms.getMobile());
        // Required:SMS-SignName-could be found in sms console
        Config signConfig = configService.getConfigByCode("SIGN");
        sendSmsRequest.setSignName(signConfig.getValue());
        // Required:Template-could be found in sms console
        Config configByCode = configService.getConfigByCode(sms.getTemplateCode());
        if (configByCode == null) {
            throw new IllegalArgumentException("The sign doesn't exist.");
        }
        sendSmsRequest.setTemplateCode(configByCode.getValue());
        // Required:The param of sms template.For exmaple, if the template is "Hello,your verification code is ${code}". The param should be like following value
        String code = RandomUtil.randomNumbers(6);
        // key: SMS:VERIFY_OLD_PHONE:15827293117     value: 123456
        redisTemplate.opsForValue().set("SMS:" + sms.getTemplateCode() + ":" + sms.getMobile(), code,5, TimeUnit.MINUTES);
        sendSmsRequest.setTemplateParam("{\"code\":\"" + code + "\"}");
        String desc = configByCode.getDesc();
        String content = signConfig.getValue() + ":" + desc.replaceAll("\\$\\{code\\}", code);
        sms.setContent(content);
        return sendSmsRequest;
    }
}
