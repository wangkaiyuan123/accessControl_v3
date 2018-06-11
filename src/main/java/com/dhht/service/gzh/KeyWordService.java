package com.dhht.service.gzh;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dhht.common.AccessResult;
import com.dhht.common.DaoUtil;
import com.dhht.common.UUIDUtil;
import com.dhht.dao.BaseDao;
import com.dhht.entity.weixin.KeyWord;


@Service
public class KeyWordService {
	
	@Resource
	private BaseDao<KeyWord> keyWordDao;
     
	/**
	 * 获取列表
	 * @return
	 */
	public List<KeyWord> getList(){
		String hql = DaoUtil.getFindPrefix(KeyWord.class)+" order by keyNo";
		return  keyWordDao.find(hql);
	}

	/**
	 * 
	 * @return  AccessResult
	 */
	public AccessResult saveAdd(KeyWord keyWord){
		KeyWord k = new KeyWord();
		k.setId(UUIDUtil.generate());
		k.setKeyNo(keyWord.getKeyNo());
		k.setReplyContent(keyWord.getReplyContent());
		keyWordDao.save(k);
		return new AccessResult(true,"新增成功");
	}
	
	/**
	 * 
	 * @param keyWord
	 * @return  AccessResult
	 */
	public AccessResult deleteKeyword(KeyWord keyWord){
		KeyWord k = keyWordDao.get(KeyWord.class, keyWord.getId());
		keyWordDao.delete(k);
		return new AccessResult(true,"删除成功");
	}
	
	public BaseDao<KeyWord> getKeyWordDao() {
		return keyWordDao;
	}

	public void setKeyWordDao(BaseDao<KeyWord> keyWordDao) {
		this.keyWordDao = keyWordDao;
	}
} 
