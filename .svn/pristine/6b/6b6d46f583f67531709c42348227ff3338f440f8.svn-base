package com.dhht.service.record;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.dhht.common.DaoUtil;
import com.dhht.dao.BaseDao;
import com.dhht.entity.record.OpenDoorRecord;
import com.dhht.entity.record.OperateRecord;
import com.dhht.entity.region.Region;
import com.dhht.entity.system.User;
import com.dhht.entity.template.newData;
import com.dhht.entity.visitors.VisitorRecord;
import com.dhht.service.device.DeviceService;
import com.dhht.service.region.RegionService;

@Service
public class VisitorOpenService {

	@Resource
	public BaseDao<VisitorRecord> visitorDao;
	@Resource
	public DeviceService deviceService;
	@Resource
	public RegionService regionService;
	
	
	public List<VisitorRecord>  getVisitorRecord(int page , int rows, User user){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(StringUtils.isBlank(user.getRegionId())){
			String sql = " select * from visitor_record where flag != 0 order by appointLongTime desc";
			List<VisitorRecord> records =  visitorDao.findBySql(sql, VisitorRecord.class, page, rows);
			for(VisitorRecord record :records){
				record.setAppointTime(format.format(new Date(record.getAppointLongTime())));
			}
			return records;
		}
		
		List<VisitorRecord> openDoorRecords = null;
		try {
			List<Region> regions =  regionService.getDevicesByRegions(user.getRegionId()); //包涵单元
			Set<String>  regionsIdSet = new HashSet<String>();
			for(Region region :regions){
				regionsIdSet.add(region.getId());
			}
			String regionsIdStr = DaoUtil.generateInStr(regionsIdSet);   //机构Id的集合包括小区
			String sql = "SELECT * FROM visitor_record WHERE mac IN (SELECT deviceMac FROM device WHERE unitId IN ("+regionsIdStr+")) and flag != 0 order by appointLongTime desc";
			openDoorRecords = visitorDao.findBySql(sql,VisitorRecord.class, page, rows);
			for(VisitorRecord record:openDoorRecords){
				record.setAppointTime(format.format(new Date(record.getAppointLongTime())));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return openDoorRecords;
	}
	
	public long getCount(User user){
		if(StringUtils.isBlank(user.getRegionId())){
			//String sql = " select * from visitor_record where flag != 0 order by appointLongTime desc";
			return visitorDao.countSQLQuery("select count(id) from visitor_record where flag != 0");
		}
		
		List<VisitorRecord> openDoorRecords = null;
		try {
			List<Region> regions =  regionService.getDevicesByRegions(user.getRegionId()); //包涵单元
			Set<String>  regionsIdSet = new HashSet<String>();
			for(Region region :regions){
				regionsIdSet.add(region.getId());
			}
			String regionsIdStr = DaoUtil.generateInStr(regionsIdSet);   //机构Id的集合包括小区
			String sql = "SELECT * FROM visitor_record WHERE mac IN (SELECT deviceMac FROM device WHERE unitId IN ("+regionsIdStr+")) and flag != 0 order by appointLongTime desc";
			openDoorRecords = visitorDao.findBySql(sql,VisitorRecord.class);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(openDoorRecords!=null){
			return openDoorRecords.size();
		}
		return 0;
	}
}
