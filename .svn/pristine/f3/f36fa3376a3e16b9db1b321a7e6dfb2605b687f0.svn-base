package com.dhht.entity.weixin;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 非访客离线记录标记
 * @author Administrator
 *
 */
@Entity
@Table(name = "offline_record_flag")
public class OfflineRecordFlag implements Serializable{

	private static final long serialVersionUID = 2401740738433770790L;
	
	//主键
	@Id
	@Column(nullable = false,length = 32 ,unique = true)
	private String id;
	
	//设备MAC
    @Column(nullable = false, length = 12)
	private String mac;
	
    //密码
  	@Column(nullable = false,length = 6)
	private String secret;
  	
  	@Column(nullable = false,length = 32)
  	private String personId;

  	@Column(length = 20)
  	private String time;
  	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
