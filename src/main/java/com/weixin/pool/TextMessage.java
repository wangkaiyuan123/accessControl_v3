package com.weixin.pool;
/**
 * 回复文本消息类
 * @author Administrator
 *
 */
public class TextMessage extends BaseMessage{
	private String Content;
	private String MsgId;
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getMsgId() {
		return MsgId;
	}
	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
}
