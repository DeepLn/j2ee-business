package com.midea.meicloud.auth.controller;

/**
 * @auth: 陈佳攀
 * @Description:本类中的接口用于本地服务器调用，调用接口的ID，需要通过HTTPSESSION，在REDIS中获取
 * @Date: Created in 14:53 2017-8-21
 */

import com.midea.meicloud.auth.entity.MyUser;
import com.midea.meicloud.auth.service.UserManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/local/")
public class UserControllerLocal {

    @Autowired
    UserManagerService userManagerService;
    @GetMapping("/info/{userid}")
    public MyUser getUserInfo(@PathVariable Long userid) {
        return userManagerService.getUserInfo(userid);
    }
}
