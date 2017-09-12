package com.midea.meicloud.entitybase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.midea.meicloud.common.MyJson;
import com.midea.meicloud.common.RSACoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @auth: 陈佳攀
 * @Description:
 * @Date: Created in 17:54 2017-8-23
 */
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //数据创建者ID
    @JsonIgnore
    private Long createdBy;
    //数据创建时间
    @JsonIgnore
    @NotNull
    private Date createDate;
    //数据最后修改人ID
    @JsonIgnore
    private Long updatedBy;
    //数据最后修改时间
    @JsonIgnore
    private Date updateDate;
    //数据版本
    @JsonIgnore
    @Version
    private Long version;

    @Transient
    private String verify;

    //从version和ID 产生verify
    public void makeVerify(){
        if (verify == null){
            BaseEntity ett = this;
            String json = MyJson.toJson(ett);
            verify = RSACoder.encryptBASE64(json.getBytes());
        }
    }

    public String getVerify() {
        return verify;
    }

    //从verify产生ID和version并验证ID
    public void doVerify() throws Exception{
        byte[] byVerify = RSACoder.decryptBASE64(this.verify);
        if (byVerify != null){
            String decVerify = new String(byVerify);
            BaseEntity ett = MyJson.fromJson(decVerify, BaseEntity.class);
            if (ett.getId() == this.getId())
            {
                this.setVersion(ett.getVersion());
                this.setCreatedBy(ett.getCreatedBy());
                this.setCreateDate(ett.getCreateDate());
                this.setUpdatedBy(ett.getUpdatedBy());
                this.setUpdateDate(ett.getUpdateDate());
                return;
            }
        }
        throw new Exception("base entity verify failed");
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
