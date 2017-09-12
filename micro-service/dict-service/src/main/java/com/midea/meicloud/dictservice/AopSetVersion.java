package com.midea.meicloud.dictservice;

import com.midea.meicloud.entitybase.BaseEntity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 15:28 2017-8-28
 */

@Aspect
@Component
public class AopSetVersion {

    @Pointcut("execution(* com.midea.meicloud..*(..)) && " +
            "(@annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            ") ")
    public void controllerMethodPointcutPutDel(){}

    @Around("controllerMethodPointcutPutDel()")
    public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();//参数
        for (Object obj : args){
            if(obj instanceof BaseEntity) {
                BaseEntity entt = (BaseEntity) obj;
                entt.doVerify();
            }
        }
        return pjp.proceed();
    }

    @Pointcut("execution(* com.midea.meicloud..*(..)) && " +
            "(@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PatchMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
            ") ")
    public void controllerMethodPointcutForReturn(){}

    @Around("controllerMethodPointcutForReturn()")
    public Object Interceptor2(ProceedingJoinPoint pjp) throws Throwable {
        Object obj = pjp.proceed();
        if (obj instanceof BaseEntity){
            BaseEntity entt = (BaseEntity) obj;
            entt.makeVerify();
        }
        else if (obj instanceof List<?>){
            List<Object> lst = (List<Object>)obj;
            for (Object listItem : lst){
                if (listItem instanceof BaseEntity){
                    BaseEntity entt = (BaseEntity) listItem;
                    entt.makeVerify();
                }
            }
        }
        return obj;
    }

}
