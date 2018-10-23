package cn.com.tw.engine.core.handler.http.lorawarn.param;

public class GateWayBean {

	private String fcntdown;//	字符串	下行帧区号	
	private String fcntup;//	字符串	上行帧区号	
	private String gweui;//	字符串	网关名称	
	private String rssi;//	字符串	信号强度	
	private String lsnr;//	字符串	信噪比	
	private String alti;//	字符串	高度	
	private String lng;//	字符串	经度	
	private String lati;//	字符串	纬度	
	public String getFcntdown() {
		return fcntdown;
	}
	public void setFcntdown(String fcntdown) {
		this.fcntdown = fcntdown;
	}
	public String getFcntup() {
		return fcntup;
	}
	public void setFcntup(String fcntup) {
		this.fcntup = fcntup;
	}
	public String getGweui() {
		return gweui;
	}
	public void setGweui(String gweui) {
		this.gweui = gweui;
	}
	public String getRssi() {
		return rssi;
	}
	public void setRssi(String rssi) {
		this.rssi = rssi;
	}
	public String getLsnr() {
		return lsnr;
	}
	public void setLsnr(String lsnr) {
		this.lsnr = lsnr;
	}
	public String getAlti() {
		return alti;
	}
	public void setAlti(String alti) {
		this.alti = alti;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getLati() {
		return lati;
	}
	public void setLati(String lati) {
		this.lati = lati;
	}
	@Override
	public String toString() {
		return "GateWayBean [fcntdown=" + fcntdown + ", fcntup=" + fcntup
				+ ", gweui=" + gweui + ", rssi=" + rssi + ", lsnr=" + lsnr
				+ ", alti=" + alti + ", lng=" + lng + ", lati=" + lati + "]";
	}
	
}
