package com.dhht.entity.record;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 *  开门记录表
 */
@Entity
@Table(name = "openDoorRecord")
public class OpenDoorRecord implements Serializable{

	/*
	 * 主键id
	 */
	@Id
	@Column(nullable = false, length = 32, unique = true)
	private String id;
	
	//人员Id
	@Column(nullable = false, length = 32)
	private String personId;
	
//	//用户类型(1:内部人员  2:预约访客  3：邀请访客)
//	@Column(nullable = false, columnDefinition = "TINYINT(1)")
//	private Integer userType;
	
	//开门方式(1:门卡  2:微信扫码  3:密码开门)
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private Integer openType;
	
//	//区域Id
//	@Column(nullable = false,length = 32)
//	private String regionId;
	
	//地点
	@Column(nullable = false,length = 50)
	private String address;
	
	//设备Mac
	@Column(nullable = false,length = 12)
	private String deviceMac;
	
	@Column
	private Date openDate;
	
	//开门人
	@Column(nullable = true,length = 5)
	private String openDoorPerson;
	
	
	//*************开门记录前端使用***************
	@Transient
	private String openDateStr;
	@Transient
	private String openTypeStr;
	//*************开门记录前端使用***************
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public Integer getOpenType() {
		return openType;
	}

	public void setOpenType(Integer openType) {
		this.openType = openType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDeviceMac() {
		return deviceMac;
	}

	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public String getOpenDateStr() {
		return openDateStr;
	}

	public void setOpenDateStr(String openDateStr) {
		this.openDateStr = openDateStr;
	}

	public String getOpenTypeStr() {
		return openTypeStr;
	}

	public void setOpenTypeStr(String openTypeStr) {
		this.openTypeStr = openTypeStr;
	}

	public String getOpenDoorPerson() {
		return openDoorPerson;
	}

	public void setOpenDoorPerson(String openDoorPerson) {
		this.openDoorPerson = openDoorPerson;
	}
}
