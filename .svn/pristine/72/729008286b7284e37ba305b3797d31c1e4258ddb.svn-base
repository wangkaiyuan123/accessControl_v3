package com.dhht.client;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * 自定义实体类解码工具    服务器-->客户端会进入到解码器
 */
public class MyProtocalDecoder extends CumulativeProtocolDecoder {
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		try {
			//System.out.println("进去解码器···");
			int pos = in.position();
			byte[] by = new byte[in.limit()];
			for(int i = pos;i<in.limit();i++){
				byte b = in.get(i);
				by[i]=b;
			}
			String msgStr = BinaryToHexString(by); 
			System.out.println("###客户端接收："+msgStr);
			out.write(by);
		} catch (Exception e) {
			System.out.println(session.toString()+"catch"+e.getMessage());
			return false;
		}finally{
			in.position(in.limit());//指针后移
		}
		return false;
	}
	
	
	
    //将字节数组转换为16进制字符串  
    public static String BinaryToHexString(byte[] bytes) { 
        String hexStr = "0123456789ABCDEF";  
        String result = "";  
        String hex = "";  
        for (byte b : bytes) {  
            hex = String.valueOf(hexStr.charAt((b & 0xF0) >> 4));  
            hex += String.valueOf(hexStr.charAt(b & 0x0F));  
            result += hex;  
        }  
        return result;  
    } 

}
