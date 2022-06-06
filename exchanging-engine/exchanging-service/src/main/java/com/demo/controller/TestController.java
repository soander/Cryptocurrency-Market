package com.demo.controller;

import com.demo.model.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Exchange Controller")
public class TestController {

    @GetMapping("/test")
    @ApiOperation(value = "Exchange System test")
    public R<String> test(){
        return R.ok("Exchange System test success");
    }
}
