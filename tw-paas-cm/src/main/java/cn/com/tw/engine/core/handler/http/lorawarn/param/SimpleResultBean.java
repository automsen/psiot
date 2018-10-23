package cn.com.tw.engine.core.handler.http.lorawarn.param;

import java.io.Serializable;
import java.util.List;

public class SimpleResultBean implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2534570136951006609L;
	/**
	 * 
	 */
	private String mac;  // 设备唯一编号	节点模块的DevEui	是
	private String appeui;	//应用服务电子识别号		是
	private String last_upate_time; //数据上传时间	格式：yyyyMMddHHmmss	是
	private String data;	//设备传输数据	16进制字符串	是
	private Object reserver;	//字符串	保留数据字段		是
	private String data_type;	//数字	数据类型	223表示LoRa节点自发的心跳包，其他表示数据	是

	private List<GateWayBean> gateways;

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getAppeui() {
		return appeui;
	}

	public void setAppeui(String appeui) {
		this.appeui = appeui;
	}

	public String getLast_upate_time() {
		return last_upate_time;
	}

	public void setLast_upate_time(String last_upate_time) {
		this.last_upate_time = last_upate_time;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Object getReserver() {
		return reserver;
	}

	public void setReserver(Object reserver) {
		this.reserver = reserver;
	}

	public String getData_type() {
		return data_type;
	}

	public void setData_type(String data_type) {
		this.data_type = data_type;
	}

	public List<GateWayBean> getGateways() {
		return gateways;
	}

	public void setGateways(List<GateWayBean> gateways) {
		this.gateways = gateways;
	}

	@Override
	public String toString() {
		return "SimpleResultBean [mac=" + mac + ", appeui=" + appeui
				+ ", last_upate_time=" + last_upate_time + ", data=" + data
				+ ", reserver=" + reserver + ", data_type=" + data_type
				+ ", gateways=" + gateways + "]";
	}
	
}
