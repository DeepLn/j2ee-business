package com.midea.meicloud.auth.repository;

import com.midea.meicloud.auth.entity.MyRole;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 18:53 2017-8-21
 */
public interface MyRoleRepo extends JpaRepository<MyRole, Long> {
    MyRole findFirstByName(String name);
}
