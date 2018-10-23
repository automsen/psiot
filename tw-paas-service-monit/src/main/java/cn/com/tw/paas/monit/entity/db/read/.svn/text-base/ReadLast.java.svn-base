package cn.com.tw.paas.monit.entity.db.read;

import java.io.Serializable;
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
     * 机构id
     */
    private String orgId;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 仪表分类（字典code）
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

    /**
     * 数据项名称(中文)
     */
    private String itemName;

    /**
     * 读数
     */
    private String readValue;

    /**
     * 是否推送成功
     */
    private Byte isPush;

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
    public String getAppId() {
        return appId;
    }
    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }
    public String getMeterType() {
        return meterType;
    }
    public void setMeterType(String meterType) {
        this.meterType = meterType == null ? null : meterType.trim();
    }
    public String getElecMeterType() {
        return elecMeterType;
    }
    public void setElecMeterType(String elecMeterType) {
        this.elecMeterType = elecMeterType == null ? null : elecMeterType.trim();
    }
    public String getMeterAddr() {
        return meterAddr;
    }
    public void setMeterAddr(String meterAddr) {
        this.meterAddr = meterAddr == null ? null : meterAddr.trim();
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode == null ? null : itemCode.trim();
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName == null ? null : itemName.trim();
    }
    public String getReadValue() {
        return readValue;
    }
    public void setReadValue(String readValue) {
        this.readValue = readValue == null ? null : readValue.trim();
    }
    public Byte getIsPush() {
        return isPush;
    }
    public void setIsPush(Byte isPush) {
        this.isPush = isPush;
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

	@Override
	public String toString() {
		return "ReadLast [id=" + id + ", orgId=" + orgId + ", appId=" + appId + ", meterType=" + meterType
				+ ", elecMeterType=" + elecMeterType + ", meterAddr=" + meterAddr + ", itemCode=" + itemCode
				+ ", itemName=" + itemName + ", readValue=" + readValue + ", isPush=" + isPush + ", readTime="
				+ readTime + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
	
}