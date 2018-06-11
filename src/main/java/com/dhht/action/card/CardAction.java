package com.dhht.action.card;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;

import com.dhht.action.BaseAction;
import com.dhht.action.person.PersonData;
import com.dhht.common.AccessResult;
import com.dhht.entity.doorguard.DoorCard;
import com.dhht.service.card.CardService;
import com.dhht.service.person.PeopleService;
import com.opensymphony.xwork2.ModelDriven;
import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 门卡管理模块
 * @author Administrator
 *
 */
@SuppressWarnings("serial")  //每个action有个默认的拦截器，在struts.xml文件中配置
@Namespace("/card")
public class CardAction extends BaseAction implements ModelDriven<DoorCard>{
	
	private static final Logger logger = Logger.getLogger(DoorCard.class);
    private DoorCard doorCard = new DoorCard();
    
    private String regionId;
    private int page;
	private int rows;
	//根据姓名或身份证搜索
	private String personName; 
	private String identifyId;
	//绑定人的Id
	private String personId;
    @Resource
    private CardService cardService;
    @Resource
    private PeopleService peopleService;
	@Override
	public DoorCard getModel() {
		return doorCard;
	}
	
	@Action("list")
	public String list(){
		return SUCCESS;
	}
	
	@Action("add")
	public String  add(){
		return SUCCESS;
	}
	
	@Action("delete")
	public String delete(){
		return SUCCESS;
	}
	
	@Action("person")
	public String person(){
		return SUCCESS;
	}

	//门卡列表
	@Action("getCardList")
	public void getCardList(){
		CardData data = new CardData();
		data.setData(cardService.getCardList(doorCard, page, rows,getLoginUser()));
		data.setCount(cardService.getCount(doorCard,getLoginUser()));
		writeJson(data);
	}
	
	//获取未绑定卡的人员
	@Action("getPersonList")
	public void getPersonList(){
		PersonData data = new PersonData();
		data.setData(peopleService.getOnbandPersons(personName,identifyId, page, rows));
		data.setCount(peopleService.getOnbandPersonsCount(personName, identifyId));
		writeJson(data);
	}
	
	//组织架构
	@Action("getTrees")
	public void getTrees(){
		writeJson(cardService.getTrees(getLoginUser()));
	}
	
	@Action("saveAdd")
	public void saveAdd(){
		try {
			writeJson(cardService.saveAdd(doorCard));
		} catch (Exception e) {
			logger.error("新增门卡失败", e);
			writeJson(new AccessResult(false,"新增门卡失败"));
		}
	}
	
	//删除卡号
	@Action("deleteCard")
	public void deleteCard(){
		try {
			writeJson(cardService);
		} catch (Exception e) {
			logger.error("新增人员失败", e);
			writeJson(new AccessResult(false,"新增人员失败"));
		}
	}
	
	//绑定人员
	@Action("saveBindPerson")
	public void bindPerson(){
		try {
			writeJson(cardService.saveBindPerson(personId,doorCard));
		} catch (Exception e) {
			logger.error("绑定人员失败", e);
			writeJson(new AccessResult(false,"绑定人员失败"));
		}
	}

	public CardService getCardService() {
		return cardService;
	}

	public void setCardService(CardService cardService) {
		this.cardService = cardService;
	}

	public PeopleService getPeopleService() {
		return peopleService;
	}

	public void setPeopleService(PeopleService peopleService) {
		this.peopleService = peopleService;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getIdentifyId() {
		return identifyId;
	}

	public void setIdentifyId(String identifyId) {
		this.identifyId = identifyId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}
}
