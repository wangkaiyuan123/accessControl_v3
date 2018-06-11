package com.dhht.entity.doorguard;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "device_card")
//卡门禁关联表，存进去最好不要删,某个设备存满了1000.在统一删
public class DeviceCard implements Serializable{
	//主键
	@Id
	@Column(nullable = false,length = 32, unique = true)
	private String id;
	
	//卡号
	@Column(nullable = false,length = 32)
	private String cardNo;
	
	//设备mac地址
	@Column(nullable = false,length = 12)
	private String mac;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}
	
	

}
