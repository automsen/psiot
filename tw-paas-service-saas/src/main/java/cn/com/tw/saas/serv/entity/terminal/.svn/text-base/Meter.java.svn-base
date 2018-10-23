
package cn.com.tw.saas.serv.entity.terminal;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.util.StringUtils;

import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.saas.serv.common.utils.cons.EnergyCategEum;

/**
 * 仪表与仪表账户
 * 数据库实体
 * t_saas_meter
 */
public class Meter {

    private String meterId;

    /**
     * 机构id
     */
    private String orgId;
    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 仪表编号
     */
    private String meterAddr;

    /**
     * 仪表类型
     */
    private String meterType;

    /**
     * 能源类型
     */
    private String energyType;

    /**
     * 型号名称
     */
    private String modelName;

    /**
     * 软件版本
     */
    private String softVersoin;

    /**
     * 采集频率(分钟)
     */
    private Integer gatherHz;

    /**
     * 是否支持远程通断
     */
    private Byte isRemoteControl;

    /**
     * 是否支持表计费控
     */
    private Byte isCostControl;

    /**
     * 是否直连
     */
    private Byte isDirect;
    
    /**
     * 是否支持用电负载识别
     */
    private Byte isElecLoad;
    
    /**
     * 是否支持最大功率限制
     */
    private Byte isElecPowerMaxLimit;
    
    /**
     * 是否启用按时供电
     */
    private Byte isElecOnTime;
    
    /**
     * 电流分时段限制规则id
     */
    private String currentTimeLimitId;
    
    /**
     * 功率分段限制规则id
     */
    private String powerLadderLimitId;
    
    /**
     * 功率波动限制规则id
     */
    private String powerFluctuationLimitId;
    
    /**
     * 功率突变限制规则id
     */
    private String powerChangeLimitId;
    
    /**
     * 功率最大限制规则id
     */
    private String powerMaxLimitId;

    /**
     * 上行通讯方式
     */
    private String netTypeCode;

    /**
     * 网关编号
     */
    private String netNumber;

    /**
     * 仪表规格
     */
    private String meterSpec;

    /**
     * 安装时起始读数
     */
    private BigDecimal installRead;

    /**
     * 上级仪表
     */
    private String pMeterAddr;

    /**
     * 仪表名称
     */
    private String meterName;

    /**
     * 安装位置
     */
    private String meterInstallAddr;

    /**
     * 仪表详情
     */
    private String meterDetails;

    /**
     * 电压变比
     */
    private Integer elecPt;

    /**
     * 电流变比
     */
    private Integer elecCt;

    /**
     * 波特率
     */
    private String baudRate;

    /**
     * 房间id
     */
    private String roomId;

    /**
     * 房间账户id
     */
    private String accountId;

    /**
     * 仪表账户状态
            0，尚未激活  
1，使用中
2，审核中
3，废弃中
4.空置
5.历史状态
     */
    private Byte subAccountStatus;

    /**
     * 仪表账户id
     */
    private String subAccountId;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 计价方式
     */
    private String priceModeCode;

    /**
     * 价格id
     */
    private String priceId;

    private Byte priceStatus;

    /**
     * 预警id
     */
    private String alarmId;

    private Byte alarmStatus;

    /**
     * 仪表账户起始时间
     */
    private Date startTime;

    /**
     * 仪表账户起始读数
     */
    private BigDecimal startRead;

    /**
     * 余额更新时间
     */
    private Date balanceUpdateTime;

    /**
     * 余额更新时读数
     */
    private BigDecimal balanceUpdateRead;

    /**
     * 余额更新序号
     */
    private Integer balanceUpdateNo;

    private Date createTime;

    private Date updateTime;
    
    /**
     * 最后读数时间
     */
    private Date lastReadTime;
    
    /**
     * 手机APP使用表号
     */
    private String equipNumber;
    
