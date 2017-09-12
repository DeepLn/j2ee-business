package com.midea.meicloud.demoservice;

import com.midea.meicloud.entitybase.BaseEntity;
import com.midea.meicloud.entitybase.ThreadInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 18:27 2017-8-28
 */
@Aspect
@Component
public class AopSetForUpdateAndCreate {

    @Pointcut("execution(* com.midea.meicloud..*.save*(..))")
    public void controllerMethodPointcut() {
    }

    @Around("controllerMethodPointcut()")
    public Object Interceptor2(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();//参数
        Object target = pjp.getTarget();
        if (target instanceof JpaRepository<?, ?>) {
            if (args.length > 0) {
                if (args[0] instanceof BaseEntity) {
                    BaseEntity ett = (BaseEntity) args[0];
                    setUpBaseEntity(ett);
                } else if (args[0] instanceof List<?>) {
                    List<Object> lst = (List<Object>) args[0];
                    for (Object obj : lst) {
                        if (obj instanceof BaseEntity) {
                            BaseEntity ett = (BaseEntity) obj;
                            setUpBaseEntity(ett);
                        }
                    }
                }
            }
        }
        return pjp.proceed();
    }

    private void setUpBaseEntity(BaseEntity ett) {
        Long userId = ThreadInfo.instance().getUserId();
        if (ett.getCreateDate() == null) {
            ett.setCreateDate(new Date());
            ett.setCreatedBy(userId);
        }
        ett.setUpdateDate(new Date());
        ett.setUpdatedBy(userId);
    }
}
