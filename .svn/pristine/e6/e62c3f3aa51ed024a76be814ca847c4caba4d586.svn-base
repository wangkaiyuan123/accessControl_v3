package com.dhht.service.record;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhht.common.DaoUtil;
import com.dhht.common.Dictionary;
import com.dhht.common.UUIDUtil;
import com.dhht.dao.BaseDao;
import com.dhht.entity.record.OperateRecord;
import com.dhht.entity.system.User;

@Service
public class OperateRecordService {
	
	@Resource
	private BaseDao<OperateRecord> operateRecordDao;
	
	/**
	 * 获取操作记录
	 * @param page
	 * @param rows
	 * @param user
	 * @return
	 */
	public List<OperateRecord> getRecords(int page,int rows,User user){
		String hql = DaoUtil.getFindPrefix(OperateRecord.class)+" order by operateTime desc";
		List<OperateRecord> records = operateRecordDao.find(hql, page, rows);
		return records;
	}
	
	public long getCount(){
		//operateRecordDao.count("select count(id) from operaterecord");
		long count = operateRecordDao.count("select count(id) from OperateRecord");  //OperateRecord是类名不是表名
		return count;
	}
	/**
	 * 根据类型保存操作记录
	 * @param user
	 * @param operateType
	 */
    public void saveLog(User user,int operateType,boolean flag,String content){
    	OperateRecord record = new OperateRecord();
    	record.setId(UUIDUtil.generate());
    	record.setOperateName(getNameByType(operateType));
    	record.setOperateType(operateType);
    	record.setOperatorName(user.getRealName());
    	record.setOperateTime(new Date());
    	if(flag){
    		record.setOperationResult("操作成功");
    	}else{
    		record.setOperationResult("操作失败");
    	}
    	record.setContent(content);
    	operateRecordDao.save(record);
    }
	
	public String getNameByType(int operateType){
		switch (operateType) {
		case 1: return Dictionary.GLY_OPERATION_1;
		case 2: return Dictionary.GLY_OPERATION_2;
		case 3: return Dictionary.GLY_OPERATION_3;
		case 4: return Dictionary.GLY_OPERATION_4;
		case 5: return Dictionary.GLY_OPERATION_5;
		case 6: return Dictionary.GLY_OPERATION_6;
		case 7: return Dictionary.GLY_OPERATION_7;
		case 8: return Dictionary.GLY_OPERATION_8;
		case 9: return Dictionary.GLY_OPERATION_9;
		case 10: return Dictionary.GLY_OPERATION_10;
		case 11: return Dictionary.GLY_OPERATION_11;
		case 12: return Dictionary.GLY_OPERATION_12;
		case 13: return Dictionary.GLY_OPERATION_13;
		case 14: return Dictionary.GLY_OPERATION_14;
		case 15: return Dictionary.GLY_OPERATION_15;
		case 16: return Dictionary.GLY_OPERATION_16;
		case 17: return Dictionary.GLY_OPERATION_17;
		default:
			return "";
		}
	}
}
