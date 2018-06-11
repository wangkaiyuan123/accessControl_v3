package com.dhht.entity.unit;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 * 楼单元实体类
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name="unit")
public class Unit implements Serializable{
	//主键
	@Id
	@Column(nullable = false, length = 32, unique = true)
	private String id;
	
	//楼单元名
	@Column(nullable = false, length = 20)
	private String unitName;
	
	//小区Id
	@Column(nullable = false, length = 32)
    private String communityId;
	
	//是否未大门(0 不是大门   1是大门)
	@Column(nullable = false,columnDefinition = "TINYINT(1) DEFAULT 0")
	private Integer isGurad;
    
	//小区名
	@Transient
	private String communityName;
	
	

	public String getCommunityId() {
		return communityId;
	}

	
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}


	public Integer getIsGurad() {
		return isGurad;
	}


	public void setIsGurad(Integer isGurad) {
		this.isGurad = isGurad;
	}
}
