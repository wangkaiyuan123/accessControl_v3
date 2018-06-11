package com.dhht.common;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QrcodeUtil {
	public static void main(String[] args) {
		createQrcode("HTTP123456","1111112222");
	}
	public static void createQrcode(String url,String id){
		try {
			   // http://mj.donghuahongtai.com/weixinpage/approvalVisitor?token=fc3a697c720d4eec88b730986f345fad  record的id
	     	   Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();  //
	            hints.put(EncodeHintType.MARGIN, 0);
	            BitMatrix bitMatrix = new QRCodeWriter().encode(url,BarcodeFormat.QR_CODE, 256, 256,hints);
	            int width = bitMatrix.getWidth();
	            int height = bitMatrix.getHeight();
	            BufferedImage image = new BufferedImage(width, height,BufferedImage.TYPE_INT_ARGB);
	            for (int x = 0; x < width; x++) {
	                 for (int y = 0; y < height; y++) {
	                       image.setRGB(x, y, bitMatrix.get(x, y) == true ? 
	                       Color.BLACK.getRGB():Color.WHITE.getRGB());
	                }
	            }
	            File dir = new File("C:/dhht/qrcodes");  //
	        	if(!dir.exists()){
	        		dir.mkdirs();   //创建个文件夹
	        	}
	            File outputFile = new File("C:/dhht/qrcodes/"+id+".png");   //二维码图片    "dhht/qrcodes/"+uid+".png";
	            ImageIO.write(image,"png", outputFile);   //将二维码图片以流的形式写到对应的文件中。
			} catch (Exception e) {
				
			}
	}
}
