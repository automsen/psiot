package cn.com.tw.paas.monit.entity.business.excel;

import java.util.Date;

public class TerminalEquipDtuExcel {

    /**
	 * 设备名称
	 */
    private String equipName;
    
    /**
	 * 设备编号
	 */
    private String equipNumber;

    /**
	 * 设备一级分类(字典code)
	 */
    //private String equipCategCode;

    /**
	 * 设备二级分类(字典code)
	 */
    //private String equipTypeCode;

    /**
	 * 软件版本号
	 */
    private String softVersoin;
    
    /**
     * 硬件版本号
     */
    private String hardwareVersoin;
    
    /**
     * dtu型号名称
     */
    private String modelName;
    
    /**
	 * 上行网络类型
	 */
    private String netTypeCode;

    /**
	 * 网络编号
	 */
    private String netNumber;
    
    /**
     * dtu型号id
     */
    private String modelId;
    
   /* *//**
     * 用户名
     *//*
    private String fUserName;
    
    *//**
     * 电话号码
     *//*
    private String fPhone;*/
    
    /**
     * 热泵ID
     */
    /*private String dtuEquipNumber;
    
    *//**
     * 设备厂家
     *//*
    private String manufacturer;
    
    *//**
     * 设备类型
     *//*
    private String equipModel;
    
    *//**
     * 下联设备类型
     *//*
    private String equipType;
    
    //dtu扩展属性字段
    
    private String coordinate;

    private String installSite;

    private String installTimeStr;

    private String workingMode;

    private String functionDigit;

    private String rfBaudRate;

    private String beattim;

    private String reportingInterval;
    
    private String dtuSoftVersoin;
    
    private String protocolVersoin;*/
    
    private Date createTime;
    

	public String getEquipNumber() {
		return equipNumber;
	}

	public void setEquipNumber(String equipNumber) {
		this.equipNumber = equipNumber == null ? null : equipNumber.trim();
	}

	public String getEquipName() {
		return equipName;
	}

	public void setEquipName(String equipName) {
		this.equipName = equipName == null ? null : equipName.trim();
	}



	/*public String getEquipCategCode() {
		return equipCategCode;
	}

	public void setEquipCategCode(String equipCategCode) {
		this.equipCategCode = equipCategCode == null ? null : equipCategCode.trim();
	}

	public String getEquipTypeCode() {
		return equipTypeCode;
	}

	public void setEquipTypeCode(String equipTypeCode) {
		this.equipTypeCode = equipTypeCode == null ? null : equipTypeCode.trim();
	}*/

	public String getSoftVersoin() {
		return softVersoin;
	}

	public void setSoftVersoin(String softVersoin) {
		this.softVersoin = softVersoin == null ? null : softVersoin.trim();
	}

	public String getHardwareVersoin() {
		return hardwareVersoin;
	}

	public void setHardwareVersoin(String hardwareVersoin) {
		this.hardwareVersoin = hardwareVersoin == null ? null : hardwareVersoin.trim();
	}

	public String getNetTypeCode() {
		return netTypeCode;
	}

	public void setNetTypeCode(String netTypeCode) {
		this.netTypeCode = netTypeCode == null ? null : netTypeCode.trim();
	}

	public String getNetNumber() {
		return netNumber;
	}

	public void setNetNumber(String netNumber) {
		this.netNumber = netNumber == null ? null : netNumber.trim();
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName == null ? null : modelName.trim();
	}

	/*public String getfUserName() {
		return fUserName;
	}

	public void setfUserName(String fUserName) {
		this.fUserName = fUserName == null ? null : fUserName.trim();
	}

	public String getfPhone() {
		return fPhone;
	}

	public void setfPhone(String fPhone) {
		this.fPhone = fPhone == null ? null : fPhone.trim();
	}

	public String getDtuEquipNumber() {
		return dtuEquipNumber;
	}

	public void setDtuEquipNumber(String dtuEquipNumber) {
		this.dtuEquipNumber = dtuEquipNumber == null ? null : dtuEquipNumber.trim();
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer == null ? null : manufacturer.trim();
	}

	public String getEquipModel() {
		return equipModel;
	}

	public void setEquipModel(String equipModel) {
		this.equipModel = equipModel == null ? null : equipModel.trim();
	}

	public String getEquipType() {
		return equipType;
	}

	public void setEquipType(String equipType) {
		this.equipType = equipType == null ? null : equipType.trim();
	}

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate == null ? null : coordinate.trim();
	}

	public String getInstallSite() {
		return installSite;
	}

	public void setInstallSite(String installSite) {
		this.installSite = installSite == null ? null : installSite.trim();
	}

	public String getInstallTimeStr() {
		return installTimeStr;
	}

	public void setInstallTimeStr(String installTimeStr) {
		this.installTimeStr = installTimeStr == null ? null : installTimeStr.trim();
	}

	public String getWorkingMode() {
		return workingMode;
	}

	public void setWorkingMode(String workingMode) {
		this.workingMode = workingMode == null ? null : workingMode.trim();
	}

	public String getFunctionDigit() {
		return functionDigit;
	}

	public void setFunctionDigit(String functionDigit) {
		this.functionDigit = functionDigit == null ? null : functionDigit.trim();
	}

	public String getRfBaudRate() {
		return rfBaudRate;
	}

	public void setRfBaudRate(String rfBaudRate) {
		this.rfBaudRate = rfBaudRate == null ? null : rfBaudRate.trim();
	}

	public String getBeattim() {
		return beattim;
	}

	public void setBeattim(String beattim) {
		this.beattim = beattim == null ? null : beattim.trim();
	}

	public String getReportingInterval() {
		return reportingInterval;
	}

	public void setReportingInterval(String reportingInterval) {
		this.reportingInterval = reportingInterval == null ? null : reportingInterval.trim();
	}

	public String getDtuSoftVersoin() {
		return dtuSoftVersoin;
	}

	public void setDtuSoftVersoin(String dtuSoftVersoin) {
		this.dtuSoftVersoin = dtuSoftVersoin == null ? null : dtuSoftVersoin.trim();
	}

	public String getProtocolVersoin() {
		return protocolVersoin;
	}

	public void setProtocolVersoin(String protocolVersoin) {
		this.protocolVersoin = protocolVersoin == null ? null : protocolVersoin.trim();
	}*/

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

}
