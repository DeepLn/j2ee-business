package com.midea.meicloud.entitybase;

import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 11:08 2017-8-29
 */
public class MyRestTemplate extends RestTemplate {
    public MyRestTemplate(){
        setInterceptors(Collections.singletonList(new UserInterceptor()));
    }
}
