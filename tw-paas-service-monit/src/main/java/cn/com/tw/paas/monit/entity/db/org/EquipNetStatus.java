package cn.com.tw.paas.monit.entity.db.org;

import java.io.Serializable;
import java.util.Date;

/**
 * 对应数据库表   t_org_equip_net_status
 * @author Administrator
 *
 */
public class EquipNetStatus implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9185970077895262423L;

	/**
	 * 主键ID
	 */
    private String id;

    /**
     * 通信地址
     */
    private String commAddr;

    /**
     * 网络状态
            0，离线
            1，在线
     */
    private Byte netStatus;

    /**
     * 最近在线时间
     */
    private Date onlineTime;

    /**
     * 最近离线时间
     */
    private Date offlineTime;
    
    /**
     * 失联次数
     */
    private int lossOfCommNum;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCommAddr() {
        return commAddr;
    }

    public void setCommAddr(String commAddr) {
        this.commAddr = commAddr == null ? null : commAddr.trim();
    }

    public Byte getNetStatus() {
        return netStatus;
    }

    public void setNetStatus(Byte netStatus) {
        this.netStatus = netStatus;
    }

    public Date getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Date getOfflineTime() {
        return offlineTime;
    }

    public void setOfflineTime(Date offlineTime) {
        this.offlineTime = offlineTime;
    }

	public int getLossOfCommNum() {
		return lossOfCommNum;
	}

	public void setLossOfCommNum(int lossOfCommNum) {
		this.lossOfCommNum = lossOfCommNum;
	}
}