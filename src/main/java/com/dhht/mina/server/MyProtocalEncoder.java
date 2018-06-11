package com.dhht.mina.server;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * 自定义编码工具
 */
public class MyProtocalEncoder extends ProtocolEncoderAdapter {
	/**
	 * 发出消息时进入此过滤器
	 */
	public void encode(IoSession session, Object message,ProtocolEncoderOutput out) throws Exception {
		//System.out.println("服务器进入编码器");
		byte[] b = (byte[])message;
//		System.out.println(SendDataUtil.BinaryToHexString(b));
		int sum =b.length;
		IoBuffer io = IoBuffer.allocate(sum).setAutoExpand(true);
		io.clear();
		io.position(0);// 清空缓存并重置
		io.put(b);
		io.flip();
		out.write(io);
		out.flush();
	}

	public void dispose() throws Exception {
	}
}
