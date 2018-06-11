package com.dhht.entity.weixin;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "openDoorFlag")
public class OpenDoorFlag {
	private static final long serialVersionUID = 1L;
	/*
     * 主键id
     */
    @Id
    @Column(nullable = false, length = 32, unique = true)
    private String id;
    
    //0初始值(未处理)    1开门成功   2开门失败
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Integer flag;
    
    //添加时间
 	@Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
 	private Date addtime;
 	
	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
    
}
