package com.dhht.entity.doorguard;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 *  门禁设备表
 */
@Entity
@Table(name = "device")
public class Device implements Serializable{

	private static final long serialVersionUID = 1L;
    //主键
	@Id
	@Column(nullable = false,length = 32, unique = true)
	private String id;
	
	//所在单元id,未接入到小区之前是null
	@Column(nullable = true,length = 32)
	private String unitId;
	
	
	//TCP端口()
	@Column(length = 5)
	private String TcpPort;
	
	//设备端口
	@Column(length = 5)
	private String devicePort;
	
	//mac地址
	@Column(nullable = false,length = 12,unique = true)
	private String deviceMac;
	
	//省
	@Transient
	private String provinceId;
	
	//市
	@Transient
	private String cityId;
	
	//区 、县
	@Transient
	private String districtId;
	
	//街道
	@Transient
	private String streetId;
	
	//小区
	@Transient
	private String communityId;
	
	//门禁地址  （隆和国际8楼801）
	@Column(nullable = false,length = 50)
	private String deviceAddress;
	
	//门禁密码
	@Column(length = 10)
	private String  password;
	
	
	//状态 (默认为0 ：未启动     1启动)
	@Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Integer state;
	
	//是否为读卡设备(默认为 是)
	@Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
	private Integer isReadCard;
	
	//授权模式(0:非授权模式  所有卡都能开   1:授权模式)
	@Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Integer authotity;
	
	//设备新增人
	@Column(length = 32,nullable = false)
	private  String addPerson;
	
	//是否已删除(0:未删除 1：已删除)
	@Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Integer isdelete;
	
	@Transient
	private String online;
 
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTcpPort() {
		return TcpPort;
	}

	public void setTcpPort(String tcpPort) {
		TcpPort = tcpPort;
	}

	public String getDevicePort() {
		return devicePort;
	}

	public void setDevicePort(String devicePort) {
		this.devicePort = devicePort;
	}

	public String getDeviceMac() {
		return deviceMac;
	}

	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}

	public String getDeviceAddress() {
		return deviceAddress;
	}

	public void setDeviceAddress(String deviceAddress) {
		this.deviceAddress = deviceAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getIsReadCard() {
		return isReadCard;
	}

	public void setIsReadCard(Integer isReadCard) {
		this.isReadCard = isReadCard;
	}

	public Integer getAuthotity() {
		return authotity;
	}

	public void setAuthotity(Integer authotity) {
		this.authotity = authotity;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getStreetId() {
		return streetId;
	}

	public void setStreetId(String streetId) {
		this.streetId = streetId;
	}

	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

	public String getAddPerson() {
		return addPerson;
	}

	public void setAddPerson(String addPerson) {
		this.addPerson = addPerson;
	}

	public String getOnline() {
		return online;
	}

	public void setOnline(String online) {
		this.online = online;
	}

	public Integer getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(Integer isdelete) {
		this.isdelete = isdelete;
	}
}
