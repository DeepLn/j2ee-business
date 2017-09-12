package com.midea.meicloud.dictservice.service;

import com.midea.meicloud.dictservice.dao.TestEntity;
import com.midea.meicloud.dictservice.dao.TestEntityRepo;
import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 14:33 2017-8-22
 */
@Service
public class TestService {
    @Autowired
    private RestTemplate restTemplate;

    static class Pair{
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

    public ResponseEntity<Pair>  getPublicKey(){
        Pair res = restTemplate.getForObject("http://authorization-service/api/public/public_key", Pair.class);
        return ResponseEntity.ok(res);
    }

    static class userinfo{
        String username;
        String password;

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

    public ResponseEntity<?> getUserInfo(){
        return restTemplate.getForEntity("http://authorization-service/api/local/info/"+ 8, userinfo.class);
    }

    public ResponseEntity<?> getUserInfo2(){
        return restTemplate.getForEntity("http://authorization-service/api/authed/manage/userlist", userinfo[].class);
    }

    @Autowired
    TestEntityRepo testEntityRepo;

    @Cacheable(value="testEntity",key="'id:'+#p0")
    public TestEntity getTestEntityRedis(Long id){
        return testEntityRepo.getOne(id);
    }

    @CachePut(value="testEntity", key="'id:'+#p0.id")
    public void putTestEntityRedis(TestEntity testEntity){
        testEntityRepo.save(testEntity);
    }

    @CachePut(value="testEntity",key="'id:'+#p0.id")
    public void updateTestEntityRedis(TestEntity testEntity){
        testEntityRepo.saveAndFlush(testEntity);
    }

    public TestEntity getTestEntity(Long id){
        return testEntityRepo.getOne(id);
    }

    public void putTestEntity(TestEntity testEntity){
        testEntityRepo.save(testEntity);
    }

    public void updateTestEntity(TestEntity testEntity){
        testEntityRepo.saveAndFlush(testEntity);
    }

    static class TestTime{
        Long testTimes;
        Long testCostTime;

        public Long getTestTimes() {
            return testTimes;
        }

        public void setTestTimes(Long testTimes) {
            this.testTimes = testTimes;
        }

        public Long getTestCostTime() {
            return testCostTime;
        }

        public void setTestCostTime(Long testCostTime) {
            this.testCostTime = testCostTime;
        }

        public Long getTestCostTimeRedis() {
            return testCostTimeRedis;
        }

        public void setTestCostTimeRedis(Long testCostTimeRedis) {
            this.testCostTimeRedis = testCostTimeRedis;
        }

        public String getTestName() {
            return testName;
        }

        public void setTestName(String testName) {
            this.testName = testName;
        }

        Long testCostTimeRedis;
        String testName;
    }

    public ResponseEntity<?> testRedisGet(){
        Date bf = new Date();
        for (int i=1; i<=1000; i++){
            getTestEntity(new Long(i));
        }
        Date now = new Date();
        Long testCost = now.getTime() - bf.getTime();
        TestTime t = new TestTime();
        t.testTimes = new Long(1000);
        t.testName = "getTestEntity";
        t.testCostTime = testCost;
        bf = new Date();
        for (int i=1; i<=1000; i++){
            getTestEntityRedis(new Long(i));
        }
        now = new Date();
        testCost = now.getTime() - bf.getTime();
        t.testCostTimeRedis = testCost;

        return ResponseEntity.ok(t);
    }

    public ResponseEntity<?> testRedisPut(){
        Date bf = new Date();
        for (int i=1; i<=1000; i++){
            TestEntity et = new TestEntity();
            et.setAtt1(new Long(i).toString());
            et.setId(new Long(i));
            putTestEntity(et);
        }
        Date now = new Date();
        Long testCost = now.getTime() - bf.getTime();
        TestTime t = new TestTime();
        t.testTimes = new Long(1000);
        t.testName = "putTestEntity";
        t.testCostTime = testCost;
        bf = new Date();
        for (int i=1; i<=1000; i++){
            TestEntity et = new TestEntity();
            et.setAtt1(new Long(i).toString());
            et.setId(new Long(i));
            putTestEntityRedis(et);
        }
        now = new Date();
        testCost = now.getTime() - bf.getTime();
        t.testCostTimeRedis = testCost;

        return ResponseEntity.ok(t);
    }

    public ResponseEntity<?> testRedisUpdate(){
        Date bf = new Date();
        for (int i=1; i<=1000; i++){
            TestEntity et = new TestEntity();
            et.setAtt1(new Long(i+1).toString());
            et.setId(new Long(i));
            updateTestEntity(et);
        }
        Date now = new Date();
        Long testCost = now.getTime() - bf.getTime();
        TestTime t = new TestTime();
        t.testTimes = new Long(1000);
        t.testName = "updateTestEntity";
        t.testCostTime = testCost;
        bf = new Date();
        for (int i=1; i<=1000; i++){
            TestEntity et = new TestEntity();
            et.setAtt1(new Long(i+1).toString());
            et.setId(new Long(i));
            updateTestEntityRedis(et);
        }
        now = new Date();
        testCost = now.getTime() - bf.getTime();
        t.testCostTimeRedis = testCost;

        return ResponseEntity.ok(t);
    }
}
