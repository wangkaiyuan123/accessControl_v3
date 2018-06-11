package com.dhht.entity.doorguard;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 *  设备门卡表
 */
@Entity
@Table(name = "doorcard")
public class DoorCard implements Serializable{

	private static final long serialVersionUID = -9112334538775109615L;
    
	// 主鍵
	@Id
	@Column(nullable = false, length = 32, unique = true)
	private String id;
	
	//卡号
	@Column(nullable = false, length = 32,unique = true)
	private String cardNo;
	
	//卡串码
	@Column(nullable = true, length = 20)
	private String cardNumber;
	
	
	//最近使用时间
	@Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date recentTime;
	
	//绑定人员Id
	@Column(nullable = true,length = 32)
	private String userId;
	
	//绑定人姓名
	@Transient
	private String name;
	
	
	//备注
	@Column(length = 100)
	private String comment;

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

	public Date getRecentTime() {
		return recentTime;
	}

	public void setRecentTime(Date recentTime) {
		this.recentTime = recentTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
