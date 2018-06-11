package com.dhht.service.gzh;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhht.common.AccessResult;
import com.dhht.common.DaoUtil;
import com.dhht.common.UUIDUtil;
import com.dhht.dao.BaseDao;
import com.dhht.entity.weixin.WeixinDictionary;

@Service
public class GzhService {
    
	@Resource
	private BaseDao<WeixinDictionary> weixinDictionaryDao;
	
	/**
	 * 获取公众号配置信息
	 * @return
	 */
	public List<WeixinDictionary> getList(){
		String hql = DaoUtil.getFindPrefix(WeixinDictionary.class)+" where state = 0 and type = 0 order by time";
		return weixinDictionaryDao.find(hql);
	}

	public AccessResult saveAdd(WeixinDictionary weixinDictionary){
		WeixinDictionary w = new WeixinDictionary();
		w.setId(UUIDUtil.generate());
		w.setName(weixinDictionary.getName());
		w.setState(0);
		w.setType(0);
		w.setValue(weixinDictionary.getValue());
		w.setWeixinkey(weixinDictionary.getWeixinkey());
		weixinDictionaryDao.save(w);
		return new AccessResult(false,"新增成功");
	}
	
	
	public AccessResult deleteMsg(WeixinDictionary weixinDictionary){
		WeixinDictionary w = weixinDictionaryDao.get(WeixinDictionary.class, weixinDictionary.getId());
		weixinDictionaryDao.delete(w);
		return new AccessResult(true,"删除成功");
	}
	
	
	public AccessResult updateMsg(WeixinDictionary weixinDictionary){
		WeixinDictionary w = weixinDictionaryDao.get(WeixinDictionary.class, weixinDictionary.getId());
		w.setName(weixinDictionary.getName());
		w.setValue(weixinDictionary.getValue());
		w.setWeixinkey(weixinDictionary.getWeixinkey());
		weixinDictionaryDao.saveOrUpdate(w);
	    return new AccessResult(true,"修改成功");
	}
	
	public BaseDao<WeixinDictionary> getWeixinDictionaryDao() {
		return weixinDictionaryDao;
	}

	public void setWeixinDictionaryDao(BaseDao<WeixinDictionary> weixinDictionaryDao) {
		this.weixinDictionaryDao = weixinDictionaryDao;
	}
	
	
}
