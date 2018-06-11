package com.dhht.entity.visitors;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


/**
 * 访客预约记录表
 * @author Administrator
 *
 */
@Entity
@Table(name = "visitor_record")
public class VisitorRecord implements Serializable{
private static final long serialVersionUID = 1L;
    
	//主键
	@Id
	@Column(nullable = false,length = 32 ,unique = true)
	private String id;
	
	//申请人（访客）的openId
	@Column(nullable = false,length = 100)
	private String visitorOpenId;
	
	//申请人（访客）的personId
	@Column(nullable = false,length = 32)
	private String visitorPersonId;
	
	//申请人（访客）的姓名
	@Column(nullable = false,length = 10)
	private String visitorName;
	
	//门禁mac
	@Column(nullable = false,length = 12)
	private String mac;
	
	//门禁关联的单元id
	@Column(nullable = false,length = 32)
	private String unitId;
	
	//此次申请算出来的密码
	@Column(nullable = false,length = 6)
	private String secret;
	
	//审批人（业主）的personId
	@Column(nullable = true,length = 32)
	private String approverPersonId;
	
	//审批人姓名
	@Column(nullable = true,length = 10)
	private String approverName;
	
	//申请时间，毫秒
	@Column(nullable = false,length = 20)
	private Long appointLongTime;
	
	//状态 0未审批    1开门成功    2开门失败  3同意授权   4拒绝授权 
	@Column(nullable = false,length = 11)
	private Integer flag;
	
	//开门时间
	@Column
	private Date openDate;
	
	@Transient  //仅作为前端展示
	private String appointTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVisitorOpenId() {
		return visitorOpenId;
	}

	public void setVisitorOpenId(String visitorOpenId) {
		this.visitorOpenId = visitorOpenId;
	}

	public String getVisitorPersonId() {
		return visitorPersonId;
	}

	public void setVisitorPersonId(String visitorPersonId) {
		this.visitorPersonId = visitorPersonId;
	}

	public String getVisitorName() {
		return visitorName;
	}

	public void setVisitorName(String visitorName) {
		this.visitorName = visitorName;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getApproverPersonId() {
		return approverPersonId;
	}

	public void setApproverPersonId(String approverPersonId) {
		this.approverPersonId = approverPersonId;
	}

	public String getApproverName() {
		return approverName;
	}

	public void setApproverName(String approverName) {
		this.approverName = approverName;
	}

	public Long getAppointLongTime() {
		return appointLongTime;
	}

	public void setAppointLongTime(Long appointLongTime) {
		this.appointLongTime = appointLongTime;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public String getAppointTime() {
		return appointTime;
	}

	public void setAppointTime(String appointTime) {
		this.appointTime = appointTime;
	}
}
