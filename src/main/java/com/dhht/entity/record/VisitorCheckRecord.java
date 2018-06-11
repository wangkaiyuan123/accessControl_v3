package com.dhht.entity.record;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 访客审核记录表
 * @author Administrator
 *
 */
@Entity
@Table(name = "visitorcheckrecord")
public class VisitorCheckRecord implements Serializable{

	private static final long serialVersionUID = 1L;
	//主键
	@Id
	@Column(nullable = false,length = 32 ,unique = true)
	private String id;
	
	//预约访客姓名
	@Column(nullable = false,length = 10)
	private String visitorName;
	
	//预约访客电话
	@Column(nullable = false ,length = 11)
	private String visitorPhoneNumber;
     
	//受访人姓名
	@Column(nullable = false,length = 10)
	private String respondentName;
	
	//受访人电话
	@Column(nullable = false ,length = 11)
	private String respondentPhoneNumber;
	
	//同意人
	@Column(nullable = false,length = 10)
	private String agreePerson;
	
	//预约时间
	@Column
	private Date appointTime ;
	
	//门禁地址
	@Column(length = 50 )
	private String address;
	
	//状态(0:未同意 1：已同意)
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private Integer state;

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

	public String getAgreePerson() {
		return agreePerson;
	}

	public void setAgreePerson(String agreePerson) {
		this.agreePerson = agreePerson;
	}

	public Date getAppointTime() {
		return appointTime;
	}

	public void setAppointTime(Date appointTime) {
		this.appointTime = appointTime;
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
	
	
}
