package com.dhht.entity.weixin;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "weixin_dictionary")
public class WeixinDictionary implements Serializable{
       
	//主键
	@Id
	@Column(nullable = false,length = 32, unique = true)
	private String id;
	
	//键
	@Column(nullable = false,length = 32, unique = true)//16不够，需改成32？
	private String weixinkey;
	
	//值
	@Column(nullable = false,length = 200)
	private String value;
	
	//配置名
	@Column(nullable = false, unique = true)
	private String name;
	
	//0能显示，1不能显示
	@Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
	private Integer state;
	
	//0公众号配置，1模板消息id
	@Column(nullable = false,columnDefinition = "TINYINT(1) DEFAULT 0")
	private Integer type;
	
	@Column(nullable = false)
	private Date time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWeixinkey() {
		return weixinkey;
	}

	public void setWeixinkey(String weixinkey) {
		this.weixinkey = weixinkey;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
	
}
