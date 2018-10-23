package cn.com.tw.saas.serv.entity.statistics;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 对应 表 t_statistics_meter_use_quantum
 * @author Administrator
 *
 */
public class MeterUseQuantum implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2943595455188861094L;

	private String id;

	/**
	 * 机构
	 */
    private String orgId;
  
    /**
     * 主回路止码
     */
    private BigDecimal  zReadValue;
    /**
     * 主回路起码
     */
    private BigDecimal  zPreReadValue;
    /**
     * 主回路用量
     */
    private BigDecimal zUseValue;
    
    /**
     * 副回路止码
     */
    private BigDecimal  viceReadValue;
    /**
     * 副回路起码
     */
    private BigDecimal  vicePreReadValue;
    /**
     * 副回路用量
     */
    private BigDecimal viceUseValue;
    
    /**
     * 第三回路止码
     */
   // private BigDecimal  thirdReadValue;
    /**
     * 第三回路起码
     */
   // private BigDecimal  thirdPreReadValue;
    /**
     * 第三回路用量
     */
   /* private BigDecimal thirdUseValue;*/
    
    /**
     * 总回路止码
     */
    private BigDecimal  totalReadValue;
    /**
     * 总回路起码
     */
    private BigDecimal  totalPreReadValue;
    /**
     * 总回路用量
     */
    private BigDecimal totalUseValue;
    
    /**
     * 房间类型，1701是商铺，1702是宿舍
     */
    private String roomUse;
    
    /**
     * 楼栋名称
     */
    private String regionFullName;
    
    
    private String regionId;
    
    public BigDecimal getzReadValue() {
		return zReadValue;
	}

	public void setzReadValue(BigDecimal zReadValue) {
		this.zReadValue = zReadValue;
	}

	public BigDecimal getzPreReadValue() {
		return zPreReadValue;
	}

	public void setzPreReadValue(BigDecimal zPreReadValue) {
		this.zPreReadValue = zPreReadValue;
	}

	public BigDecimal getzUseValue() {
		return zUseValue;
	}

	public void setzUseValue(BigDecimal zUseValue) {
		this.zUseValue = zUseValue;
	}

	public BigDecimal getViceReadValue() {
		return viceReadValue;
	}

	public void setViceReadValue(BigDecimal viceReadValue) {
		this.viceReadValue = viceReadValue;
	}

	public BigDecimal getVicePreReadValue() {
		return vicePreReadValue;
	}

	public void setVicePreReadValue(BigDecimal vicePreReadValue) {
		this.vicePreReadValue = vicePreReadValue;
	}

	public BigDecimal getViceUseValue() {
		return viceUseValue;
	}

	public void setViceUseValue(BigDecimal viceUseValue) {
		this.viceUseValue = viceUseValue;
	}

	/*public BigDecimal getThirdReadValue() {
		return thirdReadValue;
	}

	public void setThirdReadValue(BigDecimal thirdReadValue) {
		this.thirdReadValue = thirdReadValue;
	}

	public BigDecimal getThirdPreReadValue() {
		return thirdPreReadValue;
	}

	public void setThirdPreReadValue(BigDecimal thirdPreReadValue) {
		this.thirdPreReadValue = thirdPreReadValue;
	}*/

	/*public BigDecimal getThirdUseValue() {
		return thirdUseValue;
	}

	public void setThirdUseValue(BigDecimal thirdUseValue) {
		this.thirdUseValue = thirdUseValue;
	}*/

	public BigDecimal getTotalReadValue() {
		return totalReadValue;
	}

	public void setTotalReadValue(BigDecimal totalReadValue) {
		this.totalReadValue = totalReadValue;
	}

	public BigDecimal getTotalPreReadValue() {
		return totalPreReadValue;
	}

	public void setTotalPreReadValue(BigDecimal totalPreReadValue) {
		this.totalPreReadValue = totalPreReadValue;
	}

	public BigDecimal getTotalUseValue() {
		return totalUseValue;
	}

	public void setTotalUseValue(BigDecimal totalUseValue) {
		this.totalUseValue = totalUseValue;
	}

	private String orgName;

    /**
	 * 房间ID
	 */
    private String roomId;

    /**
	 * 房间编号
	 */
    private String roomNumber;

    /**
	 * 房间名称
	 */
    private String roomName;

    /**
	 * 仪表能源类型（字典code）
	 */
    private String meterCateg;

    /**
	 * 仪表类型(字典code)
	 */
    private String meterType;

    /**
	 * 仪表通讯地址
	 */
    private String meterAddr;

    /**
	 * 仪表名称
	 */
    private String meterAlias;

    /**
	 * 时标，格式是yyyyMMddhhmm
	 */
    private String freezeTd;
    
    /**
     * 导出开始时间
     */
    private String startFreezeTd;
    /**
     * 导出结束时间
     */
    private String endFreezeTd;

    /**
	 * 止码类型分为
            水流量
            组合有功电能
            正向有功电能
            反向有功电能
            组合无功电能1
            组合无功电能2
	 */
    private String valueType;

    /**
	 * 本点止码
	 */
    private BigDecimal readValue;

    /**
	 * 上点止码
	 */
    private BigDecimal preReadValue;

    /**
	 * 当前用量（=本点止码-上点止码）
	 */
    private BigDecimal useValue;

    /**
	 * 上期用量
	 */
    private BigDecimal preUseValue;

    /**
	 * 电表倍率=电压变比*电流变比  水表倍率=1
	 */
    private BigDecimal multiple;

    /**
	 * 费控模式
	 */
    private String controlType;

    /**
	 * 计价方式（字典code）
	 */
    private String priceType;

    /**
	 * 计价规则
	 */
    private String priceId;

    /**
	 * 客户号
	 */
    private String customerNo;

    /**
	 * 客户姓名
	 */
    private String customerName;
    
    /**
     * 从客户表（t_saas_customer）中关联出的客户名称
     */
    private String  customerRealName;

    /**
	 * 本期费用
	 */
    private BigDecimal useMoney;

    /**
	 * 创建时间
	 */
    private Date createTime;

    /**
	 * 修改时间
	 */
    private Date updateTime;
    
    /**
	 * 抄表时间
	 */
    private Date readTime;
    
    /**
     * day.日   month.月  year.年 时间类型
     */
    private String dateType;
    
    private String time;
    
    /**
     * 回路类型
		0，总回路
		1，主回路
		2，副回路
		3、第三回路
     */
    private Integer loopType;
    
    /**
     * LoopType的String转换类型
     */
    private String loopTypeStr;

    /**
     * 当前金额moneyNow=当前用量（=本点止码-上点止码）useValue*规则表中price1
     */
    private BigDecimal moneyNow;
    
    /**
     * 当前金额设置为String类型
     */
    private String moneyNowStr;
    
    /**
     * 规则单价
     */
    private BigDecimal price1;
    
    /**
     * 排序：1为正序  2为反序
     */
    private String orderType;
   
  //是否导出英文excel
    private String isEnExcel;
    
    
    public String getIsEnExcel() {
		return isEnExcel;
	}
	public void setIsEnExcel(String isEnExcel) {
		this.isEnExcel = isEnExcel;
	}
    
    public void setOrderType(String orderType) {
        this.orderType = orderType == null ? null : orderType.trim();
    }

    public String getOrderType() {
        return orderType;
    }
    public String getOrgName() {
		return orgName;
	}


	public void setOrgName(String orgName) {
		this.orgName = orgName == null ? null : orgName.trim();
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId == null ? null : roomId.trim();
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber == null ? null : roomNumber.trim();
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName == null ? null : roomName.trim();
    }

    public String getMeterCateg() {
        return meterCateg;
    }

    public void setMeterCateg(String meterCateg) {
        this.meterCateg = meterCateg == null ? null : meterCateg.trim();
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType == null ? null : meterType.trim();
    }

    public String getMeterAddr() {
        return meterAddr;
    }

    public void setMeterAddr(String meterAddr) {
        this.meterAddr = meterAddr == null ? null : meterAddr.trim();
    }

    public String getMeterAlias() {
        return meterAlias;
    }

    public void setMeterAlias(String meterAlias) {
        this.meterAlias = meterAlias == null ? null : meterAlias.trim();
    }

    public String getFreezeTd() {
        return freezeTd;
    }

    public void setFreezeTd(String freezeTd) {
        this.freezeTd = freezeTd == null ? null : freezeTd.trim();
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType == null ? null : valueType.trim();
    }

    public BigDecimal getReadValue() {
        return readValue;
    }

    public void setReadValue(BigDecimal readValue) {
        this.readValue = readValue;
    }

    public BigDecimal getPreReadValue() {
        return preReadValue;
    }

    public void setPreReadValue(BigDecimal preReadValue) {
        this.preReadValue = preReadValue;
    }

    public BigDecimal getUseValue() {
        return useValue;
    }

    public void setUseValue(BigDecimal useValue) {
        this.useValue = useValue;
    }

    public BigDecimal getPreUseValue() {
        return preUseValue;
    }

    public void setPreUseValue(BigDecimal preUseValue) {
        this.preUseValue = preUseValue;
    }

    public BigDecimal getMultiple() {
        return multiple;
    }

    public void setMultiple(BigDecimal multiple) {
        this.multiple = multiple;
    }

    public String getControlType() {
        return controlType;
    }

    public void setControlType(String controlType) {
        this.controlType = controlType == null ? null : controlType.trim();
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType == null ? null : priceType.trim();
    }

    public String getPriceId() {
        return priceId;
    }

    public void setPriceId(String priceId) {
        this.priceId = priceId == null ? null : priceId.trim();
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo == null ? null : customerNo.trim();
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
    }

    public BigDecimal getUseMoney() {
        return useMoney;
    }

    public void setUseMoney(BigDecimal useMoney) {
        this.useMoney = useMoney;
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

	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getLoopType() {
		return loopType;
	}



	public String getLoopTypeStr() {
		return loopTypeStr;
	}


	public void setLoopTypeStr(String loopTypeStr) {
		this.loopTypeStr = loopTypeStr;
	}
	
	


	public void setLoopType(Integer loopType) {
		this.loopType = loopType;
	}


	public BigDecimal getMoneyNow() {
		return moneyNow;
	}


	public void setMoneyNow(BigDecimal moneyNow) {
		this.moneyNow = moneyNow;
	}


	public BigDecimal getPrice1() {
		return price1;
	}


	public void setPrice1(BigDecimal price1) {
		this.price1 = price1;
	}


	public String getStartFreezeTd() {
		return startFreezeTd;
	}


	public void setStartFreezeTd(String startFreezeTd) {
		this.startFreezeTd = startFreezeTd;
	}


	public String getEndFreezeTd() {
		return endFreezeTd;
	}


	public void setEndFreezeTd(String endFreezeTd) {
		this.endFreezeTd = endFreezeTd;
	}


	public String getMoneyNowStr() {
		return moneyNowStr;
	}


	public void setMoneyNowStr(String moneyNowStr) {
		this.moneyNowStr = moneyNowStr;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getCustomerRealName() {
		return customerRealName;
	}

	public void setCustomerRealName(String customerRealName) {
		this.customerRealName = customerRealName;
	}

	public String getRoomUse() {
		return roomUse;
	}

	public void setRoomUse(String roomUse) {
		this.roomUse = roomUse;
	}

	public String getRegionFullName() {
		return regionFullName;
	}

	public void setRegionFullName(String regionFullName) {
		this.regionFullName = regionFullName;
	}


}