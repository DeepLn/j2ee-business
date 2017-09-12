package com.midea.meicloud.auth.service;

import com.midea.meicloud.auth.entity.MyRole;
import com.midea.meicloud.auth.entity.MyUser;
import com.midea.meicloud.auth.repository.MyRoleRepo;
import com.midea.meicloud.auth.repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 15:10 2017-8-21
 */

@Service
public class UserManagerService {
    @Autowired
    MyUserRepository myUserRepository;

    @Value("${default.defaultRootPassword}")
    String defaultRootPassword;
    @Value("${default.defaultRootUsername}")
    String defaultRootUsername;

    @Autowired
    MyRoleRepo myRoleRepo;

    @PostConstruct
    void initRoot() {
        MyUser user = myUserRepository.findFirstByUsername(MyRole.root);
        if (user == null) {
            user = new MyUser();
            user.setPassword(defaultRootPassword);
            user.setUsername(defaultRootUsername);
            MyRole role = myRoleRepo.findFirstByName(MyRole.root);
            if ( role == null){
                role = new MyRole();
                role.setName(MyRole.root);
                myRoleRepo.save(role);
            }
            List<MyRole> lst = new ArrayList<>();
            lst.add(role);
            user.setMyRoles(lst);
            myUserRepository.saveAndFlush(user);
        }
    }

    public MyUser getUserInfo(Long userid) {
        MyUser user = myUserRepository.findOne(userid);
        if (user != null) {
            //隐藏密码
            user.setPassword(null);
        }
        return user;
    }

    private boolean roleOK(String roleName, Long callerUserid) {
        List<MyRole> lst = myUserRepository.getOne(callerUserid).getMyRoles();
        for (MyRole role : lst) {
            if (role.getName().equals(roleName))
                return true;
        }
        return false;
    }

    @Transactional
    public boolean delUser(Long userid, Long callerUserid) {
        if (userid != callerUserid && roleOK(MyRole.root, callerUserid)) {
            return false;
        }
        MyUser user = myUserRepository.findOne(userid);
        if (user == null) {
            return false;
        } else {
            myUserRepository.delete(userid);
            return true;
        }
    }

    @Transactional
    public boolean updateUserPassword(Long userid, String password, Long callerUserid) {
        if (userid != callerUserid && roleOK(MyRole.root, callerUserid)) {
            return false;
        }
        MyUser user = myUserRepository.findOne(userid);
        if (user == null) {
            return false;
        } else {
            user.setPassword(password);
            return (myUserRepository.saveAndFlush(user) != null);
        }
    }

    public List<MyUser> getUserInfoList(Long callerUserid){
        if (roleOK(MyRole.root, callerUserid)) {
            return myUserRepository.findAll();
        }
        else if(roleOK(MyRole.common, callerUserid)){
            List<MyUser> lst = new ArrayList<>();
            lst.add(myUserRepository.findOne(callerUserid));
            return lst;
        }
        else
            return null;
    }
}
