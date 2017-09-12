package com.midea.meicloud.demo.service;

import com.midea.meicloud.demo.entity.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RibbonService {
  private static final Logger LOGGER = LoggerFactory.getLogger(RibbonService.class);
  @Autowired
  private RestTemplate restTemplate;

  @HystrixCommand(fallbackMethod = "fallback")
  public User findById(Long id) {
    // http://服务提供者的serviceId/url
    return this.restTemplate.getForObject("http://miduser/id=" + id, User.class);
  }

  public User fallback(Long id) {
    RibbonService.LOGGER.info("异常发生，进入fallback方法，接收的参数：id = {}", id);
            User user = new User();
    user.setId(-1L);
    user.setUsername("default username");
    user.setAge(0);
    return user;
  }

}
