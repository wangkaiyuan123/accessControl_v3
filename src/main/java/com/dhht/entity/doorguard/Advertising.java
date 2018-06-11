package com.dhht.entity.doorguard;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "advertising")
public class Advertising implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@Column(nullable = false,length = 32, unique = true)
	private String id;
	
	//序号
	@Column(nullable = false,length = 11)
	private Integer sendSort;
	
	@Column(length = 60)
	private String content;
	
	@Transient
	private long planTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getSendSort() {
		return sendSort;
	}

	public void setSendSort(Integer sendSort) {
		this.sendSort = sendSort;
	}

	public long getPlanTime() {
		return planTime;
	}

	public void setPlanTime(long planTime) {
		this.planTime = planTime;
	}
	
}
