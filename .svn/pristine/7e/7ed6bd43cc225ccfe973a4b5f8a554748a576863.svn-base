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
public class MessageService {
     
	@Resource
	private BaseDao<WeixinDictionary> weiDao;
	
	//获取模板消息列表
	public List<WeixinDictionary> getList(){
		String hql = DaoUtil.getFindPrefix(WeixinDictionary.class)+" where type = 1";
		return weiDao.find(hql);
	}
	
	//
	public AccessResult saveAdd(WeixinDictionary weixinDictionary){
		WeixinDictionary w = new WeixinDictionary();
		w.setId(UUIDUtil.generate());
		w.setWeixinkey(weixinDictionary.getWeixinkey());
		w.setName(weixinDictionary.getName());
		w.setState(0);
		w.setType(1);
		w.setValue(weixinDictionary.getValue());
		weiDao.save(w);
		return new AccessResult(true,"新增成功");
	}
	
	
	public AccessResult deleteMessage(WeixinDictionary weixinDictionary){
		WeixinDictionary w = weiDao.get(WeixinDictionary.class, weixinDictionary.getId());
		weiDao.delete(w);
		return new AccessResult(true,"删除成功");
	}
	
	public AccessResult updateMessage(WeixinDictionary weixinDictionary){
		WeixinDictionary w = weiDao.get(WeixinDictionary.class, weixinDictionary.getId());
		w.setWeixinkey(weixinDictionary.getWeixinkey());
		w.setName(weixinDictionary.getName());
		w.setValue(weixinDictionary.getValue());
		weiDao.saveOrUpdate(w);
		return new AccessResult(false,"修改成功");
	}
}
