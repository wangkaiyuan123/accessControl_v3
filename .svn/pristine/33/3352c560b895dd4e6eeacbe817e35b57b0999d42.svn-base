package com.dhht.entity.weixin;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "weixin_user")
public class WeixinUser implements Serializable{
	private static final long serialVersionUID = 1L;
	/*
     * 主键id
     */
    @Id
    @Column(nullable = false, length = 32, unique = true)
    private String id;
	
	//非企业成员的标识，对当前企业号唯一 
    @Column(nullable = false, length = 100, unique = true)
	private String openid;
    
	//账号
    @Column
	private Integer account;
    
	//用户昵称 
    @Column(nullable = true, length = 50)
	private String nickname;
    
	//是否内部人员  0否  1是(审核通过)  2审核中        默认0
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Integer innerPerson;
    
	//性别 0保密  1男  2女
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Integer sex;
    
	//城市
    @Column(nullable = false, length = 20)
	private String city = "";
    
    //关注时间
 	@Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
 	private Date addtime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getInnerPerson() {
		return innerPerson;
	}

	public void setInnerPerson(Integer innerPerson) {
		this.innerPerson = innerPerson;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Integer getAccount() {
		return account;
	}

	public void setAccount(Integer account) {
		this.account = account;
	}

}
