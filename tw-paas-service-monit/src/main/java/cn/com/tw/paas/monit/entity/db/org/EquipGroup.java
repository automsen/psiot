package cn.com.tw.paas.monit.entity.db.org;

import java.io.Serializable;
import java.util.Date;

/**
 * 对应数据库表   t_org_equip_collect_conf 
 * @author Administrator
 *
 */
public class EquipGroup implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7478059384175412824L;

	/**
	 * 主键ID
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
     * 通信设备地址
     */
    private String commAddr;

    /**
     * 分组号
     */
    private String groupNo;

    /**
     * 下联设备地址
     */
    private String childCommAddr;

    /**
     * 下联设备配置状态
            0：已配置未下发
            1：已配置已下发
            2：未配置已下发
     */
    private Byte childStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 跟新时间
     */
    private Date updateTime;
    
    private Integer sectors;
    
    //扩展字段
    /**
     * 页面模糊查询字段
     */
    private String childCommAddrSearch;
    
    /**
     * 上行网络类型
     */
    private String netTypeCode;
    /**
     * 网络编号
     */
    private String netNumber;
    
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

    public String getCommAddr() {
        return commAddr;
    }

    public void setCommAddr(String commAddr) {
        this.commAddr = commAddr == null ? null : commAddr.trim();
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo == null ? null : groupNo.trim();
    }

    public String getChildCommAddr() {
        return childCommAddr;
    }

    public void setChildCommAddr(String childCommAddr) {
        this.childCommAddr = childCommAddr == null ? null : childCommAddr.trim();
    }

    public Byte getChildStatus() {
        return childStatus;
    }

    public void setChildStatus(Byte childStatus) {
        this.childStatus = childStatus;
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

	public Integer getSectors() {
		return sectors;
	}

	public void setSectors(Integer sectors) {
		this.sectors = sectors;
	}

	public String getChildCommAddrSearch() {
		return childCommAddrSearch;
	}

	public void setChildCommAddrSearch(String childCommAddrSearch) {
		this.childCommAddrSearch = childCommAddrSearch;
	}

	public String getNetTypeCode() {
		return netTypeCode;
	}

	public void setNetTypeCode(String netTypeCode) {
		this.netTypeCode = netTypeCode;
	}

	public String getNetNumber() {
		return netNumber;
	}

	public void setNetNumber(String netNumber) {
		this.netNumber = netNumber;
	}
}