package com.dhht.entity.system;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "operate_picture")
public class OperatePicture implements Serializable{
	private static final long serialVersionUID = 1L;
	/*
     * 主键id
     */
    @Id
    @Column(nullable = false, length = 32, unique = true)
    private String id;
    
	@Column(name = "picturepath")//图片全路径
	private String picturePath; 
	
	@Column(name = "createtime")//创建时间
	private Date createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPicturePath() {
		return picturePath;
	}

	public void setPicturePath(String picturePath) {
		this.picturePath = picturePath;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
