package org.example.controller;

import com.mysql.cj.util.StringUtils;
import org.example.bo.RegistLoginBO;
import org.example.pojo.Users;
import org.example.result.GraceJSONResult;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.example.result.ResponseStatusEnum;
import org.example.service.UserService;
import org.example.utils.IPUtil;
import org.example.utils.SMSUtils;
import org.example.vo.UsersVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("passport")
@Api(tags = "PassportController 通行证接口")
public class PassportController extends BaseInfoProperties {
  @Autowired private SMSUtils smsUtils;
  @Autowired private UserService userService;

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
    String mobile = registLoginBO.getMobile();
    String code = registLoginBO.getSmscode();
    // 1. 从redis中获得验证码进行校验是否匹配
    String redisCode = redis.get(MOBILE_SMSCODE + ":" + mobile);
    if (StringUtils.isNullOrEmpty(redisCode) || !redisCode.equalsIgnoreCase(code)) {
      return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
    }

    // 2 查询数据库，判断用户是否存在
    Users user = userService.queryMobileIsExist(mobile);
    if (user == null) {
      user = userService.createUser(mobile);
    }

    // 3 保存用户信息和会话信息
    String uToken = UUID.randomUUID().toString();
    redis.set(REDIS_USER_TOKEN + ":" + user.getId(), uToken);

    // 4 用户登录注册成功后，删除redis中的短信验证码
    redis.del(MOBILE_SMSCODE + ":" + mobile);

    // 5 返回用户信息，包含token令牌
    UsersVO usersVO = new UsersVO();
    BeanUtils.copyProperties(user, usersVO);
    usersVO.setUserToken(uToken);

    return GraceJSONResult.ok(usersVO);
  }
}
