package com.dhht.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import com.dhht.client.MyProtocalCodecFactory;  //为了区分

//客户端
public class MinaClient {
	public static void main(String[] args) throws Exception{
		NioSocketConnector connector = new NioSocketConnector();
		connector.setHandler(new MyClientHandler());
		connector.getFilterChain().addLast("code",new ProtocolCodecFilter(new MyProtocalCodecFactory()));
		ConnectFuture future = connector.connect(new InetSocketAddress("127.0.0.1", 5557));
		future.awaitUninterruptibly();
		IoSession session = future.getSession();
		//http://weixin.qq.com/r/gyr17aLEEP9vreaO93_R?random=322424&machineId=D8B04CDEC6C6
		//http://weixin.qq.com/r/gyr17aLEEP9vreaO93_R?random=322424&machineId=D8B04CDEC6C6
		
		//硬件往服务器发送
		//D8 B0 4C DE C6 C1 55 AA 11 00 32   心跳       D8B04CDEC6B4
		byte[] heart = new byte[]{(byte)0xD8,(byte)0xB0,(byte)0x4C,(byte)0xDE,(byte)0xC6,(byte)0xCD,(byte)0x55,(byte)0xAA,(byte)0x11,(byte)0x00,(byte)0x32};  
		//D8 B0 4C DE C6 C1 55 AA 13 01 00 27  请求二维码
		byte[] getqcode = new byte[]{(byte)0xD8,(byte)0xB0,(byte)0x4C,(byte)0xDE,(byte)0xC6,(byte)0xC1,(byte)0x55,(byte)0xAA,(byte)0x13,(byte)0x01,(byte)0x00,(byte)0x27}; 
		//D8 B0 4C DE C6 C1 55 AA 13 01 01 2E  收到二维码应答
		byte[] backqcode = new byte[]{(byte)0xD8,(byte)0xB0,(byte)0x4C,(byte)0xDE,(byte)0xC6,(byte)0xC1,(byte)0x55,(byte)0xAA,(byte)0x13,(byte)0x01,(byte)0x01,(byte)0x2E}; 
		//D8 B0 4C DE C6 C1 55 AA 17 0B 14 11 0B 13 0B 01 3B 07 0C 0D 0E 3F   上传开门记录  8字节时间+3字节卡号
		byte[] openDoorRecord = new byte[]{(byte)0xD8,(byte)0xB0,(byte)0x4C,(byte)0xDE,(byte)0xC6,(byte)0xC1,(byte)0x55,(byte)0xAA,(byte)0x17,(byte)0x0B,
				(byte)0x14,(byte)0x11,(byte)0x0B,(byte)0x13,(byte)0x0B,(byte)0x01,(byte)0x3B,(byte)0x07,(byte)0x0C,(byte)0x0D,(byte)0x0E,(byte)0x65}; 
		//D8 B0 4C DE C6 C6 55 AA 1F 00 50   告知服务器效验失败
		byte[] FAIL = new byte[]{(byte)0xD8,(byte)0xB0,(byte)0x4C,(byte)0xDE,(byte)0xC6,(byte)0xC6,(byte)0x55,(byte)0xAA,(byte)0x1F,(byte)0x00,(byte)0x50};
		
		byte[] opendoor =  new byte[]{(byte)0xD8,(byte)0xB0,(byte)0x4C,(byte)0xDE,(byte)0xC6,(byte)0xCD,(byte)0x55,(byte)0xAA,(byte)0x12,(byte)0x01,(byte)0x00,(byte)0x08};
		
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in)); //这样可以直接从平台发送
		String inputContent;
		while (true) {
			inputContent = inputReader.readLine();
			if(inputContent.equals("1")){//心跳
				System.out.println(BinaryToHexString(heart));
				session.write(heart);  
			}else if(inputContent.equals("2")){//请求二维码
				System.out.println(BinaryToHexString(getqcode));
				session.write(getqcode);
			}else if(inputContent.equals("3")){//收到二维码应答
				System.out.println(BinaryToHexString(backqcode));
				session.write(backqcode);
			}else if(inputContent.equals("4")){//上传开门记录
				System.out.println(BinaryToHexString(openDoorRecord));
				session.write(openDoorRecord);
			}else if(inputContent.equals("5")){//告知服务器效验失败
				System.out.println(BinaryToHexString(FAIL));
				session.write(FAIL);
			}else if(inputContent.equals("6")){
				System.out.println(BinaryToHexString(opendoor));
				session.write(opendoor);
			}
		}

	}

    //将字节数组转换为16进制字符串  
    public static String BinaryToHexString(byte[] bytes) {  
        String hexStr = "0123456789ABCDEF";  
        String result = "";  
        String hex = "";  
        for (byte b : bytes) {  
            hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));  
            hex += String.valueOf(hexStr.charAt(b & 0x0F));  
            result += hex + "";  
        }  
        return result;  
    } 
}
