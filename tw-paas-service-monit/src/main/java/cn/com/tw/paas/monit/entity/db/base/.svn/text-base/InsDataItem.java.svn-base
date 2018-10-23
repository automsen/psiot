package cn.com.tw.paas.monit.entity.db.base;

import java.io.Serializable;

/**
 * 指令——数据项关系
 * 数据库实体
 * 对应t_base_ins_data_item
 */
public class InsDataItem implements Serializable {

    private static final long serialVersionUID = -4590658993692455948L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 指令id
     */
    private String insId;

    /**
     * 指令内序号
     */
    private Integer itemIndex;

    /**
     * 存储到数据库的字段序号value1~value6
     */
    private Integer saveIndex;

    /**
     * 数据项编码（英文）
     */
    private String itemCode;
    
    /**
     * 数据项短Code
     */
    private String itemShortCode;
    
    /*以下为扩展字段*/
    /**
     * 数据项名称(中文)
     */
    private String itemName;
    
    /**
     * 是否乘电流变比
     */
    private Byte isMultiplyCt;

    /**
     * 是否乘电压变比
     */
    private Byte isMultiplyPt;
    
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getInsId() {
        return insId;
    }
    public void setInsId(String insId) {
        this.insId = insId == null ? null : insId.trim();
    }
    public Integer getItemIndex() {
        return itemIndex;
    }
    public void setItemIndex(Integer itemIndex) {
        this.itemIndex = itemIndex;
    }
    public Integer getSaveIndex() {
        return saveIndex;
    }
    public void setSaveIndex(Integer saveIndex) {
        this.saveIndex = saveIndex;
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
		this.itemName = itemName;
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
	public String getItemShortCode() {
		return itemShortCode;
	}
	public void setItemShortCode(String itemShortCode) {
		this.itemShortCode = itemShortCode;
	}
	@Override
	public String toString() {
		return "InsDataItem [id=" + id + ", insId=" + insId + ", itemIndex="
				+ itemIndex + ", saveIndex=" + saveIndex + ", itemCode="
				+ itemCode + ", itemShortCode=" + itemShortCode + ", itemName="
				+ itemName + ", isMultiplyCt=" + isMultiplyCt
				+ ", isMultiplyPt=" + isMultiplyPt + "]";
	}

}