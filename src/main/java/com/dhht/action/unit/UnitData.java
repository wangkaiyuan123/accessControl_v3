package com.dhht.action.unit;

import java.util.List;
/**
 * 前端数据展示类
 */
import com.dhht.entity.unit.Unit;

public class UnitData {
	private int code = 0;
	private String msg = "";
	// 总条数
	private int count;
	private List<Unit> data;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<Unit> getData() {
		return data;
	}
	public void setData(List<Unit> data) {
		this.data = data;
	}
}
