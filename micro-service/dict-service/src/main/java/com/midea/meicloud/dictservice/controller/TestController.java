package com.midea.meicloud.dictservice.controller;

import com.midea.meicloud.dictservice.service.TestService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 11:37 2017-8-22
 */

@RestController

@RequestMapping("/api/authed/")
public class TestController {
    protected Logger log = Logger.getLogger(TestController.class);

    @Autowired
    TestService testService;

    @GetMapping("/dictlist")
    public ResponseEntity<?> getDicts(HttpServletRequest request){
        return testService.getPublicKey();
    }

    @GetMapping("/test_userinfo")
    public ResponseEntity<?> testGetUserInfo(){
        return testService.getUserInfo();
    }

    @GetMapping("/test_userinfo2")
    public ResponseEntity<?> testGetUserInfo2(){
        return testService.getUserInfo2();
    }

    @GetMapping("/test_redis")
    public ResponseEntity<?> testRedisGet(){
        return testService.testRedisGet();
    }

    @PutMapping("/test_redis")
    public ResponseEntity<?> testRedisPut(){
        return testService.testRedisPut();
    }

    @PatchMapping("/test_redis")
    public ResponseEntity<?> testRedisUpdate(){
        return testService.testRedisUpdate();
    }
}
