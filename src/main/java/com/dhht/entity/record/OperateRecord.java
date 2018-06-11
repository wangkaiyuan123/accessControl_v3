package com.dhht.entity.record;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 操作记录
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "operateRecord")
public class OperateRecord implements Serializable{
	/*
	 * 主键id
	 */
	@Id
	@Column(nullable = false, length = 32, unique = true)
	private String id;
	
	//操作类型
	@Column(nullable = false, columnDefinition = "TINYINT(2)")
	private Integer operateType;
	//操作名称
	@Column(nullable = false,length = 20)
	private String operateName;
	//操作人
	@Column(nullable = false,length = 20)
	private String operatorName;
	//操作内容
	@Column(nullable = false,columnDefinition = "TEXT")
	private String content;
	//操作结果
	@Column(nullable = false,length = 50)
	private String operationResult;
	//操作时间
	@Column
	private Date operateTime;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getOperateType() {
		return operateType;
	}

	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}

	public String getOperateName() {
		return operateName;
	}

	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOperationResult() {
		return operationResult;
	}

	public void setOperationResult(String operationResult) {
		this.operationResult = operationResult;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
}
