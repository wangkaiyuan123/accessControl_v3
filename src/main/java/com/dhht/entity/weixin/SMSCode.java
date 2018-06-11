package com.dhht.entity.weixin;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sms_code")
public class SMSCode implements Serializable{
	private static final long serialVersionUID = 1L;
	/*
     * 主键id
     */
    @Id
    @Column(nullable = false, length = 32, unique = true)
    private String id;
    
    //手机号 唯一
    @Column(nullable = false, length = 11, unique = true)
    private String phone;
    
    //短信验证码  6位数字
    @Column(nullable = false, length = 6)
    private String smscode;
    
    //最后保存的时间
    @Column(nullable = false, length = 20)
    private Long lastTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSmscode() {
		return smscode;
	}

	public void setSmscode(String smscode) {
		this.smscode = smscode;
	}

	public Long getLastTime() {
		return lastTime;
	}

	public void setLastTime(Long lastTime) {
		this.lastTime = lastTime;
	}
    
}
