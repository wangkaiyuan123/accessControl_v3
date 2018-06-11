package com.dhht.entity.visitors;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 访客离线记录标记
 * @author Administrator
 *
 */
@Entity
@Table(name = "visitor_offline_flag")
public class VisitorOfflineFlag implements Serializable{

	private static final long serialVersionUID = 7908358054336159068L;
	
	//主键
	@Id
	@Column(nullable = false,length = 32 ,unique = true)
	private String id;
	
	//记录访客Id
	@Column(length = 32)
	private String visitorRecordId;
	
	//密码
	@Column(nullable = false,length = 6)
	private String secreat;
	
	//mac
	@Column(nullable = false,length = 12)
	private String mac;
	
	@Column(length = 20)
	private String time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVisitorRecordId() {
		return visitorRecordId;
	}

	public void setVisitorRecordId(String visitorRecordId) {
		this.visitorRecordId = visitorRecordId;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getSecreat() {
		return secreat;
	}

	public void setSecreat(String secreat) {
		this.secreat = secreat;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
