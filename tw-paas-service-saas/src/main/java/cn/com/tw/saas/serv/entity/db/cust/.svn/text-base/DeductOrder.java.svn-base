/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.entity.db.cust;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 系统扣费订单（仅限系统计费）
 * 数据库实体
 * 对应t_saas_deduct_order
 */
public class DeductOrder {

    /**
     * 主键
     */
    private String orderId;

    /**
     * 机构id
     */
    private String orgId;

    /**
     * 房间id
     */
    private String roomId;

    /**
     * 房间账户id
     */
    private String roomAccountId;

    /**
     * 子账户id
     */
    private String subAccountId;

    /**
     * 能源类型
     */
    private String energyType;

    /**
     * 仪表地址
     */
    private String meterAddr;

    /**
     * 扣费类型
            系统计费自动扣费;
            系统计费主动扣费;
            系统计费人工扣费;
     */
    private String deductType;

    /**
     * 操作者id，仅记录人工扣费的操作者
     */
    private String operatorId;

    /**
     * 应扣费金额
     */
    private BigDecimal deductMoney;

    /**
     * 扣费时间
     */
    private Date deductTime;

    private Integer deductNo;

    /**
     * 计费方式
     */
    private String priceModeCode;

    /**
     * 历史计价规则
     */
    private String priceId;

    /**
     * 1，表示计价变更，或阶梯计价的周期结束
            0，表示计价不变，或阶梯计价的周期未结束
            为1时，下次扣费重置阶梯累计值
     */
    private Byte isPriceOver;

    /**
     * 扣费时读数
     */
    private BigDecimal readValue;

    /**
     * 总扣费用量
     */
    private BigDecimal useValue;

    /**
     * 读数1(尖),用于分时计价
     */
    private BigDecimal readValue1;

    /**
     * 读数2(峰),用于分时计价
     */
    private BigDecimal readValue2;

    /**
     * 读数3(平)
,用于分时计价
     */
    private BigDecimal readValue3;

    /**
     * 读数4(谷)
,用于分时计价
     */
    private BigDecimal readValue4;

    /**
     * 阶梯1累计用量,用于阶梯计价
     */
    private BigDecimal stepValue1;

    /**
     * 阶梯2累计用量,用于阶梯计价
     */
    private BigDecimal stepValue2;

    /**
     * 阶梯3累计用量,用于阶梯计价
     */
    private BigDecimal stepValue3;

    /**
     * 阶梯4累计用量,用于阶梯计价
     */
    private BigDecimal stepValue4;

    /**
     * 本次扣费用量1
            对应尖或者阶梯1
     */
    private BigDecimal useValue1;

    /**
     * 本次扣费用量2
            对应峰或者阶梯2
     */
    private BigDecimal useValue2;

    /**
     * 本次扣费用量3
            对应平或者阶梯3
     */
    private BigDecimal useValue3;

    /**
     * 本次扣费用量4
            对应谷或者阶梯4
     */
    private BigDecimal useValue4;

