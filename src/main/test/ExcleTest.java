import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;

import com.dhht.common.DaoUtil;
import com.dhht.dao.BaseDao;
import com.dhht.entity.record.OpenDoorRecord;
@Service
public class ExcleTest {
	
	@Resource
	private BaseDao<OpenDoorRecord> openDoorRecordDao;
	
	public void exportExcelC(HttpServletRequest request, HttpServletResponse response, 
			  String year, String month, String depot) throws Exception {
	
		String title = year+"年"+ month+"月记录";
		String fileName = title + ".xls"; // 文件名
		// 通过Response把数据以Excel格式保存
		try {
			HSSFWorkbook hss = exportExcelS(request, response, year, month, depot);
			
			response.reset();
			response.setContentType("application/msexcel;charset=UTF-8");
			response.addHeader("Content-Disposition",
					"attachment;filename=\"" + new String((fileName).getBytes("GBK"), "ISO8859_1") + "\"");
			OutputStream out = response.getOutputStream();
			hss.write(out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public HSSFWorkbook  exportExcelS(HttpServletRequest request, HttpServletResponse response,
			String year, String month,String depot) throws Exception{
		
		//	JgDepotVouchers
		String startDate = year + "-" + month + "-1";
		String endDate = year + "-" + (Integer.parseInt(month) + 1) + "-1";
		String hql = DaoUtil.getFindPrefix(OpenDoorRecord.class)+" order by openDate desc";
		List<OpenDoorRecord> list = openDoorRecordDao.find(hql, 1, 20);
				
		
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook hhs = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = hhs.createSheet("sheet");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row = sheet.createRow((int) 0);
		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = hhs.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 左对齐
		HSSFCell cell =null;
		HSSFRichTextString text=null;
		//String[] titles={"单据编号","单据状态","所属仓库","单据来源","申请人","操作人","单据类型","审核人","出库日期","创建日期","单据备注"};
		String[] titles={"记录编号","开门地址","设备号","开门时间","开门人","开门类型"};
		row=sheet.createRow(0);
		for(int i=0;i<titles.length;i++){
			if(i==0 ||i==2||i==8||i==9){
				sheet.setColumnWidth(i, 20 * 256);  
			}else if(i==10){
				sheet.setColumnWidth(i, 35 * 256);  
			}else{
				sheet.setColumnWidth(i, 10 * 256);  
			}
			cell=row.createCell(i);
			cell.setCellStyle(style);
			text=new HSSFRichTextString(titles[i]);
			cell.setCellValue(text);
		}
	
	    SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int index=0;
		for(OpenDoorRecord record:list){
			String state = "";
			String source = "";
			switch (record.getOpenType()) {
			case 1:
				state = "门卡开门";
				break;
			case 2:
				state = "微信扫码开门";
				break;
			default:
				break;
			}
			
			String[] titles2={record.getId(),record.getAddress(),record.getDeviceMac(),
					sdf.format(record.getOpenDate()),record.getOpenDoorPerson(),state};
			row = sheet.createRow(index + 1);
			for(int i=0;i<titles.length;i++){
				cell=row.createCell(i);
				cell.setCellStyle(style);
				text=new HSSFRichTextString(titles2[i]);
				cell.setCellValue(text);
			}
			index++;
		}
		return hhs;
	}
}
