package com.dhht.entity.weixin;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 设备动态随机数，用于生成二维码
 * @author Administrator
 *
 */
@Entity
@Table(name = "dynamic_random")
public class DynamicRandom implements Serializable{
	private static final long serialVersionUID = 1L;
	/*
     * 主键id
     */
    @Id
    @Column(nullable = false, length = 32, unique = true)
    private String id;
    
    //设备MAC
    @Column(nullable = false, length = 12, unique = true)
    private String deviceMAC;
    
    //随机数--01.12改成算法动态密码
    @Column(nullable = false, length = 6)
    private String random1;
    
    //人员id--01.12改成人员id
    @Column(nullable = true, length = 32)
    private String random2;
    
    //最后时间：时效
 	@Column(nullable = false, length = 20)
 	private Long lastTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeviceMAC() {
		return deviceMAC;
	}

	public void setDeviceMAC(String deviceMAC) {
		this.deviceMAC = deviceMAC;
	}

	public String getRandom1() {
		return random1;
	}

	public void setRandom1(String random1) {
		this.random1 = random1;
	}

	public String getRandom2() {
		return random2;
	}

	public void setRandom2(String random2) {
		this.random2 = random2;
	}

	public Long getLastTime() {
		return lastTime;
	}

	public void setLastTime(Long lastTime) {
		this.lastTime = lastTime;
	}
	
}
