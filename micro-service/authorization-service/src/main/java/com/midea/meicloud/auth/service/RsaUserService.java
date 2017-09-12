package com.midea.meicloud.auth.service;

import com.midea.meicloud.common.*;
import com.midea.meicloud.auth.entity.MyUser;
import com.midea.meicloud.auth.repository.MyUserRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class RsaUserService {
    public RsaUserService() {
        initRSAKey();
    }

    //    @Value("${rsa.publicKey}")
    private String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCekcTcnK2SblaJWBoWhat7nBFY4x3TGYkdnyWtE53Fdvx6r0nEbaGIhF2uW+IszN2IMcUj6DakV4GhF60Q4ZA3+E68fiR6PU0Q/SAm6qNb6qfxDkao5fOIVtaZCH6QTPCmb/kD5ePhe7Bnh7P16ZSmHtPv4j1QL2uGNF6IAyJ1CQIDAQAB";
    //    @Value("${rsa.privateKey}")
    private String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJ6RxNycrZJuVolYGhaFq3ucEVjjHdMZiR2fJa0TncV2/HqvScRtoYiEXa5b4izM3YgxxSPoNqRXgaEXrRDhkDf4Trx+JHo9TRD9ICbqo1vqp/EORqjl84hW1pkIfpBM8KZv+QPl4+F7sGeHs/XplKYe0+/iPVAva4Y0XogDInUJAgMBAAECgYAK56VtMo0xQ8tJuLhakii/4HTE8yGK8fefBOHXbEDuYodsfH+LNarlM40pv4HnaNNXWWzgUkjntmjgxjsNrSV4yCGS1Y3zVxjvgG5qaZXqlB288yeX2p2hhRIpCOxZXxuugVsiYDdPMkdETUvG3THRjho6s2fX6XL8wA6ZFvxcnQJBANdzic0l9WMNaTm5ElVueKaAFFUbxjJ96kmqQBdOHFCkxWF3K8FuP6wJDa+zZdQSZr3HnEgdkU9DM198D1IPwk8CQQC8aafdFWpheZ1ZE6Y4Jg2xw0PD/E8B/K5tTENo9bRfEctMx/OOozgvgbdOMxswrW4hYxJ4+ndnZ8IoeaqHhrUnAkBj5Kv1tC3MGUG6g7aeabQC7st+knwYmQzxzsAcjhjOwzbI8+oTqzxWVXRFDJaf91Avmcc6IItpBq1hDjJESA49AkEAuvSEEsZbRtmsHmV2/CQWVpuRNHm51Bjs45tXEGEuV1+KwWdu78xZxhoKz9e6VTTiINLz04OE0+CLMip34f7y5QJAJzB8J+n4Sn7H9385rYSTQeNegqsc30ozACg3ZlUZB5HSNuCoxnueFTUSA9YSeEl9uwNztebZZZfkhJLprEr3gg==";

    protected Logger log = Logger.getLogger(RsaUserService.class);
    @Autowired
    MyUserRepository myUserRepository;

    private void initRSAKey() {
        try {
            if (privateKey == null || publicKey == null) {
                RSACoder.initKey();
            } else {
                RSACoder.setKey(publicKey, privateKey);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    private String StringRsaEncryptBase64(String str) throws Exception {
        byte[] bt = RSACoder.encryptByPublicKey(str.getBytes());
        return RSACoder.encryptBASE64(bt);
    }

    private String keyDataDecrypt(EncryptedData data) throws Exception {
        return RSACoder.decryptLongContentByPrivateKey(data);
    }

    class UsernameAndPassword {
        private String username;
        private String password;
        private String randomString;

        public String getRandomString() {
            return randomString;
        }

        public void setRandomString(String randomString) {
            this.randomString = randomString;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public String getPublicKey() {
        String publicKey = RSACoder.getPublicKey();
        log.debug(publicKey);
        return publicKey;
    }

    private void setSessionAttribute(HttpSession session, TempUserInfo info) {
        session.setAttribute(Constants.sessionAttributeUserInfo, info);
        log.info("set token to cookie done");
    }

    public Map<String, Object> rsaLogin(EncryptedData data, HttpServletRequest request) {
        log.info("user try login");
        Map<String, Object> mp = new HashMap<>();
        mp.put(Constants.result, false);
        mp.put(Constants.reason, "login info decrypt error");
        try {
            String loginInfo = keyDataDecrypt(data);
            mp.put(Constants.reason, "login info convert error");
            UsernameAndPassword userNameAndPassword = MyJson.fromJson(loginInfo, UsernameAndPassword.class);
            String strRandom = (String)request.getSession().getAttribute(Constants.randomStr);
            if(strRandom == null || !strRandom.equals(userNameAndPassword.getRandomString())){
                mp.put(Constants.reason, "random string error");
                log.info("login failed: random str is error");
                throw new Exception();
            }
            MyUser myUser = myUserRepository.findFirstByUsername(userNameAndPassword.getUsername());
            if (myUser == null) {
                mp.put(Constants.reason, "username do not exist");
                log.info("username: " + userNameAndPassword.getUsername() + " do not exist");
                throw new Exception();
            }
            if (!myUser.getPassword().equals(userNameAndPassword.getPassword())) {
                log.info("password is error for username: " + userNameAndPassword.getUsername());
                mp.put(Constants.reason, "password error");
                throw new Exception();
            }
            TempUserInfo info = new TempUserInfo(myUser.getUsername());
            info.setUserid(myUser.getId());
            refreshUserData(myUser);
            setSessionAttribute(request.getSession(), info);
            mp.put(Constants.result, true);
            mp.put(Constants.reason, null);
            log.info("user login success : " + userNameAndPassword.getUsername());
        } catch (Exception e) {
            log.warn(e.getMessage());
            log.warn(e.getStackTrace());
        } finally {
            return mp;
        }
    }

    private void refreshUserData(MyUser myUser) {
        myUser.setLastLoginDate(new Date());
        myUser.setOnLine(true);
        myUserRepository.saveAndFlush(myUser);
    }

    public Map<String, Object> rsaSignup(EncryptedData data,  HttpServletRequest request) {
        log.info("user try sign up");
        Map<String, Object> mp = new HashMap<>();
        mp.put(Constants.result, false);
        mp.put(Constants.reason, "sign up info decrypt error");
        try {
            String signInfo = keyDataDecrypt(data);
            mp.put(Constants.reason, "sign info convert error");

            UsernameAndPassword userNameAndPassword = MyJson.fromJson(signInfo, UsernameAndPassword.class);
            String strRandom = (String)request.getSession().getAttribute(Constants.randomStr);
            if(userNameAndPassword == null || strRandom == null || !strRandom.equals(userNameAndPassword.getRandomString())){
                mp.put(Constants.reason, "random string error");
                throw new Exception();
            }
            MyUser myUser = MyJson.fromJson(signInfo, MyUser.class);
            if(myUser == null)
            {
                mp.put(Constants.reason, "sign info format error");
                throw new Exception();
            }
            if (myUser.getUsername().length() < 4) {
                mp.put(Constants.reason, "username is too short");
                throw new Exception();
            }
            if (myUser.getPassword().length() < 4) {
                mp.put(Constants.reason, "password is too short");
                throw new Exception();
            }
            if (myUserRepository.findFirstByUsername(myUser.getUsername()) != null) {
                mp.put(Constants.reason, "username already exist");
                throw new Exception();
            }
            myUser.setOnLine(false);
            myUser.setLastLoginDate(null);
            MyUser us = myUserRepository.save(myUser);
            if (us == null) {
                mp.put(Constants.reason, "save user info error occur");
                log.info("username: " + myUser.getUsername() + " exist already");
                throw new Exception();
            }
            mp.put(Constants.result, true);
            mp.put(Constants.reason, null);
            log.info("user sign success : " + us.getUsername());
        } catch (Exception e) {
            log.warn(e.getMessage());
            log.warn(e.getStackTrace());
        } finally {
            return mp;
        }
    }

}