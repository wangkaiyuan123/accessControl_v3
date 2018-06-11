package com.dhht.mina.server;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * 自定义实体类解码工具
 */
public class MyProtocalDecoder extends CumulativeProtocolDecoder {
	/**
	 *收到消息时会进入此过滤器
	 */
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		try {
			//System.out.print("服务器进入解码器，接收字节长度：");
			//System.out.println(in.limit()+"****************************");
			int pos = in.position();
			byte[] by = new byte[in.limit()];
			for(int i = pos;i<in.limit();i++){
				byte b = in.get(i);
				by[i]=b;
			}
			out.write(by);
		} catch (Exception e) {
			System.out.println(session.toString()+"catch"+e.getMessage());
			return false;
		}finally{
			in.position(in.limit());//指针后移
		}
		return false;
	}

}
