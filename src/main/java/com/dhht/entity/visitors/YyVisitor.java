package com.dhht.entity.visitors;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "yy_visitor")
public class YyVisitor implements Serializable{

	private static final long serialVersionUID = 1L;
    
	//主键
	@Id
	@Column(nullable = false,length = 32 ,unique = true)
	private String id;
	
	//访客姓名
	@Column(nullable = false,length = 10)
	private String visitorName;
	
	//访客电话
	@Column(nullable = false,length = 11)
	private String visitorPhoneNumber;
	
	//受访人姓名
	@Column(nullable = false,length = 10)
	private String respondentName;
	
	//受访人电话
	@Column(nullable = false,length = 11)
	private String respondentPhoneNumber;
	
	//微信昵称(预约访客的微信)
	@Column(nullable = false,length = 15)
	private String weixinName;
	
	//预约时间
	@Column(nullable = false)
	private Date appointDate;
	
	//门禁地址
	@Column(nullable = false,length = 50)
	private String address;
	
	//状态  0:不同意  1 :已同意  2 :未审核
	@Column(length =1)
	private Integer state;
	
	//是否已审核(0 ：表示未审核  1：表示已审核)
	@Column(length = 1)
	private Integer isChecked;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVisitorName() {
		return visitorName;
	}

	public void setVisitorName(String visitorName) {
		this.visitorName = visitorName;
	}

	public String getVisitorPhoneNumber() {
		return visitorPhoneNumber;
	}

	public void setVisitorPhoneNumber(String visitorPhoneNumber) {
		this.visitorPhoneNumber = visitorPhoneNumber;
	}

	public String getRespondentName() {
		return respondentName;
	}

	public void setRespondentName(String respondentName) {
		this.respondentName = respondentName;
	}

	public String getRespondentPhoneNumber() {
		return respondentPhoneNumber;
	}

	public void setRespondentPhoneNumber(String respondentPhoneNumber) {
		this.respondentPhoneNumber = respondentPhoneNumber;
	}

	public String getWeixinName() {
		return weixinName;
	}

	public void setWeixinName(String weixinName) {
		this.weixinName = weixinName;
	}

	public Date getAppointDate() {
		return appointDate;
	}

	public void setAppointDate(Date appointDate) {
		this.appointDate = appointDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(Integer isChecked) {
		this.isChecked = isChecked;
	}
	
   
}
