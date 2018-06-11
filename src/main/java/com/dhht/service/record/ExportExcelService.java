package com.dhht.service.record;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.stereotype.Service;

import com.dhht.common.DaoUtil;
import com.dhht.common.DateUtils;
import com.dhht.common.UploadFileUtils;
import com.dhht.dao.BaseDao;
import com.dhht.entity.record.OpenDoorRecord;
import com.dhht.entity.region.Region;
import com.dhht.entity.system.User;
import com.dhht.service.region.RegionService;

@Service
public class ExportExcelService {
	
	@Resource
	private BaseDao<OpenDoorRecord> openDoorRecordDao;
	@Resource
	private RegionService regionService; 

	public void exportExcel(String[] headers, Collection<OpenDoorRecord> dataset,
			OutputStream out) {
		exportExcel("开门记录", headers, dataset, out, "yyyy-MM-dd HH:mm:ss");
	}

	
	
	/**
	 * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上
	 * 
	 * @param title
	 *            表格标题名
	 * @param headers
	 *            表格属性列名数组
	 * @param dataset
	 *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
	 *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
	 * @param out
	 *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
	 * @param pattern
	 *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
	 */
	@SuppressWarnings("unchecked")
	public void exportExcel(String title, String[] headers,
			Collection<OpenDoorRecord> dataset, OutputStream out, String pattern) {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setFillForegroundColor(HSSFColor.WHITE.index);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		font.setFontHeightInPoints((short) 12);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setFillForegroundColor(HSSFColor.WHITE.index);
		style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
		style2.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setColor(HSSFColor.BLACK.index);
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
		// 把字体应用到当前的样式
		style2.setFont(font2);

		// 声明一个画图的顶级管理器
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		// 定义注释的大小和位置,详见文档
		HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
				0, 0, 0, (short) 4, 2, (short) 6, 5));
		// 设置注释内容
		//comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
		// 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
		//comment.setAuthor("leno");

		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}

		// 遍历集合数据，产生数据行
		Iterator<OpenDoorRecord> it = dataset.iterator();
		int index = 0;
		while (it.hasNext()) {
			index++;
			row = sheet.createRow(index);
			OpenDoorRecord t = it.next();
			// 利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
			Field[] fields = t.getClass().getDeclaredFields();  //获取列数
			for (short i = 0; i < fields.length-2; i++) {   
				HSSFCell cell = row.createCell(i);
				cell.setCellStyle(style2);
				Field field = fields[i];
				String fieldName = field.getName();
				String getMethodName = "get"
						+ fieldName.substring(0, 1).toUpperCase()
						+ fieldName.substring(1);
				try {
					Class tCls = t.getClass();
					Method getMethod = tCls.getMethod(getMethodName,
							new Class[] {});
					Object value = getMethod.invoke(t, new Object[] {});
					// 判断值的类型后进行强制类型转换
					String textValue = "";
					if(value!=null){
						if (value instanceof Integer) {
							int bValue = (Integer) value;
							if (bValue==1) {
								textValue = "门卡开门";
							}
							if(bValue==2){
								textValue = "微信开门";
							}
							if(bValue==3){
								textValue = "密码开门";
							}
						} else if (value instanceof Date) {
							Date date = (Date) value;
							SimpleDateFormat sdf = new SimpleDateFormat(pattern);
							textValue = sdf.format(date);
						} else if (value instanceof byte[]) {
							// 有图片时，设置行高为60px;
							row.setHeightInPoints(60);
							// 设置图片所在列宽度为80px,注意这里单位的一个换算
							sheet.setColumnWidth(i, (short) (35.7 * 80));
							// sheet.autoSizeColumn(i);
							byte[] bsValue = (byte[]) value;
							HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,
									1023, 255, (short) 6, index, (short) 6, index);
							anchor.setAnchorType(2);
							patriarch.createPicture(anchor, workbook.addPicture(
									bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
						} else {
							// 其它数据类型都当作字符串简单处理
							textValue = value.toString();
						}
						// 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
						if (textValue != null) {
							Pattern p = Pattern.compile("^//d+(//.//d+)?$");
							Matcher matcher = p.matcher(textValue);
							if (matcher.matches()) {
								// 是数字当作double处理
								cell.setCellValue(Double.parseDouble(textValue));
							} else {
								HSSFRichTextString richString = new HSSFRichTextString(
										textValue);
//								HSSFFont font3 = workbook.createFont();
//								font3.setColor(HSSFColor.BLACK.index);
//								richString.applyFont(font3);
								cell.setCellValue(richString);
							}
						}
					}
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} finally {
					// 清理资源
				}
			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//先下载到服务器 C:/dhht/XXX.xls
	public void doExport(HttpServletResponse response,User user,String time) {
		
		List<Region> regions = regionService.getDevicesByRegions(user.getRegionId()); // 包涵单元
		Set<String> regionsIdSet = new HashSet<String>();
		for (Region region : regions) {
			regionsIdSet.add(region.getId());
		}
		String regionsIdStr = DaoUtil.generateInStr(regionsIdSet); // 机构Id的集合包括小区
		try {
			// 前端传过来的时间
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
			// 月初时间
			Date startDate = sdf1.parse(time);
			// 下月月初时间
			Date endDate = DateUtils.getRelativeMonthOfAppointedDate(startDate,1);
			//String hql = DaoUtil.getFindPrefix(OpenDoorRecord.class)+ " where openDate >= :startDate and openDate <= :endDate order by openDate desc";
			String hql = DaoUtil.getFindPrefix(OpenDoorRecord.class)+ " where deviceMac IN (SELECT deviceMac FROM Device WHERE unitId IN ("+regionsIdStr+
					  ")) and openDate >= :startDate and openDate <= :endDate order by openDate desc";
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("startDate", startDate);
			paramMap.put("endDate", endDate);
			List<OpenDoorRecord> records = openDoorRecordDao.find(hql,paramMap);

			String[] headers = { "记录编号", "人员编号", "开门方式", "地址", "设备号", "开门时间",
					"开门人" };

			OutputStream out = new FileOutputStream("C:/dhht/" + user.getId()+ ".xls");
			// 先导入到excel
			exportExcel(headers, (Collection<OpenDoorRecord>) records, out);
			out.close();
			System.out.println("excel导出成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void exportExcelC(User user,HttpServletRequest request,HttpServletResponse response, String time) {
		//先导入到excel文件中
		doExport(response,user,time);
	}

	//在从服务器下载 /dhht/XXX.xls ---> C:/dhht/XXX.xls 下载   两种方法实现。
	public void download(String path, HttpServletResponse response,User user) {
//		try {
//			//这里
//			response.setCharacterEncoding("UTF-8");
//			//静态资源路径
//			response.sendRedirect("/dhht/"+user.getId()+".xls");  //这里实现的就是浏览器选择对应的路劲来下载的实现,这里需要在tomcat里配置虚拟路径。
//		} catch (Exception e) {}
		try {  
            // path是指欲下载的文件的路径。  '
			String urlPath = "C:/dhht/"+user.getId()+".xls";  //这样就不需要配置静态路径
            File file = new File(urlPath);  
            // 取得文件名。  
            String filename = file.getName();
            // 以流的形式下载文件。  
            InputStream fis = new BufferedInputStream(new FileInputStream(urlPath));  
            byte[] buffer = new byte[fis.available()];  
            fis.read(buffer);  
            fis.close();  
            // 清空response  
            response.reset();  
            // 设置response的Header  
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes()));  
            response.addHeader("Content-Length", "" + file.length());  
            OutputStream toClient = new BufferedOutputStream( response.getOutputStream());  
            response.setContentType("application/vnd.ms-excel;charset=gb2312");  
            toClient.write(buffer);  
            toClient.flush();  
            toClient.close();  
        } catch (IOException ex) {  
            ex.printStackTrace();  
        } 
    }



	public BaseDao<OpenDoorRecord> getOpenDoorRecordDao() {
		return openDoorRecordDao;
	}

	public void setOpenDoorRecordDao(BaseDao<OpenDoorRecord> openDoorRecordDao) {
		this.openDoorRecordDao = openDoorRecordDao;
	}  
	
}
