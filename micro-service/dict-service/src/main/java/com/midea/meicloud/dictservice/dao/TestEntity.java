package com.midea.meicloud.dictservice.dao;

import com.midea.meicloud.entitybase.BaseEntity;

import javax.persistence.Entity;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 14:14 2017-8-26
 */
@Entity
public class TestEntity extends BaseEntity{
    public String getAtt1() {
        return att1;
    }

    public void setAtt1(String att1) {
        this.att1 = att1;
    }

    public String getAtt2() {
        return att2;
    }

    public void setAtt2(String att2) {
        this.att2 = att2;
    }

    public String getAtt3() {
        return att3;
    }

    public void setAtt3(String att3) {
        this.att3 = att3;
    }

    public Long getAtt4() {
        return att4;
    }

    public void setAtt4(Long att4) {
        this.att4 = att4;
    }

    String att1;
    String att2;
    String att3;
    Long att4;
}
