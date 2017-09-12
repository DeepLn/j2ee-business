package com.midea.meicloud.entitybase;

import com.midea.meicloud.common.TempUserInfo;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 17:56 2017-8-23
 */
public class ThreadInfo {

    private static ThreadLocal<ThreadInfo> globalThreadLocal = new ThreadLocal<>();

    //默认0，表示服务器自身逻辑写的数据的用户ID
    Long userId = new Long(0);

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public static ThreadInfo instance(){
        ThreadInfo info = globalThreadLocal.get();
        if(info == null){
            info = new ThreadInfo();
            globalThreadLocal.set(info);
        }
        return info;
    }
}
