import javax.annotation.Resource;

import com.dhht.action.BaseAction;
import com.dhht.dao.BaseDao;
import com.dhht.entity.system.SysRoleResource;


public class Test1 extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	public static void main(String[] args) {
//		System.out.println("http://mj.donghuahongtai.com/w/o?M=123456123456T=123456781234567812345678".length());
//		System.out.println("http://mj.donghuahongtai.com/weixinpage/ope?random=123456MAC=123456123456".length());
		for(int i=0;i<6;i++){
			System.out.println(i+",");
			while(++i<5){
				System.out.println("#"+i);
				continue;
			}
			System.out.println(i);
		}
	}

}