package com.midea.meicloud.dictservice;

import com.midea.meicloud.common.Constants;
import com.midea.meicloud.common.TempUserInfo;
import com.midea.meicloud.entitybase.ThreadInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 16:36 2017-8-23
 */
@Aspect
@Component
public class AopSetCurThreadUser {
    private static final Logger logger = LoggerFactory.getLogger(AopSetCurThreadUser.class);


    @Pointcut("execution(* com.midea.meicloud..*(..)) && " +
            "(@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PatchMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            ") ")
    public void controllerMethodPointcut() {
    }

    @Around("controllerMethodPointcut()")
    public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        TempUserInfo tempUserInfo = (TempUserInfo)session.getAttribute(Constants.sessionAttributeUserInfo);
        if (tempUserInfo != null){
            //表示登录用户的直接访问
            ThreadInfo.instance().setUserId(tempUserInfo.getUserid());
        }
        else {
            String strUserId = request.getHeader(Constants.userId);
            if(strUserId != null){
               Long userId = Long.valueOf(strUserId);
               //表示通过负载均衡的服务器访问
               ThreadInfo.instance().setUserId(userId);
            }
            else{
                //表示公开访问
                ThreadInfo.instance().setUserId(new Long(-1));
            }
        }
        Object obj = pjp.proceed();
        ThreadInfo.instance().setUserId(new Long(0));
        return obj;
    }
}
