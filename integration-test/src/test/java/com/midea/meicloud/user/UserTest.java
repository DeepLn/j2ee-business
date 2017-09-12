package com.midea.meicloud.user;

import com.midea.meicloud.RestTemplateConfig;
import com.midea.meicloud.UserLoginHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 9:53 2017-8-30
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestTemplateConfig.class)
public class UserTest {

    @Autowired
    RestTemplate restTemplate;

    @Before
    public void setup() throws Exception{
        UserLoginHelper.instance(restTemplate).login("root", "root123");
    }

    @Test
    public void getUserTest() throws Exception{

    }
}
