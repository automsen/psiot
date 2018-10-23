package cn.com.tw.common.utils.notify;

import cn.com.tw.common.enm.notify.NotifyBusTypeEm;
import cn.com.tw.common.enm.notify.NotifyLvlEm;
import cn.com.tw.common.enm.notify.NotifyPlatTypeEm;
import cn.com.tw.common.enm.notify.NotifyTypeEm;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.tools.json.JsonUtils;

import com.alibaba.fastjson.JSONObject;

public class Notify {
	
	//平台类型
	private int platType;
	
	//通知所在企业Id
	private String orgId;
	
	//业务类型 充值，退费
	private int notifyBusType;
	
	//通知业务号
	private String notifyBusNo;
	
	//通知级别
	private int notifyLevel;
	
	//通知类型，短信，微信，邮件, 平台
	private int notifyType;
	
	private int limitNotifyTimes = 5;
	
	private String notifyUrl;
	
	private String notifyToNo;
	
	private String notifyToUserId;
	
	private String notifyToUserName;
	
	private String notifyContent;
	
	private String notifyContentType;
	
	private String extField1;

    private String extField2;

    private String extField3;

    private String extField4;

    private String extField5;
	
	private Notify() {
		
	}
	
	public String getNotifyToNo() {
		return notifyToNo;
	}

	public void setNotifyToNo(String notifyToNo) {
		this.notifyToNo = notifyToNo;
	}

	public String getNotifyToUserId() {
		return notifyToUserId;
	}

