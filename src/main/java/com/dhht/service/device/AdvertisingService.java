package com.dhht.service.device;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhht.common.AccessResult;
import com.dhht.common.DaoUtil;
import com.dhht.common.UUIDUtil;
import com.dhht.dao.BaseDao;
import com.dhht.entity.doorguard.Advertising;

@Service
public class AdvertisingService {
	
	@Resource
	private BaseDao<Advertising> advertisingDao;
     
	//宣传语列表
	public List<Advertising> getList(int page,int rows){
		String hql = DaoUtil.getFindPrefix(Advertising.class)+" order by sendSort ";
		return advertisingDao.find(hql, page, rows);
	}
	
	//宣传语列表--不分页
	public List<Advertising> getAllList(){
		String hql = " from Advertising order by sendSort ";
		return advertisingDao.find(hql);
	}
	
    //新增广告
	public AccessResult saveAdd(Advertising advertising){
		Advertising ad = new Advertising();
		ad.setId(UUIDUtil.generate());
		ad.setSendSort(advertising.getSendSort());
		ad.setContent(advertising.getContent());
		advertisingDao.save(ad);
		return new AccessResult(true,"新增广告成功");
	}
	
	public AccessResult delete(Advertising advertising){
		Advertising ad =advertisingDao.get(Advertising.class, advertising.getId());
		advertisingDao.delete(ad);
		return new AccessResult(true,"删除成功");
	}
	
	public BaseDao<Advertising> getAdvertisingDao() {
		return advertisingDao;
	}

	public void setAdvertisingDao(BaseDao<Advertising> advertisingDao) {
		this.advertisingDao = advertisingDao;
	}
}
