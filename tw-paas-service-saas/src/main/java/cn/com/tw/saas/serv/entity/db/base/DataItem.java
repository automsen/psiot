package cn.com.tw.saas.serv.entity.db.base;

import java.io.Serializable;

/**
 * 采集数据项
 * 数据库实体
 * 对应t_base_data_item
 */
public class DataItem implements Serializable{

	private static final long serialVersionUID = 7439391215911792936L;

	/**
     * 主键
     */
    private Integer id;

    /**
     * 数据项类别
     */
    private String itemType;

    /**
     * 数据项编码（英文）
     */
    private String itemCode;

    /**
     * 数据项名称(中文)
     */
    private String itemName;

    private String unit;

    /**
     * 总位数
     */
    private Integer totalPlace;

    /**
     * 小数位数
     */
    private Integer decimalPlace;

    /**
     * 格式
     */
    private String format;

    /**
     * 是否乘电流变比
     */
    private Byte isMultiplyCt;

    /**
     * 是否乘电压变比
     */
    private Byte isMultiplyPt;

    /**
     * 可否读写
	1、只读
	2、读写
	3、只写
     */
    private Byte readOrWrite;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getItemType() {
        return itemType;
    }
    public void setItemType(String itemType) {
        this.itemType = itemType == null ? null : itemType.trim();
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
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit == null ? null : unit.trim();
    }
    public Integer getTotalPlace() {
        return totalPlace;
    }
    public void setTotalPlace(Integer totalPlace) {
        this.totalPlace = totalPlace;
    }
    public Integer getDecimalPlace() {
        return decimalPlace;
    }
    public void setDecimalPlace(Integer decimalPlace) {
        this.decimalPlace = decimalPlace;
    }
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format == null ? null : format.trim();
    }
    public Byte getIsMultiplyCt() {
        return isMultiplyCt;
    }
    public void setIsMultiplyCt(Byte isMultiplyCt) {
        this.isMultiplyCt = isMultiplyCt;
    }
    public Byte getIsMultiplyPt() {
        return isMultiplyPt;
    }
    public void setIsMultiplyPt(Byte isMultiplyPt) {
        this.isMultiplyPt = isMultiplyPt;
    }
    public Byte getReadOrWrite() {
        return readOrWrite;
    }
    public void setReadOrWrite(Byte readOrWrite) {
        this.readOrWrite = readOrWrite;
    }
	@Override
	public String toString() {
		return "DataItem [id=" + id + ", itemType=" + itemType + ", itemCode=" + itemCode + ", itemName=" + itemName
				+ ", unit=" + unit + ", totalPlace=" + totalPlace + ", decimalPlace=" + decimalPlace + ", format="
				+ format + ", isMultiplyCt=" + isMultiplyCt + ", isMultiplyPt=" + isMultiplyPt + ", readOrWrite="
				+ readOrWrite + "]";
	}

}