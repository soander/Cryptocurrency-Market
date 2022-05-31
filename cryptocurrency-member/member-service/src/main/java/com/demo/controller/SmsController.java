package com.demo.controller;

import com.demo.domain.Sms;
import com.demo.model.R;
import com.demo.service.SmsService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService ;

    @PostMapping("/sendTo")
    @ApiOperation(value = "Send sms")
    @ApiImplicitParams({
            @ApiImplicitParam(name="sms",value = "sms json")
    })
    public R sendSms(@RequestBody @Validated Sms sms) {
        boolean isOk = smsService.sendSms(sms);
        if(isOk){
            return R.ok();
        }
        return R.fail("Send fail.");
    }
}
