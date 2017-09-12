package com.midea.meicloud.auth.controller;
/**
* @Auth: 陈佳攀
* @Description: 登录之后进行用户管理，普通用户只能管理自身，超级管理员能管理任何账户
* @Date: Created in 17:18 2017-8-21
*/
import com.midea.meicloud.common.Constants;
import com.midea.meicloud.common.TempUserInfo;
import com.midea.meicloud.auth.entity.MyUser;
import com.midea.meicloud.auth.service.UserManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 16:55 2017-8-21
 */

@RestController
@RequestMapping("/api/authed/")
public class UserControllerAuthed {

    @Autowired
    UserManagerService userManagerService;
/**
 * @Description: 删除用户，仅管理员可使用
 * @Date: 18:00 2017-8-21
 * @Param:
 * @Return:
 * @Throw:
 * */
    @DeleteMapping("/manage/{userid}")
    public ResponseEntity<?> delUser(@PathVariable Long userid, HttpServletRequest request){
        TempUserInfo userInfo = (TempUserInfo)request.getSession().getAttribute(Constants.sessionAttributeUserInfo);
        if(userInfo == null){
            return new ResponseEntity<>("user not login", HttpStatus.UNAUTHORIZED);
        }
        boolean bres = userManagerService.delUser(userid, userInfo.getUserid());
        if (bres){
            return ResponseEntity.ok("");
        }
        else{
            return ResponseEntity.badRequest().body("user not found");
        }
    }

    /**
     * @Description: 修改密码， 仅管理员可以用
     * @Date: 18:01 2017-8-21
     * @Param:
     * @Return:
     * @Throw:
     * */
    @PutMapping("/manage/id={userid}")
    public ResponseEntity<?> updateUserPassword(@PathVariable Long userid, String password, HttpServletRequest request){
        TempUserInfo userInfo = (TempUserInfo)request.getSession().getAttribute(Constants.sessionAttributeUserInfo);
        if(userInfo == null){
            return new ResponseEntity<>("user not login", HttpStatus.UNAUTHORIZED);
        }
        boolean res = userManagerService.updateUserPassword(userid, password, userInfo.getUserid());
        if (res){
            return ResponseEntity.ok("");
        }
        else{
            return ResponseEntity.badRequest().body("update password failed");
        }
    }

    @GetMapping("/manage/userlist")
    public ResponseEntity<?> getUserList(HttpServletRequest request){
        TempUserInfo userInfo = (TempUserInfo)request.getSession().getAttribute(Constants.sessionAttributeUserInfo);
        if(userInfo == null){
            return new ResponseEntity<>("user not login", HttpStatus.UNAUTHORIZED);
        }

        Long callerId = userInfo.getUserid();
        List<MyUser> lst = userManagerService.getUserInfoList(callerId);
        return new ResponseEntity<>(lst, HttpStatus.OK);
    }
}
