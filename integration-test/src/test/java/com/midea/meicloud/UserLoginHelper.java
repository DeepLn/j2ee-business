package com.midea.meicloud;

import com.midea.meicloud.common.EncryptedData;
import com.midea.meicloud.common.MyJson;
import com.midea.meicloud.common.RSACoder;
import com.midea.meicloud.user.UserTest;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 9:54 2017-8-30
 */

public class UserLoginHelper {

    boolean isLogin = false;

    private UserLoginHelper(){}

    static UserLoginHelper ins = new UserLoginHelper();

    public static UserLoginHelper instance(RestTemplate restTemplate){
        ins.testRestTemplate = restTemplate;
        return ins;
    }

    static final String baseUrl = "http://gateway/user/api/public";

    private RestTemplate testRestTemplate;

    PublicKeyRandom publicKeyRandom;

    static class PublicKeyRandom{
        String publicKey;
        String randomString;

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public String getRandomString() {
            return randomString;
        }

        public void setRandomString(String randomString) {
            this.randomString = randomString;
        }
    }

    static class UserInfo {
        String username;
        String password;
        String randomString;

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

        public String getRandomString() {
            return randomString;
        }

        public void setRandomString(String randomString) {
            this.randomString = randomString;
        }
    }
    private static EncryptedData makeloginJson(String publicKey, String random, String user, String pwd) throws Exception{
        UserInfo info = new UserInfo();
        info.setPassword(pwd);
        info.setUsername(user);
        info.setRandomString(random);
        EncryptedData data = RSACoder.encryptLongContentByPublicKey(MyJson.toJson(info), publicKey);
        return data;
    }

    private EncryptedData makeRegistJson(String publicKey, String random, String user, String pwd) throws Exception{
        return makeloginJson(publicKey, random, user, pwd);
    }

    public boolean login(String user, String pwd) throws Exception{
        if(isLogin == false){
            String url = "http://gateway/user/api/public/public_key";
            ResponseEntity<PublicKeyRandom> res = testRestTemplate.getForEntity(url, PublicKeyRandom.class);
            assert(HttpStatus.OK == res.getStatusCode());
            publicKeyRandom = res.getBody();
            url = baseUrl + "/rsa_login";
            EncryptedData data = makeloginJson(publicKeyRandom.getPublicKey(), publicKeyRandom.getRandomString(), user,  pwd);
            ResponseEntity<?> res2 = testRestTemplate.postForEntity(url, data, Object.class);
            Assert.assertEquals(HttpStatus.OK, res2.getStatusCode());
            return true;
        }
        isLogin = true;
        return true;
    }
}