	public void setNotifyToUserId(String notifyToUserId) {
		this.notifyToUserId = notifyToUserId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/**
	 * 构造短信内容
	 * @param phoneNum
	 * @param templNum
	 * @param content
	 * @return
	 */
	public static Notify createSms(String phoneNum, String templNum, String content) {
		JSONObject json = new JSONObject();
		json.put("phoneNum", phoneNum);
		json.put("templNum", templNum);
		json.put("message", content);
		String message = JSONObject.toJSONString(json);
		message = CommUtils.base64Encode(message);
		Notify notify = new Notify();
		notify.setNotifyUrl("send/sms?message=" + message);
		notify.notifyToNo = phoneNum;
		notify.notifyType = NotifyTypeEm.SMS.getValue();
		notify.notifyContent = content;
		return notify;
	}
	
	public static Notify createAliSms(String phoneNum, String templNum, String content, String signature) {
		JSONObject json = new JSONObject();
		json.put("phoneNum", phoneNum);
		json.put("templNum", templNum);
		json.put("message", content);
		json.put("signature", signature);
		String message = JSONObject.toJSONString(json);
		message = CommUtils.base64Encode(message);
		Notify notify = new Notify();
		notify.setNotifyUrl("aliSMS/verify?message=" + message);
		notify.notifyToNo = phoneNum;
		notify.notifyType = NotifyTypeEm.SMS.getValue();
		notify.notifyContent = content;
		return notify;
	}
	
	public static Notify createEvent(String notifyToNo, String notifyUrl, String jsonContent, int limitNotifyTimes) {
		Notify notify = new Notify();
		notify.setNotifyUrl(notifyUrl);
		notify.setNotifyContent(jsonContent);
		notify.setNotifyContentType("JSON");
		notify.setNotifyToNo(notifyToNo);
		notify.setNotifyType(NotifyTypeEm.PLAT.getValue());
		return notify;
	}
	
	public Notify setPaasBusData(NotifyBusTypeEm busType, String busNo, String toUserId, String notifyToUserName, String orgId, NotifyLvlEm lvl) {
		this.platType = NotifyPlatTypeEm.paas.getValue();
		this.notifyBusType = busType.getValue();
		this.notifyBusNo = busNo;
		this.notifyToUserId = toUserId;
		this.setNotifyToUserName(notifyToUserName);
		this.orgId = orgId;
		this.notifyLevel = lvl.getValue();
		return this;
	}
	
	public Notify setSaasBusData(NotifyBusTypeEm busType, String busNo, String toUserId, String notifyToUserName, String orgId, NotifyLvlEm lvl) {
		this.platType = NotifyPlatTypeEm.saas.getValue();
		this.notifyBusType = busType.getValue();
		this.notifyBusNo = busNo;
		this.notifyToUserId = toUserId;
		this.setNotifyToUserName(notifyToUserName);
		this.orgId = orgId;
		this.notifyLevel = lvl.getValue();
		return this;
	}
	
	public Notify setSaasExt(String roomId, String roomName) {
		this.extField1 = roomId;
		this.extField2 = roomName;
		return this;
	}
	
	public String toJsonStr(){
		return JsonUtils.objectToJson(this);
	}

	public int getNotifyBusType() {
		return notifyBusType;
	}

	public void setNotifyBusType(NotifyBusTypeEm notifyBusType) {
		this.notifyBusType = notifyBusType.getValue();
	}

	public String getNotifyBusNo() {
		return notifyBusNo;
	}

	public void setNotifyBusNo(String notifyBusNo) {
		this.notifyBusNo = notifyBusNo;
	}

	@Override
	public String toString() {
		return "Notify [platType=" + platType + ", orgId=" + orgId
				+ ", notifyBusType=" + notifyBusType + ", notifyBusNo="
				+ notifyBusNo + ", notifyLevel=" + notifyLevel
				+ ", notifyType=" + notifyType + ", limitNotifyTimes="
				+ limitNotifyTimes + ", notifyUrl=" + notifyUrl
				+ ", notifyToNo=" + notifyToNo + ", notifyToUserId="
				+ notifyToUserId + ", notifyToUserName=" + notifyToUserName
				+ ", notifyContent=" + notifyContent + ", notifyContentType="
				+ notifyContentType + ", extField1=" + extField1
				+ ", extField2=" + extField2 + ", extField3=" + extField3
				+ ", extField4=" + extField4 + ", extField5=" + extField5 + "]";
	}

	public int getNotifyLevel() {
		return notifyLevel;
	}

	public void setNotifyLevel(int notifyLevel) {
		this.notifyLevel = notifyLevel;
	}

	public void setNotifyBusType(int notifyBusType) {
		this.notifyBusType = notifyBusType;
	}

	public void setNotifyType(int notifyType) {
		this.notifyType = notifyType;
	}

	public int getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(NotifyTypeEm notifyType) {
		this.notifyType = notifyType.getValue();
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public int getPlatType() {
		return platType;
	}

	public void setPlatType(int platType) {
		this.platType = platType;
	}

	public String getExtField1() {
		return extField1;
	}

	public void setExtField1(String extField1) {
		this.extField1 = extField1;
	}

	public String getExtField2() {
		return extField2;
	}

	public void setExtField2(String extField2) {
		this.extField2 = extField2;
	}

	public String getExtField3() {
		return extField3;
	}

	public void setExtField3(String extField3) {
		this.extField3 = extField3;
	}

	public String getExtField4() {
		return extField4;
	}

	public void setExtField4(String extField4) {
		this.extField4 = extField4;
	}

	public String getExtField5() {
		return extField5;
	}

	public void setExtField5(String extField5) {
		this.extField5 = extField5;
	}

	public String getNotifyToUserName() {
		return notifyToUserName;
	}

	public void setNotifyToUserName(String notifyToUserName) {
		this.notifyToUserName = notifyToUserName;
	}

	public String getNotifyContent() {
		return notifyContent;
	}

	public void setNotifyContent(String notifyContent) {
		this.notifyContent = notifyContent;
	}

	public int getLimitNotifyTimes() {
		return limitNotifyTimes;
	}

	public void setLimitNotifyTimes(int limitNotifyTimes) {
		this.limitNotifyTimes = limitNotifyTimes;
	}

	public String getNotifyContentType() {
		return notifyContentType;
	}

	public void setNotifyContentType(String notifyContentType) {
		this.notifyContentType = notifyContentType;
	}
	
}
