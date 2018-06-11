import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import com.dhht.common.UUIDUtil;
//import com.google.zxing.EncodeHintType;
//import com.google.zxing.MultiFormatWriter;
//import com.google.zxing.BarcodeFormat;
//import com.google.zxing.common.BitMatrix;
//import com.google.zxing.qrcode.QRCodeWriter;
public class Test {
      public static void main(String[] args) {
//    	  int a = Integer.parseInt("EE", 16);
//    	  int b = Integer.parseInt("BA30", 16);
//    	  System.out.println(a);
//    	  System.out.println(b);
//    	  
//    	  String first = "1";
//    	  String completion ="";
//    	  if(first.length()<5){
//				for(int i=1;i<=5-first.length();i++){
//					completion += "0";
//				}
//			}
//    	  System.out.println(first+completion);
  		String hexStr = "";
  		String first = "";
  		first = Integer.toHexString(Integer.parseInt("24004048".substring(0, 3)));
  		System.out.println(first);
  		String completion1 ="";
  		if(first.length()<2){
			for(int i=1;i<=2-first.length();i++){
				completion1 += "0";
			}
			first = completion1+first;
		}
  		String second = "";
  		second = Integer.toHexString(Integer.parseInt("24004048".substring(3, 8)));
  		System.out.println(second);
  		String completion2 ="";
  		if(second.length()<4){
			for(int i=1;i<=4-second.length();i++){
				completion2 += "0";
			}
			second = completion2+second;
		}
  		hexStr = first+second;
  		System.out.println(hexStr);
    	  
//    	  long now = System.currentTimeMillis();
//    	  System.out.println(now);
//    	  String str =Long.toHexString(now).toUpperCase();
//    	  System.out.println(str.length());
//    	  int length = str.length();
//    	  if(str.length()<16){
//    		  for(int i=1;i<=16-length;i++){
//    			  str = "0"+str;
//    		  }
//    	  }
//    	  System.out.println(str);
    	  
    	  
//    	  String text = "你好";   
//          int width = 100;   
//          int height = 100;   
//          String format = "png";   
//          Hashtable<EncodeHintType, String> hints= new Hashtable<EncodeHintType, String>();   
//          hints.put(EncodeHintType.CHARACTER_SET, "utf-8");   
//           BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height,hints);   
//           File outputFile = new File("D:new.png");   
//           MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile); 
           
           /*try {
        	   Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
               hints.put(EncodeHintType.MARGIN, 0);
               BitMatrix bitMatrix = new QRCodeWriter().encode("http://weixin.qq.com/r/Yi8LE7nE9OSLrf1w93pw?random=322424machineId=D8B04CDEC6CE",BarcodeFormat.QR_CODE, 256, 256,hints);
               int width = bitMatrix.getWidth();
               int height = bitMatrix.getHeight();
               BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
               for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                          image.setRGB(x, y, bitMatrix.get(x, y) == true ? 
                          Color.BLACK.getRGB():Color.WHITE.getRGB());
                   }
               }
               File outputFile = new File("D:/productManage/111.png");
               ImageIO.write(image,"png", outputFile);
		} catch (Exception e) {
			// TODO: handle exception
		}*/
    	  
//    	  System.out.println("随机数为" + getRandNum(1,999999));args
//    	  String code = "";
//			for(int i=1;i<=6;i++){
//				int d=(int)(Math.random()*9+1);
//				code+=d;
//			}
//			System.out.println(code);
           
}
	  public static int getRandNum(int min, int max) {
		    int randNum = min + (int)(Math.random() * ((max - min) + 1));
		    return randNum;
		}      

}