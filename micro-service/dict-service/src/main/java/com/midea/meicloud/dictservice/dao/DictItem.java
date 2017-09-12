package com.midea.meicloud.dictservice.dao;

import com.midea.meicloud.entitybase.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 11:40 2017-8-22
 */

@Entity
public class DictItem extends BaseEntity {

    //主表ID，保存在这里作为关联条件
    @NotNull
    private Long dictId;
    @NotNull
    private String name;
    @NotNull
    private String value;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDictId() {
        return dictId;
    }

    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
