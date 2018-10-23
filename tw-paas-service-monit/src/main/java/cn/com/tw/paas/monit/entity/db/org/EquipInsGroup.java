package cn.com.tw.paas.monit.entity.db.org;

import java.io.Serializable;
import java.util.Date;

/**
 * 对应数据库表 t_org_equip_collect_ins_conf
 * @author Administrator
 *
 */
public class EquipInsGroup implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6140598372180184233L;

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
     * 指令数据标识
     */
    private String dataMarker;

    /**
     * 指令配置状态
            0：已配置未下发
            1：已配置已下发
            2：未配置已下发
     */
    private Byte insStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 跟新时间
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

    public String getDataMarker() {
        return dataMarker;
    }

    public void setDataMarker(String dataMarker) {
        this.dataMarker = dataMarker == null ? null : dataMarker.trim();
    }

    public Byte getInsStatus() {
        return insStatus;
    }

    public void setInsStatus(Byte insStatus) {
        this.insStatus = insStatus;
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
}