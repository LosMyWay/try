package com.yjq.programmer.dto;

import com.yjq.programmer.annotation.ValidateEntity;

import java.util.Date;

/**
 * @author 蔡中宇
 * @create 2023-04-24
 */
public class BookDTO {

    private String id;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=32,minLength=1,errorRequiredMsg="图书名称不能为空！",errorMaxLengthMsg="图书名称长度不能大于32！",errorMinLengthMsg="图书名称不能为空！")
    private String name;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=128,minLength=1,errorRequiredMsg="图书简介不能为空！",errorMaxLengthMsg="图书简介长度不能大于128！",errorMinLengthMsg="图书简介不能为空！")
    private String info;

    private Integer state;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=16,minLength=1,errorRequiredMsg="图书作者不能为空！",errorMaxLengthMsg="图书作者长度不能大于16！",errorMinLengthMsg="图书作者不能为空！")
    private String author;

    private Date createTime;

    private String photo;

    private Integer version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", info=").append(info);
        sb.append(", state=").append(state);
        sb.append(", author=").append(author);
        sb.append(", createTime=").append(createTime);
        sb.append(", photo=").append(photo);
        sb.append(", version=").append(version);
        sb.append("]");
        return sb.toString();
    }
}
