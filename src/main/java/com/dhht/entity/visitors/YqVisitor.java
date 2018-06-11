package com.dhht.entity.visitors;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "yq_visitor")
public class YqVisitor implements Serializable{
	private static final long serialVersionUID = 1L;
    //主键
	@Id
	@Column(nullable = false,length = 32 ,unique = true)
	private String id;
	
	//受邀访客姓名
	@Column(nullable = false, length = 10)
	private String invitedName;
	
	//受邀访客手机号
	@Column(nullable = false, length = 11)
	private String invitedPhoneNumber;
	
	//邀请人
	@Column(nullable = false, length = 10)
	private String inviteName;
	
	//邀请时间
	@Column(nullable = false)
	private Date inviteTime;
	
	//微信昵称(受邀访客的微信)
	@Column(nullable = false,length = 15)
	private String weixinName;
	
	//门禁地址
	@Column(nullable = false,length = 50)
	private String address;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInvitedName() {
		return invitedName;
	}

	public void setInvitedName(String invitedName) {
		this.invitedName = invitedName;
	}

	public String getInvitedPhoneNumber() {
		return invitedPhoneNumber;
	}

	public void setInvitedPhoneNumber(String invitedPhoneNumber) {
		this.invitedPhoneNumber = invitedPhoneNumber;
	}

	public String getInviteName() {
		return inviteName;
	}

	public void setInviteName(String inviteName) {
		this.inviteName = inviteName;
	}

	public Date getInviteTime() {
		return inviteTime;
	}

	public void setInviteTime(Date inviteTime) {
		this.inviteTime = inviteTime;
	}

	public String getWeixinName() {
		return weixinName;
	}

	public void setWeixinName(String weixinName) {
		this.weixinName = weixinName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}