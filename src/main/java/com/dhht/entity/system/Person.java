package com.dhht.entity.system;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 人员实体类
 */
@Entity
@Table(name = "person")
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;

	/*
	 * 用户id
	 */
	@Id
	@Column(nullable = false, length = 32, unique = true)
	private String id;

	// 关联的微信openId，可以为空
	@Column(nullable = true, length = 50, unique = true)
	// 该字段必须保证唯一性
	private String openid;

	// 用户姓名
	@Column(nullable = false, length = 10)
	private String name;

	// 年龄
	@Column(nullable = false, columnDefinition = "INT(4) DEFAULT 0")
	private Integer age;

	// 性别 (0:保密 1男 2女)
	@Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Integer sex;

	// 电话号码
	@Column(nullable = false, length = 11)
	private String telephone;

	// 用户类型Id
	@Column(nullable = false, length = 32)
	private String personTypeId;

	// 状态( 1:正常  2审核中 3 冻结 )
	@Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
	private Integer state;

	// 卡号,新增的时候可以不添加卡号
	@Column(nullable = true, length = 32)
	private String cardId;

	// 身份证号
	@Column(nullable = false, length = 18, unique = true)
	private String identifyId;

	// 身份证图片id
	@Column(nullable = true, length = 32)
	private String identifyPicId;
	
	//用户类型
	@Transient
	private String personType;
	
	//单元名
	@Transient
	private String unitName;
	
	//绑定的微信用户Id
	@Transient
	private String neckName ="";
	
	@Transient
	private List<String> unitIds;
	
	@Transient
	private  String  regionId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPersonTypeId() {
		return personTypeId;
	}

	public void setPersonTypeId(String personTypeId) {
		this.personTypeId = personTypeId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getIdentifyId() {
		return identifyId;
	}

	public void setIdentifyId(String identifyId) {
		this.identifyId = identifyId;
	}

	public String getIdentifyPicId() {
		return identifyPicId;
	}

	public void setIdentifyPicId(String identifyPicId) {
		this.identifyPicId = identifyPicId;
	}

	public String getPersonType() {
		return personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getNeckName() {
		return neckName;
	}

	public void setNeckName(String neckName) {
		this.neckName = neckName;
	}

	public List<String> getUnitIds() {
		return unitIds;
	}

	public void setUnitIds(List<String> unitIds) {
		this.unitIds = unitIds;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	
}
