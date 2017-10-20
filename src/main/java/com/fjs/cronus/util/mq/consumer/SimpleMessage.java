package com.fjs.cronus.util.mq.consumer;

import io.swagger.annotations.ApiModelProperty;

public class SimpleMessage {

	@ApiModelProperty(value = "消息体", required = false)
	private String body;
	@ApiModelProperty(value = "消息ID（加密）", required = false)
	private String msgId;
	@ApiModelProperty(value = "消息生产时间", required = false)
	private String bornTime;
	@ApiModelProperty(value = "不知道（不能用汉字解释）", required = false)
	private String msgHandle;
	@ApiModelProperty(value = "重复消费次数", required = false)
	private int reconsumeTimes;
	@ApiModelProperty(value = "分支", required = false)
	private String tag;

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	public int getReconsumeTimes() {
		return reconsumeTimes;
	}

	public void setReconsumeTimes(int reconsumeTimes) {
		this.reconsumeTimes = reconsumeTimes;
	}

	public void setMsgHandle(String msgHandle) {
		this.msgHandle = msgHandle;
	}

	public String getMsgHandle() {
		return msgHandle;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public String getBornTime() {
		return bornTime;
	}

	public void setBornTime(String bornTime) {
		this.bornTime = bornTime;
	}

	@Override
	public String toString() {
		return "SimpleMessage [body=" + body + ", msgId=" + msgId + ", bornTime=" + bornTime + ", msgHandle="
				+ msgHandle + ", reconsumeTimes=" + reconsumeTimes + ", tag=" + tag + "]";
	}

}
