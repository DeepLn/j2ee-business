package com.midea.meicloud.auth.repository;

import com.midea.meicloud.auth.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyUserRepository  extends JpaRepository<MyUser, Long> {
    MyUser findFirstByUsername(String username);
}