    /**
	 * 1.断 0.开
	 */
	private BigDecimal readValue;
	/**
	 * 最后止码
	 */
	private BigDecimal totalActiveE;
	/**
	 * 通信状态 1.正常 0.不正常
	 */
	private String communicationType;
	/**
	 * 最近读数时间
	 */
	private Date readTime;
	/**
	 * 房间名称
	 */
	private String roomName;
	/**
	 * 客户姓名
	 */
	private String customerRealname;
	/**
	 * 房间编号
	 */
	private String roomNumber;
	/**
	 * 楼栋加房间全名
	 */
	private String fullName;
	/**
	 * 楼栋编号
	 */
	private String regionNumber;
	/**
	 * 房间类型
	 */
	private String roomUse;
	/**
	 * 客户电话
	 */
	private String customerMobile1;
	/**
	 * 客户标识
	 */
	private String customerNo;
	/**
	 * 客户ID
	 */
	private String customerId;
	/**
	 * 客户类型
	 */
	private String customerType;
	/**
	 * 楼栋ID
	 */
	private String regionId;
	
	/**
	 * 设备数量
	 */
	private String number;
	
	/**
     * 基准价格（尖）
     */
    private BigDecimal price1;
    
    
    

    public String getMeterId() {
        return meterId;
    }
    public void setMeterId(String meterId) {
        this.meterId = meterId == null ? null : meterId.trim();
    }
    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }
    public String getMeterAddr() {
        return meterAddr;
    }
    public void setMeterAddr(String meterAddr) {
        this.meterAddr = meterAddr == null ? null : meterAddr.trim();
    }
    public String getMeterType() {
        return meterType;
    }
    public void setMeterType(String meterType) {
        this.meterType = meterType == null ? null : meterType.trim();
    }
    public String getEnergyType() {
        return energyType;
    }
    public void setEnergyType(String energyType) {
        this.energyType = energyType == null ? null : energyType.trim();
    }
    public String getModelName() {
        return modelName;
    }
    public void setModelName(String modelName) {
        this.modelName = modelName == null ? null : modelName.trim();
    }
    public String getSoftVersoin() {
        return softVersoin;
    }
    public void setSoftVersoin(String softVersoin) {
        this.softVersoin = softVersoin == null ? null : softVersoin.trim();
    }
    public Integer getGatherHz() {
        return gatherHz;
    }
    public void setGatherHz(Integer gatherHz) {
        this.gatherHz = gatherHz;
    }
    public Byte getIsRemoteControl() {
        return isRemoteControl;
    }
    public void setIsRemoteControl(Byte isRemoteControl) {
        this.isRemoteControl = isRemoteControl;
    }
    public Byte getIsCostControl() {
        return isCostControl;
    }
    public void setIsCostControl(Byte isCostControl) {
        this.isCostControl = isCostControl;
    }
    public Byte getIsDirect() {
        return isDirect;
    }
    public void setIsDirect(Byte isDirect) {
        this.isDirect = isDirect;
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
    public String getMeterSpec() {
        return meterSpec;
    }
    public void setMeterSpec(String meterSpec) {
        this.meterSpec = meterSpec == null ? null : meterSpec.trim();
    }
    public BigDecimal getInstallRead() {
        return installRead;
    }
    public void setInstallRead(BigDecimal installRead) {
        this.installRead = installRead;
    }
    public String getpMeterAddr() {
        return pMeterAddr;
    }
    public void setpMeterAddr(String pMeterAddr) {
        this.pMeterAddr = pMeterAddr == null ? null : pMeterAddr.trim();
    }
    public String getMeterName() {
        return meterName;
    }
    public void setMeterName(String meterName) {
        this.meterName = meterName == null ? null : meterName.trim();
    }
    public String getMeterInstallAddr() {
        return meterInstallAddr;
    }
    public void setMeterInstallAddr(String meterInstallAddr) {
        this.meterInstallAddr = meterInstallAddr == null ? null : meterInstallAddr.trim();
    }
    public String getMeterDetails() {
        return meterDetails;
    }
    public void setMeterDetails(String meterDetails) {
        this.meterDetails = meterDetails == null ? null : meterDetails.trim();
    }
    public Integer getElecPt() {
        return elecPt;
    }
    public void setElecPt(Integer elecPt) {
        this.elecPt = elecPt;
    }
    public Integer getElecCt() {
        return elecCt;
    }
    public void setElecCt(Integer elecCt) {
        this.elecCt = elecCt;
    }
    public String getBaudRate() {
        return baudRate;
    }
    public void setBaudRate(String baudRate) {
        this.baudRate = baudRate == null ? null : baudRate.trim();
    }
    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId == null ? null : roomId.trim();
    }
    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }
    public Byte getSubAccountStatus() {
        return subAccountStatus;
    }
    public void setSubAccountStatus(Byte subAccountStatus) {
        this.subAccountStatus = subAccountStatus;
    }
    public String getSubAccountId() {
        return subAccountId;
    }
    public void setSubAccountId(String subAccountId) {
        this.subAccountId = subAccountId == null ? null : subAccountId.trim();
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public String getPriceModeCode() {
        return priceModeCode;
    }
    public void setPriceModeCode(String priceModeCode) {
        this.priceModeCode = priceModeCode == null ? null : priceModeCode.trim();
    }
    public String getPriceId() {
        return priceId;
    }
    public void setPriceId(String priceId) {
        this.priceId = priceId == null ? null : priceId.trim();
    }
    public Byte getPriceStatus() {
        return priceStatus;
    }
    public void setPriceStatus(Byte priceStatus) {
        this.priceStatus = priceStatus;
    }
    public String getAlarmId() {
        return alarmId;
    }
    public void setAlarmId(String alarmId) {
        this.alarmId = alarmId == null ? null : alarmId.trim();
    }
    public Byte getAlarmStatus() {
        return alarmStatus;
    }
    public void setAlarmStatus(Byte alarmStatus) {
        this.alarmStatus = alarmStatus;
    }
    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public BigDecimal getStartRead() {
        return startRead;
    }
    public void setStartRead(BigDecimal startRead) {
        this.startRead = startRead;
    }
    public Date getBalanceUpdateTime() {
        return balanceUpdateTime;
    }
    public void setBalanceUpdateTime(Date balanceUpdateTime) {
        this.balanceUpdateTime = balanceUpdateTime;
    }
    public BigDecimal getBalanceUpdateRead() {
        return balanceUpdateRead;
    }
    public void setBalanceUpdateRead(BigDecimal balanceUpdateRead) {
        this.balanceUpdateRead = balanceUpdateRead;
    }
    public Integer getBalanceUpdateNo() {
        return balanceUpdateNo;
    }
    public void setBalanceUpdateNo(Integer balanceUpdateNo) {
        this.balanceUpdateNo = balanceUpdateNo;
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
    
    public String generateSubAccountId(){
    	return CommUtils.getUuid();
    }
    
	/**
	 * paas查询到的数据和输入数据拼接
	 * @param meter
	 * @param map
	 * @return
	 */
	public static Meter splicing(Meter meter, Map<String, Object> map) {
		String meterAddr = (String) map.get("equipNumber");
		if (!StringUtils.isEmpty(meterAddr)) {
			meter.setMeterAddr(meterAddr);
		}
		String meterName = (String) map.get("equipName");
		if (!StringUtils.isEmpty(meterName)) {
			meter.setMeterName(meterName);
		}
		String gatherHz = (String) map.get("gatherHz");
		if (!StringUtils.isEmpty(gatherHz)) {
			meter.setGatherHz(Integer.valueOf(gatherHz));
		}
		String netTypeCode = (String) map.get("netTypeCode");
		if (!StringUtils.isEmpty(netTypeCode)) {
			meter.setNetTypeCode(netTypeCode);
		}
		String isDirect = String.valueOf(map.get("isDirect"));
		if (!StringUtils.isEmpty(isDirect)) {
			int direct = Integer.valueOf(isDirect);
			meter.setIsDirect((byte) direct);
		}
		String netNumber = (String) map.get("netNumber");
		if (netNumber != "null" && !StringUtils.isEmpty(netNumber)) {
			meter.setNetNumber(netNumber);
		}
		String isRemoteControl = String.valueOf(map.get("isRemoteControl"));
		if (isRemoteControl != "null" && !StringUtils.isEmpty(isRemoteControl)) {
			int remoteControl = Integer.valueOf(isRemoteControl);
			meter.setIsRemoteControl((byte) remoteControl);
		}
		String isCostControl = String.valueOf(map.get("isCostControl"));
		if (isCostControl != "null" && !StringUtils.isEmpty(isCostControl)) {
			int costControl = Integer.valueOf(isCostControl);
			meter.setIsCostControl((byte) costControl);
		}
		String modelName = (String) map.get("modelName");
		if (!StringUtils.isEmpty(modelName)) {
			meter.setModelName(modelName);
		}
		String baudRate = (String) map.get("baudRate");
		if (!StringUtils.isEmpty(baudRate)) {
			meter.setBaudRate(baudRate);
		}
		String softVersoin = (String) map.get("softVersoin");
		if (!StringUtils.isEmpty(softVersoin)) {
			meter.setSoftVersoin(softVersoin);
		}
		String meterSpec = (String) map.get("meterSpec");
		if (!StringUtils.isEmpty(meterSpec)) {
			meter.setMeterSpec(meterSpec);
		}
		String energyType = (String) map.get("equipTypeCode");
		String meterType = "";
		if (!StringUtils.isEmpty(energyType)) {
			meter.setEnergyType(energyType);
			if (EnergyCategEum.ELEC.getValue().equals(energyType)) {
				meterType = (String) map.get("elecMeterTypeCode");
				meter.setMeterType(meterType);
			} else if (EnergyCategEum.WATER.getValue().equals(energyType)) {
				meterType = (String) map.get("equipTypeCode");
				meter.setMeterType(meterType);
			}
		}
		return meter;
	}
	public String getEquipNumber() {
		return equipNumber;
	}
	public void setEquipNumber(String equipNumber) {
		this.equipNumber = equipNumber;
	}
	public BigDecimal getReadValue() {
		return readValue;
	}
	public void setReadValue(BigDecimal readValue) {
		this.readValue = readValue;
	}
	public String getCommunicationType() {
		return communicationType;
	}
	public void setCommunicationType(String communicationType) {
		this.communicationType = communicationType;
	}
	public Date getReadTime() {
		return readTime;
	}
	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getCustomerRealname() {
		return customerRealname;
	}
	public void setCustomerRealname(String customerRealname) {
		this.customerRealname = customerRealname;
	}
	public String getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public BigDecimal getTotalActiveE() {
		return totalActiveE;
	}
	public void setTotalActiveE(BigDecimal totalActiveE) {
		this.totalActiveE = totalActiveE;
	}
	public Date getLastReadTime() {
		return lastReadTime;
	}
	public void setLastReadTime(Date lastReadTime) {
		this.lastReadTime = lastReadTime;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Byte getIsElecLoad() {
		return isElecLoad;
	}
	public void setIsElecLoad(Byte isElecLoad) {
		this.isElecLoad = isElecLoad;
	}
	public Byte getIsElecPowerMaxLimit() {
		return isElecPowerMaxLimit;
	}
	public void setIsElecPowerMaxLimit(Byte isElecPowerMaxLimit) {
		this.isElecPowerMaxLimit = isElecPowerMaxLimit;
	}
	public Byte getIsElecOnTime() {
		return isElecOnTime;
	}
	public void setIsElecOnTime(Byte isElecOnTime) {
		this.isElecOnTime = isElecOnTime;
	}
	public String getCurrentTimeLimitId() {
		return currentTimeLimitId;
	}
	public void setCurrentTimeLimitId(String currentTimeLimitId) {
		this.currentTimeLimitId = currentTimeLimitId;
	}
	public String getPowerLadderLimitId() {
		return powerLadderLimitId;
	}
	public void setPowerLadderLimitId(String powerLadderLimitId) {
		this.powerLadderLimitId = powerLadderLimitId;
	}
	public String getPowerFluctuationLimitId() {
		return powerFluctuationLimitId;
	}
	public void setPowerFluctuationLimitId(String powerFluctuationLimitId) {
		this.powerFluctuationLimitId = powerFluctuationLimitId;
	}
	public String getPowerChangeLimitId() {
		return powerChangeLimitId;
	}
	public void setPowerChangeLimitId(String powerChangeLimitId) {
		this.powerChangeLimitId = powerChangeLimitId;
	}
	public String getPowerMaxLimitId() {
		return powerMaxLimitId;
	}
	public void setPowerMaxLimitId(String powerMaxLimitId) {
		this.powerMaxLimitId = powerMaxLimitId;
	}
	public BigDecimal getPrice1() {
		return price1;
	}
	public void setPrice1(BigDecimal price1) {
		this.price1 = price1;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getRegionNumber() {
		return regionNumber;
	}
	public void setRegionNumber(String regionNumber) {
		this.regionNumber = regionNumber;
	}
	public String getRoomUse() {
		return roomUse;
	}
	public void setRoomUse(String roomUse) {
		this.roomUse = roomUse;
	}
	public String getCustomerMobile1() {
		return customerMobile1;
	}
	public void setCustomerMobile1(String customerMobile1) {
		this.customerMobile1 = customerMobile1;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
}