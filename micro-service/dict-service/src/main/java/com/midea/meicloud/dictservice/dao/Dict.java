package com.midea.meicloud.dictservice.dao;

import com.midea.meicloud.entitybase.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 11:40 2017-8-22
 */

@Entity
public class Dict extends BaseEntity {
    @NotNull
    private String dictCode;
    @NotNull
    private String dictName;
    private String dictDescription;

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
}
