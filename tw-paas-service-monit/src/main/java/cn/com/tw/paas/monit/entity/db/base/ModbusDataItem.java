/**
 * TODO: complete the comment
 */
package cn.com.tw.paas.monit.entity.db.base;

/**
 * TODO: complete the comment
 */
public class ModbusDataItem {

    private Integer id;

    /**
     * 生产厂家
     */
    private String manufacturerCode;

    /**
     * 功能码
     */
    private String function;

    /**
     * 寄存器地址
     */
    private String registerAddr;

    /**
     * 寄存器长度（字符数）
     */
    private Integer registerLength;

    private String itemCode;

    private String itemShortCode;

    /**
     * 数据项名称(中文)
     */
    private String itemName;

    /**
     * 小数位数
     */
    private Integer decimalPlace;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getManufacturerCode() {
        return manufacturerCode;
    }
    public void setManufacturerCode(String manufacturerCode) {
        this.manufacturerCode = manufacturerCode == null ? null : manufacturerCode.trim();
    }
    public String getFunction() {
        return function;
    }
    public void setFunction(String function) {
        this.function = function == null ? null : function.trim();
    }
    public String getRegisterAddr() {
        return registerAddr;
    }
    public void setRegisterAddr(String registerAddr) {
        this.registerAddr = registerAddr == null ? null : registerAddr.trim();
    }
    public Integer getRegisterLength() {
        return registerLength;
    }
    public void setRegisterLength(Integer registerLength) {
        this.registerLength = registerLength;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode == null ? null : itemCode.trim();
    }
    public String getItemShortCode() {
		return itemShortCode;
	}
	public void setItemShortCode(String itemShortCode) {
		this.itemShortCode = itemShortCode;
	}
	public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName == null ? null : itemName.trim();
    }
    public Integer getDecimalPlace() {
        return decimalPlace;
    }
    public void setDecimalPlace(Integer decimalPlace) {
        this.decimalPlace = decimalPlace;
    }
}