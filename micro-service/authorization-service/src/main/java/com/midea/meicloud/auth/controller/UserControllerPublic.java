package com.midea.meicloud.auth.controller;
/**
* @Auth: 陈佳攀
* @Description: 本文件中的接口直接向外提供服务，这些接口需要映射到外部
* @Date: Created in 14:39 2017-8-21
*/
import com.midea.meicloud.common.Constants;
import com.midea.meicloud.common.EncryptedData;
import com.midea.meicloud.auth.service.RsaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/public")
public class UserControllerPublic {
    @Autowired
    RsaUserService myUserService;

    class Pair{
        String randomString;
        String publicKey;
        public String getRandomString() {
            return randomString;
        }
        public void setRandomString(String random) {
            this.randomString = random;
        }
        public String getPublicKey() {
            return publicKey;
        }
        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }
    }

    @GetMapping("/public_key")
    public ResponseEntity<?> getPublicKey(HttpServletRequest request){
        String strRandom = UUID.randomUUID().toString();
        request.getSession().setAttribute(Constants.randomStr, strRandom);
        Pair p = new Pair();
        p.setPublicKey(myUserService.getPublicKey());
        p.setRandomString(strRandom);
        return ResponseEntity.ok(p);
    }

    @PostMapping(value = "/rsa_login")
    public ResponseEntity<?> rsa_login(@RequestBody EncryptedData data, HttpServletRequest request){
        Map<String , Object> mp = myUserService.rsaLogin(data, request);
        if(mp.get(Constants.result).equals(true)){
            return ResponseEntity.ok("");
        }
        else{
            return ResponseEntity.badRequest().body(mp.get(Constants.reason));
        }
    }

    @PostMapping(value = "/rsa_signup")
    public ResponseEntity<?> rsaSignUp(@RequestBody EncryptedData data, HttpServletRequest request){
        Map<String , Object> mp = myUserService.rsaSignup(data, request);
        if(mp.get(Constants.result).equals(true)){
            return ResponseEntity.ok("");
        }
        else{
            return ResponseEntity.badRequest().body(mp.get(Constants.reason));
        }
    }

}
