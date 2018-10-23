package cn.com.tw.saas.serv.entity.view.maint;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 通信测试
 * 页面实体
 * @author admin
 *
 */
public class CommTestModel {
	/**
	 * 机构id
	 */
	private String orgId;
	/**
	 * 仪表编号
	 */
	private String meterAddr;
	
	/**
	 * 仪表类型
	 */
	private String meterType;
	
	/**
	 * 仪表型号名称
	 */
	private String meterModelName;
	
	/**
	 * 仪表规格
	 */
	private String meterSpec;
	
	/**
	 * 房间id
	 */
	private String roomId;
	
	/**
	 * 房间名
	 */
	private String roomName;
	
	/**
	 * 房间号
	 */
	private String roomNumber;
	
	/**
	 * 楼栋全名
	 */
	private String regionFullName;
	
	/**
	 * 仪表安装位置
	 */
	private String meterInstall;
	
	/**
	 * 是否直连
	 */
    private Byte isDirect;
    
	/**
	 * 网关编号
	 */
	private String netNumber;
	
	/**
	 * 网关型号名称
	 */
	private String netModelName;
	
	/**
	 * 网关安装位置
	 */
	private String netInstall;
	
	/**
	 * 网关类型
	 */
	private String netType;

	/**
	 * 最近读数（止码）
	 */
	private BigDecimal readValue;
	
	/**
	 * 最近读数时间
	 */
	private Date readTime;
	
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getMeterAddr() {
		return meterAddr;
	}
	public void setMeterAddr(String meterAddr) {
		this.meterAddr = meterAddr;
	}
	public String getMeterType() {
		return meterType;
	}
	public void setMeterType(String meterType) {
		this.meterType = meterType;
	}
	public String getMeterModelName() {
		return meterModelName;
	}
	public void setMeterModelName(String meterModelName) {
		this.meterModelName = meterModelName;
	}
	public String getMeterSpec() {
		return meterSpec;
	}
	public void setMeterSpec(String meterSpec) {
		this.meterSpec = meterSpec;
	}
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}
	public String getMeterInstall() {
		return meterInstall;
	}
	public void setMeterInstall(String meterInstall) {
		this.meterInstall = meterInstall;
	}
	public Byte getIsDirect() {
		return isDirect;
	}
	public void setIsDirect(Byte isDirect) {
		this.isDirect = isDirect;
	}
	public String getNetNumber() {
		return netNumber;
	}
	public void setNetNumber(String netNumber) {
		this.netNumber = netNumber;
	}
	public String getNetModelName() {
		return netModelName;
	}
	public void setNetModelName(String netModelName) {
		this.netModelName = netModelName;
	}
	public String getNetInstall() {
		return netInstall;
	}
	public void setNetInstall(String netInstall) {
		this.netInstall = netInstall;
	}
	public String getNetType() {
		return netType;
	}
	public void setNetType(String netType) {
		this.netType = netType;
	}
	public Date getReadTime() {
		return readTime;
	}
	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}
	public BigDecimal getReadValue() {
		return readValue;
	}
	public void setReadValue(BigDecimal readValue) {
		this.readValue = readValue;
	}
	public String getRegionFullName() {
		return regionFullName;
	}
	public void setRegionFullName(String regionFullName) {
		this.regionFullName = regionFullName;
	}
}
