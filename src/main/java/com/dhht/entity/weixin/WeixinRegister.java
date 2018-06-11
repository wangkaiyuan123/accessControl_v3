package com.dhht.entity.weixin;

import java.io.Serializable;

public class WeixinRegister implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String personName;
	private String personNumber;
	private String personPhone;
	private String codeNumber;
	private String personLocation;//省 市 区
	private String personAddress;
	private String personType;
	private Integer pageNumber;
	
	private String deviceNo;//设备号
	private String deviceMac;//mac
	private String realUnitId;//realUnitId 单元id
	private String userName;  //登录用户名
	private String password;  //用户登录密码
	
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getPersonNumber() {
		return personNumber;
	}
	public void setPersonNumber(String personNumber) {
		this.personNumber = personNumber;
	}
	public String getPersonPhone() {
		return personPhone;
	}
	public void setPersonPhone(String personPhone) {
		this.personPhone = personPhone;
	}
	public String getCodeNumber() {
		return codeNumber;
	}
	public void setCodeNumber(String codeNumber) {
		this.codeNumber = codeNumber;
	}
	public String getPersonLocation() {
		return personLocation;
	}
	public void setPersonLocation(String personLocation) {
		this.personLocation = personLocation;
	}
	public String getPersonAddress() {
		return personAddress;
	}
	public void setPersonAddress(String personAddress) {
		this.personAddress = personAddress;
	}
	public String getPersonType() {
		return personType;
	}
	public void setPersonType(String personType) {
		this.personType = personType;
	}
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	public String getDeviceMac() {
		return deviceMac;
	}
	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}
	public String getRealUnitId() {
		return realUnitId;
	}
	public void setRealUnitId(String realUnitId) {
		this.realUnitId = realUnitId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
