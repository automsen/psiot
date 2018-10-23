package cn.com.tw.paas.monit.entity.db.base;

import java.io.Serializable;
import java.util.List;

/**
 * 指令（暂只支持645协议）
 * 数据库实体
 * 对应t_base_ins
 */
public class Ins implements Serializable{

    private static final long serialVersionUID = 2187854421769274118L;

    /**
     * 主键
     */
    private String id;

    /**
     * 控制码
     */
    private String controlCode;

    /**
     * 数据标识
     */
    private String dataMarker;

    /**
     * 软件版本
     */
    private String softVersion;

    /**
     * 指令名称(中文)
     */
    private String insName;

    /**
     * 是否曲线指令
            0，非曲线指令
            1，曲线指令
     */
    private Byte isCurve;

    /**
     * 下发参数个数
     */
    private Integer paramNum;

    /**
     * 返回参数个数
     */
    private Integer returnNum;
    /**
     *  指令下发重试次数
     */
    private Integer retryNum;

    private String format;

    private String length;
    
    /**
     * 关联关系
     */
    private List<InsDataItem> items;
    
    private Integer itemIndex;
    
    
    private Byte needPassword;
    
    private String otherMarker;
    
    
    

    public Integer getRetryNum() {
		return retryNum;
	}
	public void setRetryNum(Integer retryNum) {
		this.retryNum = retryNum;
	}
	public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }
    public String getControlCode() {
        return controlCode;
    }
    public void setControlCode(String controlCode) {
        this.controlCode = controlCode == null ? null : controlCode.trim();
    }
    public String getDataMarker() {
        return dataMarker;
    }
    public void setDataMarker(String dataMarker) {
        this.dataMarker = dataMarker == null ? null : dataMarker.trim();
    }
    public String getSoftVersion() {
        return softVersion;
    }
    public void setSoftVersion(String softVersion) {
        this.softVersion = softVersion == null ? null : softVersion.trim();
    }
    public String getInsName() {
        return insName;
    }
    public void setInsName(String insName) {
        this.insName = insName == null ? null : insName.trim();
    }
    public Byte getIsCurve() {
        return isCurve;
    }
    public void setIsCurve(Byte isCurve) {
        this.isCurve = isCurve;
    }
    public Integer getParamNum() {
        return paramNum;
    }
    public void setParamNum(Integer paramNum) {
        this.paramNum = paramNum;
    }
    public Integer getReturnNum() {
        return returnNum;
    }
    public void setReturnNum(Integer returnNum) {
        this.returnNum = returnNum;
    }
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format == null ? null : format.trim();
    }
    public String getLength() {
        return length;
    }
    public void setLength(String length) {
        this.length = length == null ? null : length.trim();
    }
	public List<InsDataItem> getItems() {
		return items;
	}
	public void setItems(List<InsDataItem> items) {
		this.items = items;
	}
	
	
	public Integer getItemIndex() {
		return itemIndex;
	}
	public void setItemIndex(Integer itemIndex) {
		this.itemIndex = itemIndex;
	}
	
	
	public Byte getNeedPassword() {
		return needPassword;
	}
	public void setNeedPassword(Byte needPassword) {
		this.needPassword = needPassword;
	}
	public String getOtherMarker() {
		return otherMarker;
	}
	public void setOtherMarker(String otherMarker) {
		this.otherMarker = otherMarker;
	}
	@Override
	public String toString() {
		return "Ins [id=" + id + ", controlCode=" + controlCode + ", dataMarker=" + dataMarker + ", softVersion="
				+ softVersion + ", insName=" + insName + ", isCurve=" + isCurve + ", paramNum=" + paramNum
				+ ", returnNum=" + returnNum + ", format=" + format + ", length=" + length + "]";
	}
}