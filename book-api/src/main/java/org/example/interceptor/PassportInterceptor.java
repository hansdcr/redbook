package org.example.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.example.controller.BaseInfoProperties;
import org.example.exceptions.GraceException;
import org.example.result.ResponseStatusEnum;
import org.example.utils.IPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class PassportInterceptor extends BaseInfoProperties implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        // 获得用户的ip
        String userIp = IPUtil.getRequestIp(request);

        // 得到是否存在的判断
        boolean keyIsExist = redis.keyIsExist(MOBILE_SMSCODE + ":" + userIp);

        if (keyIsExist) {
            log.info("短信发送频率太大！");
            GraceException.display(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
            return false;
        }

        /**
         * true: 请求放行
         * false: 请求拦截
         */
        return true;
    }


    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
