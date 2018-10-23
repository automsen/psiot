package cn.com.tw.paas.monit.entity.db.org;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 对应数据库表 t_org_terminal_equip
 * @author Administrator
 *
 */
public class TerminalEquip implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2634829607415509511L;

	/**
	 * 仪表id
	 */
	private String equipId;

	/**
	 * 设备编号
	 */
    private String equipNumber;

    /**
	 * 设备名称
	 */
    private String equipName;

    /**
	 * 注册机构id
	 */
    private String orgId;
    
    /**
     * 机构名称
     */
    private String orgName;

    /**
	 * 应用id
	 */
    private String appId;
    
    /**
     * 应用名称
     */
    private String appName;
    
    /**
     * 应用appKey
     */
    private String appKey;
    
    /**
     * 应用回推地址
     */
    private String callbackUrl;

    /**
	 * 设备一级分类(字典code)
	 */
    private String equipCategCode;

    /**
	 * 设备二级分类(字典code)
	 */
    private String equipTypeCode;

    /**
	 * 型号id
	 */
    private String modelId;

    /**
	 * 软件版本号
	 */
    private String softVersoin;
    
    /**
     * 硬件版本号
     */
    private String hardwareVersoin;
    
    /**
     * 采集频率
     */
    private String gatherHz;
    
    /**
	 * 通信密码
	 */
    private String commPwd;

    /**
	 * 是否直连
	 */
    private Byte isDirect;

    /**
	 * 链路类型
	 */
    private String linkTypeCode;

    /**
	 * 集抄设备编号
	 */
    private String netEquipNumber;

    /**
	 * 上行网络类型
	 */
    private String netTypeCode;

    /**
	 * 网络编号
	 */
    private String netNumber;

    /**
	 * 电表子类
	 */
    private String elecMeterTypeCode;

    /**
	 * 额定电压
	 */
    private BigDecimal elecVoltageRating;

    /**
	 * 额定电流
	 */
    private BigDecimal elecCurrentRating;

    /**
	 * 额定功率
	 */
    private BigDecimal elecPowerRating;

    /**
	 * 最大电流
	 */
    private BigDecimal elecCurrentMax;

    /**
	 * 最大功率
	 */
    private BigDecimal elecPowerMax;

    /**
	 * 电压变比(默认值为1)
	 */
    private BigDecimal elecPt;

    /**
	 * 电流变比(默认值为1)
	 */
    private BigDecimal elecCt;

    /**
	 * 指令开
            指令关
            强制开
            强制关
            
	 */
    private String elecControlStatus;

    /**
	 * 是否可用
	 */
    private Byte isUsable;

    /**
	 * 是否历史设备
	 */
    private Byte isHistory;

    /**
	 * 创建时间
	 */
    private Date createTime;

    /**
	 * 修改时间
	 */
    private Date updateTime;

    private String meterSpec;
    
    private String modelName;
    
    private String baudRate;
    
    /**
     * 机构来源（用于返回绑定原厂id）
     */
    private String sourceOrgId;
    
    /**
     * 机构来源（用于返回绑定原厂应用id）
     */
    private String sourceAppId;
    
    /**
     * 操作人(userId)
     */
    private String operator;
    
    /**
     * 操作记录时间
     */
    private Date operationRecordingTime;
    
    /**
     * 对应扇区
     */
    private int sectors;
    
    /**
     * 用户名
     */
    private String fUserName;
    
    /**
     * 电话号码
     */
    private String fPhone;
    
    //扩展字段
    
    /**
     * app复合查询字段
     */
    private String search;
    
    /**
     * 各扇区计总
     */
    private int sectorsSum;
    
    private String protocolType;
    
    //批量修改id集合
    private List<String> idList;
    
    /**
     * 用于网关仪表绑定查询
     * 是否过滤 0不需要 1 需要
     */
    private Byte isFilter;
    
    /**
     * daas设备对象集合
     */
    private List<TerminalEquipChildren> dteList;
    
    /**
     * dtu属性对象
     */
    private TerminalEquipParamDtu terminalEquipParamDtu;
    
    /**
     * 热泵ID
     */
    private String childrenEquipNumber;
    
    /**
     * 设备厂家
     */
    private String manufacturer;
    
    /**
     * 设备类型
     */
    private String equipModel;
    
    /**
     * 下联设备类型
     */
    private String equipType;
    
    //dtu扩展属性字段
    
    private String coordinate;

    private Integer childrenNum;

    private String installSite;

    private Date installTime;
    
    private String installTimeStr;

    private String workingMode;

    private String functionDigit;

    private String rfBaudRate;

    private Byte beattim;

    private Integer reportingInterval;
    
    private String dtuSoftVersoin;
    
    private String protocolVersoin;
    
    /**
     * 门牌号Id
     */
    private String locationId;
    /**
     * 居民信息表
     */
    //private OrgResident orgResident;
    
    public String getEquipId() {
        return equipId;
    }

    public void setEquipId(String equipId) {
        this.equipId = equipId == null ? null : equipId.trim();
    }

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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getEquipCategCode() {
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
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId == null ? null : modelId.trim();
    }

    public String getSoftVersoin() {
        return softVersoin;
    }

    public void setSoftVersoin(String softVersoin) {
        this.softVersoin = softVersoin == null ? null : softVersoin.trim();
    }

    public String getCommPwd() {
        return commPwd;
    }

    public void setCommPwd(String commPwd) {
        this.commPwd = commPwd == null ? null : commPwd.trim();
    }

    public Byte getIsDirect() {
        return isDirect;
    }

    public void setIsDirect(Byte isDirect) {
        this.isDirect = isDirect;
    }

    public String getLinkTypeCode() {
        return linkTypeCode;
    }

    public void setLinkTypeCode(String linkTypeCode) {
        this.linkTypeCode = linkTypeCode == null ? null : linkTypeCode.trim();
    }

    public String getNetEquipNumber() {
        return netEquipNumber;
    }

    public void setNetEquipNumber(String netEquipNumber) {
        this.netEquipNumber = netEquipNumber == null ? null : netEquipNumber.trim();
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

    public String getElecMeterTypeCode() {
        return elecMeterTypeCode;
    }

    public void setElecMeterTypeCode(String elecMeterTypeCode) {
        this.elecMeterTypeCode = elecMeterTypeCode == null ? null : elecMeterTypeCode.trim();
    }

    public BigDecimal getElecVoltageRating() {
        return elecVoltageRating;
    }

    public void setElecVoltageRating(BigDecimal elecVoltageRating) {
        this.elecVoltageRating = elecVoltageRating;
    }

    public BigDecimal getElecCurrentRating() {
        return elecCurrentRating;
    }

    public void setElecCurrentRating(BigDecimal elecCurrentRating) {
        this.elecCurrentRating = elecCurrentRating;
    }

    public BigDecimal getElecPowerRating() {
        return elecPowerRating;
    }

    public void setElecPowerRating(BigDecimal elecPowerRating) {
        this.elecPowerRating = elecPowerRating;
    }

    public BigDecimal getElecCurrentMax() {
        return elecCurrentMax;
    }

    public void setElecCurrentMax(BigDecimal elecCurrentMax) {
        this.elecCurrentMax = elecCurrentMax;
    }

    public BigDecimal getElecPowerMax() {
        return elecPowerMax;
    }

    public void setElecPowerMax(BigDecimal elecPowerMax) {
        this.elecPowerMax = elecPowerMax;
    }

    public BigDecimal getElecPt() {
        return elecPt;
    }

    public void setElecPt(BigDecimal elecPt) {
        this.elecPt = elecPt;
    }

    public BigDecimal getElecCt() {
        return elecCt;
    }

    public void setElecCt(BigDecimal elecCt) {
        this.elecCt = elecCt;
    }

    public String getElecControlStatus() {
        return elecControlStatus;
    }

    public void setElecControlStatus(String elecControlStatus) {
        this.elecControlStatus = elecControlStatus == null ? null : elecControlStatus.trim();
    }

    public Byte getIsUsable() {
        return isUsable;
    }

    public void setIsUsable(Byte isUsable) {
        this.isUsable = isUsable;
    }

    public Byte getIsHistory() {
        return isHistory;
    }

    public void setIsHistory(Byte isHistory) {
        this.isHistory = isHistory;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getMeterSpec() {
		return meterSpec;
	}

	public void setMeterSpec(String meterSpec) {
		this.meterSpec = meterSpec;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getBaudRate() {
		return baudRate;
	}

	public void setBaudRate(String baudRate) {
		this.baudRate = baudRate;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getGatherHz() {
		return gatherHz;
	}

	public void setGatherHz(String gatherHz) {
		this.gatherHz = gatherHz;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperationRecordingTime() {
		return operationRecordingTime;
	}

	public void setOperationRecordingTime(Date operationRecordingTime) {
		this.operationRecordingTime = operationRecordingTime;
	}

	public String getSourceOrgId() {
		return sourceOrgId;
	}

	public void setSourceOrgId(String sourceOrgId) {
		this.sourceOrgId = sourceOrgId;
	}

	public String getSourceAppId() {
		return sourceAppId;
	}

	public void setSourceAppId(String sourceAppId) {
		this.sourceAppId = sourceAppId;
	}

	public int getSectors() {
		return sectors;
	}

	public void setSectors(int sectors) {
		this.sectors = sectors;
	}

	public int getSectorsSum() {
		return sectorsSum;
	}

	public void setSectorsSum(int sectorsSum) {
		this.sectorsSum = sectorsSum;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public Byte getIsFilter() {
		return isFilter;
	}

	public void setIsFilter(Byte isFilter) {
		this.isFilter = isFilter;
	}

	public List<String> getIdList() {
		return idList;
	}

	public void setIdList(List<String> idList) {
		this.idList = idList;
	}

	public List<TerminalEquipChildren> getDteList() {
		return dteList;
	}

	public void setDteList(List<TerminalEquipChildren> dteList) {
		this.dteList = dteList;
	}

	public String getChildrenEquipNumber() {
		return childrenEquipNumber;
	}

	public void setChildrenEquipNumber(String childrenEquipNumber) {
		this.childrenEquipNumber = childrenEquipNumber;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getEquipModel() {
		return equipModel;
	}

	public void setEquipModel(String equipModel) {
		this.equipModel = equipModel;
	}

	public String getEquipType() {
		return equipType;
	}

	public void setEquipType(String equipType) {
		this.equipType = equipType;
	}

	public String getfUserName() {
		return fUserName;
	}

	public void setfUserName(String fUserName) {
		this.fUserName = fUserName;
	}

	public String getfPhone() {
		return fPhone;
	}

	public void setfPhone(String fPhone) {
		this.fPhone = fPhone;
	}
	
	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public Integer getChildrenNum() {
		return childrenNum;
	}

	public void setChildrenNum(Integer childrenNum) {
		this.childrenNum = childrenNum;
	}

	public String getInstallSite() {
		return installSite;
	}

	public void setInstallSite(String installSite) {
		this.installSite = installSite;
	}

	public Date getInstallTime() {
		return installTime;
	}

	public void setInstallTime(Date installTime) {
		this.installTime = installTime;
	}

	public String getWorkingMode() {
		return workingMode;
	}

	public void setWorkingMode(String workingMode) {
		this.workingMode = workingMode;
	}

	public String getFunctionDigit() {
		return functionDigit;
	}

	public void setFunctionDigit(String functionDigit) {
		this.functionDigit = functionDigit;
	}

	public String getRfBaudRate() {
		return rfBaudRate;
	}

	public void setRfBaudRate(String rfBaudRate) {
		this.rfBaudRate = rfBaudRate;
	}

	public Byte getBeattim() {
		return beattim;
	}

	public void setBeattim(Byte beattim) {
		this.beattim = beattim;
	}

	public Integer getReportingInterval() {
		return reportingInterval;
	}

	public void setReportingInterval(Integer reportingInterval) {
		this.reportingInterval = reportingInterval;
	}

	public TerminalEquipParamDtu getTerminalEquipParamDtu() {
		return terminalEquipParamDtu;
	}

	public void setTerminalEquipParamDtu(TerminalEquipParamDtu terminalEquipParamDtu) {
		this.terminalEquipParamDtu = terminalEquipParamDtu;
	}

	public String getInstallTimeStr() {
		return installTimeStr;
	}

	public void setInstallTimeStr(String installTimeStr) {
		this.installTimeStr = installTimeStr;
	}

	public String getHardwareVersoin() {
		return hardwareVersoin;
	}

	public void setHardwareVersoin(String hardwareVersoin) {
		this.hardwareVersoin = hardwareVersoin;
	}

	public String getDtuSoftVersoin() {
		return dtuSoftVersoin;
	}

	public void setDtuSoftVersoin(String dtuSoftVersoin) {
		this.dtuSoftVersoin = dtuSoftVersoin;
	}

	public String getProtocolVersoin() {
		return protocolVersoin;
	}

	public void setProtocolVersoin(String protocolVersoin) {
		this.protocolVersoin = protocolVersoin;
	}
	@Override
	public String toString() {
		return "TerminalEquip [equipId=" + equipId + ", equipNumber="
				+ equipNumber + ", equipName=" + equipName + ", orgId=" + orgId
				+ ", orgName=" + orgName + ", appId=" + appId + ", appName="
				+ appName + ", appKey=" + appKey + ", callbackUrl="
				+ callbackUrl + ", equipCategCode=" + equipCategCode
				+ ", equipTypeCode=" + equipTypeCode + ", modelId=" + modelId
				+ ", softVersoin=" + softVersoin + ", hardwareVersoin="
				+ hardwareVersoin + ", gatherHz=" + gatherHz + ", commPwd="
				+ commPwd + ", isDirect=" + isDirect + ", linkTypeCode="
				+ linkTypeCode + ", netEquipNumber=" + netEquipNumber
				+ ", netTypeCode=" + netTypeCode + ", netNumber=" + netNumber
				+ ", elecMeterTypeCode=" + elecMeterTypeCode
				+ ", elecVoltageRating=" + elecVoltageRating
				+ ", elecCurrentRating=" + elecCurrentRating
				+ ", elecPowerRating=" + elecPowerRating + ", elecCurrentMax="
				+ elecCurrentMax + ", elecPowerMax=" + elecPowerMax
				+ ", elecPt=" + elecPt + ", elecCt=" + elecCt
				+ ", elecControlStatus=" + elecControlStatus + ", isUsable="
				+ isUsable + ", isHistory=" + isHistory + ", createTime="
				+ createTime + ", updateTime=" + updateTime + ", meterSpec="
				+ meterSpec + ", modelName=" + modelName + ", baudRate="
				+ baudRate + ", sourceOrgId=" + sourceOrgId + ", sourceAppId="
				+ sourceAppId + ", operator=" + operator
				+ ", operationRecordingTime=" + operationRecordingTime
				+ ", sectors=" + sectors + ", fUserName=" + fUserName
				+ ", fPhone=" + fPhone + ", search=" + search + ", sectorsSum="
				+ sectorsSum + ", protocolType=" + protocolType + ", idList="
				+ idList + ", isFilter=" + isFilter + ", dteList=" + dteList
				+ ", terminalEquipParamDtu=" + terminalEquipParamDtu
				+ ", childrenEquipNumber=" + childrenEquipNumber
				+ ", manufacturer=" + manufacturer + ", equipModel="
				+ equipModel + ", equipType=" + equipType + ", coordinate="
				+ coordinate + ", childrenNum=" + childrenNum
				+ ", installSite=" + installSite + ", installTime="
				+ installTime + ", installTimeStr=" + installTimeStr
				+ ", workingMode=" + workingMode + ", functionDigit="
				+ functionDigit + ", rfBaudRate=" + rfBaudRate + ", beattim="
				+ beattim + ", reportingInterval=" + reportingInterval
				+ ", dtuSoftVersoin=" + dtuSoftVersoin + ", protocolVersoin="
				+ protocolVersoin + "]";
	}

	/*public OrgResident getOrgResident() {
		return orgResident;
	}

	public void setOrgResident(OrgResident orgResident) {
		this.orgResident = orgResident;
	}*/

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
}