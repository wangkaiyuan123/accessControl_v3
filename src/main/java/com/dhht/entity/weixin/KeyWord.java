package com.dhht.entity.weixin;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "keyword")
public class KeyWord implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// 主键
	@Id
	@Column(nullable = false, length = 32, unique = true)
	private String id;
	
	@Column(nullable = false,length = 10)
	private String keyNo;
	
	@Column(length = 100)
	private String replyContent;

	@Column(nullable = true,length = 200)
	private String remark;//备注
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKeyNo() {
		return keyNo;
	}

	public void setKeyNo(String keyNo) {
		this.keyNo = keyNo;
	}

	public String getReplyContent() {
		return replyContent;
	}

	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
