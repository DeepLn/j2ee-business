package com.midea.meicloud.demo.controller;

import com.midea.meicloud.demo.entity.User;
import com.midea.meicloud.demo.service.RibbonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RibbonController {
  @Autowired
  private RibbonService ribbonService;

  @GetMapping("/id={id}")
  public User findById(@PathVariable Long id) {
    return this.ribbonService.findById(id);
  }
}
