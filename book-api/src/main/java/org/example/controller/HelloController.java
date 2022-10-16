package org.example.controller;

import com.grace.result.GraceJSONResult;
import org.example.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("hello")
    public Object hello(){
        return GraceJSONResult.ok();
    }

    @Autowired
    private SMSUtils smsUtils;

    @GetMapping("sms")
    public Object sms() throws Exception {
        String code = "123456";
        smsUtils.sendSMS("18665947850",code);
        return GraceJSONResult.ok();
    }
}
