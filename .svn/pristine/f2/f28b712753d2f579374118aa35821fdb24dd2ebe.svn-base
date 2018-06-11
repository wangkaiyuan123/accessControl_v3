package com.dhht.entity.weixin;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "poll_data")
public class PollData implements Serializable{
	private static final long serialVersionUID = 1L;
	/*
     * 主键id
     */
    @Id
    @Column(nullable = false, length = 32, unique = true)
    private String id;
    
	//关联的mac地址
	@Column(nullable = false,length = 12)
	private String deviceMac;
	
	//功能码
	@Column(nullable = false,length = 4)
	private String func;
	
	//数据包
	@Column(nullable = false)
	private String data;
	
	//异常或结果描述
	@Column(nullable = true)
	private String resultDec;
	
	//时间
	@Column(nullable = false,length = 20)
	private Long pollTime;
	
	//发送次数  默认0
    @Column(nullable = false, columnDefinition = "TINYINT(2) DEFAULT 0")
    private Integer sendCount;
    
	//0未处理  1成功   2失败
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Integer state;
    
	//0不在线   1在线
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private Integer online;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeviceMac() {
		return deviceMac;
	}

	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Long getPollTime() {
		return pollTime;
	}

	public void setPollTime(Long pollTime) {
		this.pollTime = pollTime;
	}

	public Integer getSendCount() {
		return sendCount;
	}

	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getFunc() {
		return func;
	}

	public void setFunc(String func) {
		this.func = func;
	}

	public Integer getOnline() {
		return online;
	}

	public void setOnline(Integer online) {
		this.online = online;
	}

	public String getResultDec() {
		return resultDec;
	}

	public void setResultDec(String resultDec) {
		this.resultDec = resultDec;
	}
}