    /**
     * 订单状态:
            0,初始;
            1,成功;
            2,失败;
            3,超时;
     */
    private Byte status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 结算的时候预览的曾经单价
     */
    private String price;
    /**
     * 结算时候预览的结算记录列表中的生效日期
     */
    private Date sTime;
    /**
     * 结算时候预览的结算记录列表中的生效日期
     */
    private Date eTime;
    
    
    public Date getsTime() {
		return sTime;
	}
	public void setsTime(Date sTime) {
		this.sTime = sTime;
	}
	public Date geteTime() {
		return eTime;
	}
	public void seteTime(Date eTime) {
		this.eTime = eTime;
	}
	public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
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
    public String getRoomAccountId() {
        return roomAccountId;
    }
    public void setRoomAccountId(String roomAccountId) {
        this.roomAccountId = roomAccountId == null ? null : roomAccountId.trim();
    }
    public String getSubAccountId() {
        return subAccountId;
    }
    public void setSubAccountId(String subAccountId) {
        this.subAccountId = subAccountId == null ? null : subAccountId.trim();
    }
    public String getEnergyType() {
        return energyType;
    }
    public void setEnergyType(String energyType) {
        this.energyType = energyType == null ? null : energyType.trim();
    }
    public String getMeterAddr() {
        return meterAddr;
    }
    public void setMeterAddr(String meterAddr) {
        this.meterAddr = meterAddr == null ? null : meterAddr.trim();
    }
    public String getDeductType() {
        return deductType;
    }
    public void setDeductType(String deductType) {
        this.deductType = deductType == null ? null : deductType.trim();
    }
    public String getOperatorId() {
        return operatorId;
    }
    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId == null ? null : operatorId.trim();
    }
    public BigDecimal getDeductMoney() {
        return deductMoney;
    }
    public void setDeductMoney(BigDecimal deductMoney) {
        this.deductMoney = deductMoney;
    }
    public Date getDeductTime() {
        return deductTime;
    }
    public void setDeductTime(Date deductTime) {
        this.deductTime = deductTime;
    }
    public Integer getDeductNo() {
        return deductNo;
    }
    public void setDeductNo(Integer deductNo) {
        this.deductNo = deductNo;
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
    public Byte getIsPriceOver() {
        return isPriceOver;
    }
    public void setIsPriceOver(Byte isPriceOver) {
        this.isPriceOver = isPriceOver;
    }
    public BigDecimal getReadValue() {
        return readValue;
    }
    public void setReadValue(BigDecimal readValue) {
        this.readValue = readValue;
    }
    public BigDecimal getUseValue() {
        return useValue;
    }
    public void setUseValue(BigDecimal useValue) {
        this.useValue = useValue;
    }
    public BigDecimal getReadValue1() {
        return readValue1;
    }
    public void setReadValue1(BigDecimal readValue1) {
        this.readValue1 = readValue1;
    }
    public BigDecimal getReadValue2() {
        return readValue2;
    }
    public void setReadValue2(BigDecimal readValue2) {
        this.readValue2 = readValue2;
    }
    public BigDecimal getReadValue3() {
        return readValue3;
    }
    public void setReadValue3(BigDecimal readValue3) {
        this.readValue3 = readValue3;
    }
    public BigDecimal getReadValue4() {
        return readValue4;
    }
    public void setReadValue4(BigDecimal readValue4) {
        this.readValue4 = readValue4;
    }
    public BigDecimal getStepValue1() {
        return stepValue1;
    }
    public void setStepValue1(BigDecimal stepValue1) {
        this.stepValue1 = stepValue1;
    }
    public BigDecimal getStepValue2() {
        return stepValue2;
    }
    public void setStepValue2(BigDecimal stepValue2) {
        this.stepValue2 = stepValue2;
    }
    public BigDecimal getStepValue3() {
        return stepValue3;
    }
    public void setStepValue3(BigDecimal stepValue3) {
        this.stepValue3 = stepValue3;
    }
    public BigDecimal getStepValue4() {
        return stepValue4;
    }
    public void setStepValue4(BigDecimal stepValue4) {
        this.stepValue4 = stepValue4;
    }
    public BigDecimal getUseValue1() {
        return useValue1;
    }
    public void setUseValue1(BigDecimal useValue1) {
        this.useValue1 = useValue1;
    }
    public BigDecimal getUseValue2() {
        return useValue2;
    }
    public void setUseValue2(BigDecimal useValue2) {
        this.useValue2 = useValue2;
    }
    public BigDecimal getUseValue3() {
        return useValue3;
    }
    public void setUseValue3(BigDecimal useValue3) {
        this.useValue3 = useValue3;
    }
    public BigDecimal getUseValue4() {
        return useValue4;
    }
    public void setUseValue4(BigDecimal useValue4) {
        this.useValue4 = useValue4;
    }
    public Byte getStatus() {
        return status;
    }
    public void setStatus(Byte status) {
        this.status = status;
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
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
}