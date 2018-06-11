package com.dhht.entity.region;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSON;
/**
 * 组织架构表
 * @author Administrator
 *
 */
@Entity
@Table(name = "region")
public class Region implements Serializable{
	private static final long serialVersionUID = 1L;
	
	//主键
	@Id
	@Column(nullable = false, length = 32, unique = true)
	private String id;
	
	//区域名
	@Column(length = 10 ,nullable = false)
	private String regionName;
	
	//上级ID
	@Column(length = 32)
	private String parentId;
	
	//区域划分等级(省-市-区/县-街道-小区)
	@Column(columnDefinition = "TINYINT(1)")
	private Integer level;
	
	//是否未大门(0 不是大门   1是大门)
	@Column(nullable = false,columnDefinition = "TINYINT(1) DEFAULT 0")
	private Integer isGurad;
	
	@Transient
	private List<Region> children;
	
	//***************用户申请界面使用，勿删*****************
	@Transient
	private List<Region> sub;
	@Transient
	private String name;
	@Transient
	private String code;
	//***************用户申请界面使用，勿删*****************

	public String getId() {
		return id;
	}

	public List<Region> getSub() {
		return sub;
	}


	public void setSub(List<Region> sub) {
		this.sub = sub;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public List<Region> getChildren() {
		return children;
	}

	public void setChildren(List<Region> children) {
		this.children = children;
	}
	public Integer getIsGurad() {
		return isGurad;
	}

	public void setIsGurad(Integer isGurad) {
		this.isGurad = isGurad;
	}

	@Override
	public String toString() {
		if(this.sub!=null){
			return "name:'"+this.name+"',code:'"+this.code+"',sub:[{"+JSON.toJSONString(this.sub)+"}]";
		}
		return "name:'"+this.name+"',code:'"+this.code+"'";
	}
}
