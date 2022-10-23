package org.example.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("userinfo")
@Api(tags = "UserInfoController 用户信息接口")
public class UserInfoController {}
