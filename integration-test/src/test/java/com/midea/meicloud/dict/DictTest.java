package com.midea.meicloud.dict;

import java.util.List;

import com.midea.meicloud.RestTemplateConfig;
import com.midea.meicloud.UserLoginHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 18:21 2017-9-4
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestTemplateConfig.class)
public class DictTest {

    String baseUrl = "http://gateway/dict/api";

    static class Dict {
        String verify;
        String dictCode;

        public String getVerify() {
            return verify;
        }

        public void setVerify(String verify) {
            this.verify = verify;
        }

        public String getDictCode() {
            return dictCode;
        }

        public void setDictCode(String dictCode) {
            this.dictCode = dictCode;
        }

        public String getDictName() {
            return dictName;
        }

        public void setDictName(String dictName) {
            this.dictName = dictName;
        }

        public String getDictDescription() {
            return dictDescription;
        }

        public void setDictDescription(String dictDescription) {
            this.dictDescription = dictDescription;
        }

        String dictName;
        String dictDescription;
    }

    @Autowired
    RestTemplate restTemplate;

    @Before
    public void setup() throws Exception {
        UserLoginHelper.instance(restTemplate).login("root", "root123");
        Dict exam = new Dict();
        exam.setDictName("test");
        ResponseEntity<Dict[]> lst = restTemplate.getForEntity(baseUrl + "/authed/dicts", Dict[].class, exam);
        for(Dict dct : lst.getBody()){
            restTemplate.delete(baseUrl + "/authed/dict", dct);
        }
    }

    @Test
    public void addDictTest() throws Exception {
    }

}
