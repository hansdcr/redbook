package org.example.controller;

import com.grace.result.GraceJSONResult;
import com.mysql.jdbc.StringUtils;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.example.utils.IPUtil;
import org.example.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("passport")
@Api(tags = "PassportController 通行证接口")
public class PassportController extends BaseController{
    @Autowired
    private SMSUtils smsUtils;

    @PostMapping("getSMSCode")
    public GraceJSONResult getSMSCode(@RequestParam String mobile, HttpServletRequest request) throws Exception {
        if (StringUtils.isNullOrEmpty(mobile)) {
            return GraceJSONResult.ok();
        }

        // TODO 获取用户IP，限制用户IP在60s只能获取一次验证码
        String userIp = IPUtil.getRequestIp(request);
        redis.setnx(MOBILE_SMSCODE+":"+userIp,userIp);


        // 随机生成6位的验证码
        String code = (int) ((Math.random() * 9 + 1) * 100000) + "";
        smsUtils.sendSMS(mobile, code);
        // 把验证码放入redis中用于后续验证
        redis.set(MOBILE_SMSCODE+":"+mobile,code,60*30);
        log.info("code={}", code);

        //TODO 把验证码放入redis中

        return GraceJSONResult.ok();
    }
}
