package cn.com.tw.saas.serv.entity.db.read;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 最近读数
 * 数据库实体
 * 对应t_read_last
 */
public class ReadLast implements Serializable{

    private static final long serialVersionUID = -3518449663675446979L;

	/**
     * 主键
     */
    private String id;

    /**
     * 机构ID(外键关联机构信息表)
     */
    private String orgId;

    /**
     * 仪表能源类型（字典code）
     */
    private String meterType;
    
    private String elecMeterType;

    /**
     * 仪表通讯地址
     */
    private String meterAddr;

    /**
     * 数据项编码（英文）
     */
    private String itemCode;
    
    private String itemName;

    private BigDecimal readValue;

    /**
     * 旧表读数与余额是否人工读取
            0，系统获取
            1，人工读取
     */
    private Byte isManual;

    /**
     * 读取时间
     */
    private Date readTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    
    /**
     * 回路类型
		0，总回路
		1，主回路
		2，副回路
		3、第三回路
     */
    private Integer loopType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getMeterType() {
		return meterType;
	}

	public void setMeterType(String meterType) {
		this.meterType = meterType;
	}

	public String getElecMeterType() {
		return elecMeterType;
	}

	public void setElecMeterType(String elecMeterType) {
		this.elecMeterType = elecMeterType;
	}

	public String getMeterAddr() {
		return meterAddr;
	}

	public void setMeterAddr(String meterAddr) {
		this.meterAddr = meterAddr;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public BigDecimal getReadValue() {
		return readValue;
	}

	public void setReadValue(BigDecimal readValue) {
		this.readValue = readValue;
	}

	public Byte getIsManual() {
		return isManual;
	}

	public void setIsManual(Byte isManual) {
		this.isManual = isManual;
	}

	public Date getReadTime() {
		return readTime;
	}

	public void setReadTime(Date readTime) {
		this.readTime = readTime;
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

	public Integer getLoopType() {
		return loopType;
	}

	public void setLoopType(Integer loopType) {
		this.loopType = loopType;
	}
    
}