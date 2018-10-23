package cn.com.tw.paas.monit.entity.business.orgEquipment;

import java.util.Date;

import cn.com.tw.paas.monit.entity.db.orgEquipment.OrgEquipment;

/**
 * 设备扩展字段
 * @author Administrator
 *
 */
public class OrgEquipmentExpand extends OrgEquipment{
	
	private static final long serialVersionUID = -5269067620635072248L;

	/**
	 * 设备总数
	 */
	private String totalOrgEquipment;
	
	/**
	 * 型号名称(国标)
	 */
    private String modelName;

    /**
	 * 完整型号名称
	 */
    private String fullName;

    /**
	 * 产品名称
	 */
    private String productName;

    /**
	 * 生产厂家
	 */
    private String factory;

    /**
	 * 设备一级分类(字典code)
	 */
    private String modelEquipCateg;
    /**
     * 一级分类名
     */
    private String equipCategName;

    /**
	 * 设备二级分类(字典code)
	 */
    private String modelEquipType;
    /**
     *二级分类名 
     */
    private String typeName;

	/**
	 * 协议类型
	 */
    private String protocolType;

    /**
	 * 波特率
	 */
    private String baudRate;

    /**
	 * 网络连接方式
	 */
    private String modelNetType;
    /**
     * 网络连接方式名
     */
    private String netName;

    /**
	 * 是否支持主动上传
	 */
    private Byte isInitiativePush;

    /**
	 * 是否支持远程通断
	 */
    private Byte isRemoteControl;

    /**
	 * 是否支持分时计量
	 */
    private Byte isTimearea;

    /**
	 * 是否支持表计费控 1.是 0.否
	 */
    private Byte isCostControl;

    /**
	 * 是否可用 0，不可用 1，可用
	 */
    private Byte modelIsUsable;

    /**
	 * 创建时间
	 */
    private Date modelCreateTime;

    /**
	 * 更新时间
	 */
    private Date modelUpdateTime;
    
    /**
     * 最近在线时间
     */
    private Date onlineTime;

    /**
     * 最近离线时间
     */
    private Date offlineTime;

	public String getTotalOrgEquipment() {
		return totalOrgEquipment;
	}

	public void setTotalOrgEquipment(String totalOrgEquipment) {
		this.totalOrgEquipment = totalOrgEquipment;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public String getBaudRate() {
		return baudRate;
	}

	public void setBaudRate(String baudRate) {
		this.baudRate = baudRate;
	}

	public Byte getIsInitiativePush() {
		return isInitiativePush;
	}

	public void setIsInitiativePush(Byte isInitiativePush) {
		this.isInitiativePush = isInitiativePush;
	}

	public Byte getIsRemoteControl() {
		return isRemoteControl;
	}

	public void setIsRemoteControl(Byte isRemoteControl) {
		this.isRemoteControl = isRemoteControl;
	}

	public Byte getIsTimearea() {
		return isTimearea;
	}

	public void setIsTimearea(Byte isTimearea) {
		this.isTimearea = isTimearea;
	}

	public Byte getIsCostControl() {
		return isCostControl;
	}

	public void setIsCostControl(Byte isCostControl) {
		this.isCostControl = isCostControl;
	}

	public String getModelEquipCateg() {
		return modelEquipCateg;
	}

	public void setModelEquipCateg(String modelEquipCateg) {
		this.modelEquipCateg = modelEquipCateg;
	}

	public String getModelEquipType() {
		return modelEquipType;
	}

	public void setModelEquipType(String modelEquipType) {
		this.modelEquipType = modelEquipType;
	}

	public String getModelNetType() {
		return modelNetType;
	}

	public void setModelNetType(String modelNetType) {
		this.modelNetType = modelNetType;
	}

	public Byte getModelIsUsable() {
		return modelIsUsable;
	}

	public void setModelIsUsable(Byte modelIsUsable) {
		this.modelIsUsable = modelIsUsable;
	}

	public Date getModelCreateTime() {
		return modelCreateTime;
	}

	public void setModelCreateTime(Date modelCreateTime) {
		this.modelCreateTime = modelCreateTime;
	}

	public Date getModelUpdateTime() {
		return modelUpdateTime;
	}

	public void setModelUpdateTime(Date modelUpdateTime) {
		this.modelUpdateTime = modelUpdateTime;
	}

	public String getEquipCategName() {
		return equipCategName;
	}

	public void setEquipCategName(String equipCategName) {
		this.equipCategName = equipCategName;
	}

	public String getNetName() {
		return netName;
	}

	public void setNetName(String netName) {
		this.netName = netName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Date getOnlineTime() {
		return onlineTime;
	}

	public void setOnlineTime(Date onlineTime) {
		this.onlineTime = onlineTime;
	}

	public Date getOfflineTime() {
		return offlineTime;
	}

	public void setOfflineTime(Date offlineTime) {
		this.offlineTime = offlineTime;
	}

}
