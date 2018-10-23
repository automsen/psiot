package cn.com.tw.saas.serv.entity.business.cust;

import java.math.BigDecimal;
import java.util.Date;

import cn.com.tw.saas.serv.entity.db.cust.DeductOrder;
/**
 * 系统扣费订单计算参数（仅限系统计费）
 * 业务实体
 */
public class DeductOrderParams extends DeductOrder{

	/**
     * 关联时读数
     */
    private BigDecimal startRead;
    
	private Date startTime;
    
    /**
     * 上次扣费时读数
     */
    private BigDecimal lastReadValue;

    /**
     * 上次扣费时间
     */
    private Date lastDeductTime;
    /**
     * 读数1(尖),用于分时计价
     */
    private BigDecimal lastReadValue1;

    /**
     * 读数2(峰),用于分时计价
     */
    private BigDecimal lastReadValue2;

    /**
     * 读数3(平),用于分时计价
     */
    private BigDecimal lastReadValue3;

    /**
     * 读数4(谷),用于分时计价
     */
    private BigDecimal lastReadValue4;

    /**
     * 阶梯1累计用量,用于阶梯计价
     */
    private BigDecimal lastStepValue1;

    /**
     * 阶梯2累计用量,用于阶梯计价
     */
    private BigDecimal lastStepValue2;

    /**
     * 阶梯3累计用量,用于阶梯计价
     */
    private BigDecimal lastStepValue3;

    /**
     * 阶梯4累计用量,用于阶梯计价
     */
    private BigDecimal lastStepValue4;

    /**
     * 1，表示计价变更，或阶梯计价的周期结束
            0，表示计价不变，或阶梯计价的周期未结束
            为1时，下次扣费重置阶梯累计值
     */
    private Byte lastIsPriceOver;
    /**
     * 计算后倍率
     */
    private BigDecimal multiple;
	public BigDecimal getStartRead() {
		return startRead;
	}
	public void setStartRead(BigDecimal startRead) {
		this.startRead = startRead;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public BigDecimal getLastReadValue() {
		return lastReadValue;
	}
	public void setLastReadValue(BigDecimal lastReadValue) {
		this.lastReadValue = lastReadValue;
	}
	public Date getLastDeductTime() {
		return lastDeductTime;
	}
	public void setLastDeductTime(Date lastDeductTime) {
		this.lastDeductTime = lastDeductTime;
	}
	public BigDecimal getLastReadValue1() {
		return lastReadValue1;
	}
	public void setLastReadValue1(BigDecimal lastReadValue1) {
		this.lastReadValue1 = lastReadValue1;
	}
	public BigDecimal getLastReadValue2() {
		return lastReadValue2;
	}
	public void setLastReadValue2(BigDecimal lastReadValue2) {
		this.lastReadValue2 = lastReadValue2;
	}
	public BigDecimal getLastReadValue3() {
		return lastReadValue3;
	}
	public void setLastReadValue3(BigDecimal lastReadValue3) {
		this.lastReadValue3 = lastReadValue3;
	}
	public BigDecimal getLastReadValue4() {
		return lastReadValue4;
	}
	public void setLastReadValue4(BigDecimal lastReadValue4) {
		this.lastReadValue4 = lastReadValue4;
	}
	public BigDecimal getLastStepValue1() {
		return lastStepValue1;
	}
	public void setLastStepValue1(BigDecimal lastStepValue1) {
		this.lastStepValue1 = lastStepValue1;
	}
	public BigDecimal getLastStepValue2() {
		return lastStepValue2;
	}
	public void setLastStepValue2(BigDecimal lastStepValue2) {
		this.lastStepValue2 = lastStepValue2;
	}
	public BigDecimal getLastStepValue3() {
		return lastStepValue3;
	}
	public void setLastStepValue3(BigDecimal lastStepValue3) {
		this.lastStepValue3 = lastStepValue3;
	}
	public BigDecimal getLastStepValue4() {
		return lastStepValue4;
	}
	public void setLastStepValue4(BigDecimal lastStepValue4) {
		this.lastStepValue4 = lastStepValue4;
	}
	public Byte getLastIsPriceOver() {
		return lastIsPriceOver;
	}
	public void setLastIsPriceOver(Byte lastIsPriceOver) {
		this.lastIsPriceOver = lastIsPriceOver;
	}
	public BigDecimal getMultiple() {
		return multiple;
	}
	public void setMultiple(BigDecimal multiple) {
		this.multiple = multiple;
	}
}
