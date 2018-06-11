package com.dhht.entity.record;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.dhht.entity.system.Person;

/**
 * 用户申请记录表
 * 
 * @author Administrator
 * 
 */
@Entity
@Table(name = "applyRecord")
public class UserApplyRecord implements Serializable {

	private static final long serialVersionUID = 1L;
	// 主键
	@Id
	@Column(nullable = false, length = 32, unique = true)
	private String id;

	// 申请人员id
	@Column(nullable = false, length = 32)
	private String personId; 
	
	// 申请的单元id
	@Column(nullable = false, length = 32)
	private String unitId; 

	// 申请状态(0审核中 1审核通过 2审核失败 )
	@Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Integer state;

	// 申请时间
	@Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date applyDate;
	
	//审核时间
	@Column(nullable = true, columnDefinition = "TIMESTAMP")
	private Date checkDate;
	

	//审核人
	@Column(nullable = true,length = 10)
	private String checkPerson;
	
	//申请人名字
	@Column(nullable = false,length = 10)
	private String name;
	
	@Column(nullable = false, length = 18)
	private String identifyId;
	
	// 电话号码
	@Column(nullable = false, length = 11)
	private String telephone;
	
	// 关联的微信openId，可以为空
	@Column(nullable = true, length = 50)
	private String openid;
	
	//审核图片
	@Column(nullable = true,length = 100)
	private String imgUrl;
	
	@Transient
	private String unitName;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentifyId() {
		return identifyId.replace(identifyId.substring(6, 14), "********");
	}

	public void setIdentifyId(String identifyId) {
		this.identifyId = identifyId;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getCheckPerson() {
		return checkPerson;
	}

	public void setCheckPerson(String checkPerson) {
		this.checkPerson = checkPerson;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
}
