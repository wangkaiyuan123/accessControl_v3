import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.dhht.dao.BaseDao;
import com.dhht.entity.doorguard.DeviceCard;
import com.dhht.entity.doorguard.DoorCard;
import com.dhht.entity.system.Person;
import com.dhht.service.card.CardService;
import com.dhht.service.device.DeviceService;
import com.dhht.service.person.PeopleService;


//修改数据库数据
public class Test6 {
	
    public static void main(String[] args) {
    	 ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    	 //CardService cardService = (CardService) ctx.getBean("cardService");
    	 //DeviceService deviceService =(DeviceService)ctx.getBean("deviceService");
    	 PeopleService peopleService = (PeopleService)ctx.getBean("peopleService");
    	 List<Person> list =  peopleService.getList();
    	 System.out.println(list.size());
    	 for(Person card : list){
    		 String cardNo = card.getCardId();
    		 if(StringUtils.isNotBlank(cardNo)){
    			 if(cardNo.length()==8){
    				 String first = Integer.toHexString(Integer.parseInt(cardNo.substring(0,3)));
    				 String second = Integer.toHexString(Integer.parseInt(cardNo.substring(3,8)));
    				 String zero = "";
    				 if(second.length()<4){
    					 for(int i =0 ;i<4-second.length();i++){
    						 zero+="0";
    					 }
    				 }
    				 second = zero + second;
    				 String cardNew = (first+second+"").toUpperCase();
    				 System.out.println(cardNew);
    				 card.setCardId(cardNew);
    				 peopleService.saveOrUpdate(card);
    			 }
    		 }
    	 }
//    	String data = "EFCBB0";
//    	String first = Integer.parseInt(data.substring(0, 2), 16)+"";
//    	String second = Integer.parseInt(data.substring(2, 6),16)+"";
//    	System.out.println(""+first+second);
    	
  
	}
}
