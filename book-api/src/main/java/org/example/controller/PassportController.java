package org.example.controller;

import org.example.bo.RegistLoginBO;
import org.example.result.GraceJSONResult;
import com.mysql.jdbc.StringUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.example.utils.IPUtil;
import org.example.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("passport")
@Api(tags = "PassportController 通行证接口")
public class PassportController extends BaseInfoProperties {
  @Autowired private SMSUtils smsUtils;

  @PostMapping("getSMSCode")
  public GraceJSONResult getSMSCode(@RequestParam String mobile, HttpServletRequest request)
      throws Exception {
    if (StringUtils.isNullOrEmpty(mobile)) {
      return GraceJSONResult.ok();
    }

    // TODO 获取用户IP，限制用户IP在60s只能获取一次验证码
    String userIp = IPUtil.getRequestIp(request);
    redis.setnx60s(MOBILE_SMSCODE + ":" + userIp, userIp);

    // 随机生成6位的验证码
    String code = (int) ((Math.random() * 9 + 1) * 100000) + "";
    smsUtils.sendSMS(mobile, code);
    // 把验证码放入redis中用于后续验证
    redis.set(MOBILE_SMSCODE + ":" + mobile, code, 60 * 30);
    log.info("code={}", code);

    // TODO 把验证码放入redis中

    return GraceJSONResult.ok();
  }

  @PostMapping("login")
  public GraceJSONResult login(
      @Valid @RequestBody RegistLoginBO registLoginBO, HttpServletRequest request) {
    return GraceJSONResult.ok();
  }
}
