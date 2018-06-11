package com.dhht.client;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * 自定义编码工具      客户端-->服务端会进行编码
 */
public class MyProtocalEncoder extends ProtocolEncoderAdapter {  
	public void encode(IoSession session, Object message,ProtocolEncoderOutput out) throws Exception {
		try {
			//System.out.println("客户端进入编码器");
			byte[] b = (byte[])message;
			int sum =b.length;
			IoBuffer io = IoBuffer.allocate(sum).setAutoExpand(true);
			io.clear();
			io.position(0);// 清空缓存并重置
			io.put(b);
			io.flip();
			out.write(io);
		} catch (Exception e) {
			
		}finally{
			if(out!=null){
				out.flush();
			}
		}
	}

}