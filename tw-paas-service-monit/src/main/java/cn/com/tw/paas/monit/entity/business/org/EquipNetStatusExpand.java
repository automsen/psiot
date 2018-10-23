package cn.com.tw.paas.monit.entity.business.org;

import cn.com.tw.paas.monit.entity.db.org.EquipNetStatus;

/**
 * 设备连接状态 扩展字段
 * @author Administrator
 *
 */
public class EquipNetStatusExpand extends EquipNetStatus{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7918063437134278740L;
	
	/**
	 * 设备名称
	 */
	private String equipName;
	
	/**
	 * 机构ID
	 */
	private String orgId;
	
	/**
	 * 应用ID
	 */
	private String appId;
	
	/**
	 * 机构名称
	 */
	private String orgName;
	
	/**
	 * 网络连接方式
	 */
	private String netTypeCode;
	
	/**
	 * 设备一级分类
	 */
	private String equipCategCode;
	
	/**
	 * 设备2级类型(字典code)
	 */
    private String equipTypeCode;
    
    /**
     * 终端编号
     */
    private String equipNumber;

	public String getEquipName() {
		return equipName;
	}

	public void setEquipName(String equipName) {
		this.equipName = equipName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getNetTypeCode() {
		return netTypeCode;
	}

	public void setNetTypeCode(String netTypeCode) {
		this.netTypeCode = netTypeCode;
	}

	public String getEquipCategCode() {
		return equipCategCode;
	}

	public void setEquipCategCode(String equipCategCode) {
		this.equipCategCode = equipCategCode;
	}

	public String getEquipTypeCode() {
		return equipTypeCode;
	}

	public void setEquipTypeCode(String equipTypeCode) {
		this.equipTypeCode = equipTypeCode;
	}

	public String getEquipNumber() {
		return equipNumber;
	}

	public void setEquipNumber(String equipNumber) {
		this.equipNumber = equipNumber;
	}
	
	
}
