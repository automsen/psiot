package cn.com.tw.paas.monit.entity.db.org;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.com.tw.paas.monit.service.inn.callback.CmdCallback;

public class CmdExe implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2703511628185439490L;

	private String id;

    private String orgId;

    private String appId;
    
    private String businessNo;

    private String cmdCode;

    private String cmdName;

    private Integer cmdLevel;

    private String connAddr;

    private String param;

    private String returnValue;

    private Integer innNum;

    private Byte status;

    private Integer useTime;

    private Date createTime;

    private Date updateTime;
    
    private List<InsExe> ins;
    
    private String commPwd;
    
/*    private OrgApplication application;
    
    private TerminalEquip terminalEquip;
    
    private NetEquip netEquip;
*/    
    private InsExe currIns;
    
    private Map<String,Object> pushData ;
    
    
    private CmdCallback callback;
    
    private Map<String,Object> requestParams;
    
    private String cmdType;
    
    private Integer handleTimes;
    
    private Integer limitHandleTimes;
    
    private Byte cmdBlock;
    
    /**
     *  查询条件。和createTime 做对比
     */
    private Integer delayMinute;
    // 特殊查询条件，会混合判断是否执行完毕查询
    private String canStartQuery;
    
    private String[] statusNotIn;
    
    
    
    
	public String[] getStatusNotIn() {
		return statusNotIn;
	}

	public void setStatusNotIn(String[] statusNotIn) {
		this.statusNotIn = statusNotIn;
	}

	public String getCanStartQuery() {
		return canStartQuery;
	}

	public void setCanStartQuery(String canStartQuery) {
		this.canStartQuery = canStartQuery;
	}

	public Integer getDelayMinute() {
		return delayMinute;
	}

	public void setDelayMinute(Integer delayMinute) {
		this.delayMinute = delayMinute;
	}

	public Byte getCmdBlock() {
		return cmdBlock;
	}

	public void setCmdBlock(Byte cmdBlock) {
		this.cmdBlock = cmdBlock;
	}

	public Map<String, Object> getPushData() {
		return pushData;
	}

	public void setPushData(Map<String, Object> pushData) {
		this.pushData = pushData;
	}

	/*public OrgApplication getApplication() {
		return application;
	}*/

	/*public void setApplication(OrgApplication application) {
		this.application = application;
	}*/

	public String getCmdType() {
		return cmdType;
	}

	public void setCmdType(String cmdType) {
		this.cmdType = cmdType;
	}

	public List<InsExe> getIns() {
		return ins;
	}

	public void setIns(List<InsExe> ins) {
		this.ins = ins;
	}

	public InsExe getCurrIns() {
		return currIns;
	}

	public void setCurrIns(InsExe currIns) {
		this.currIns = currIns;
	}

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

    @Override
	public String toString() {
		return "CmdExe [id=" + id + ", orgId=" + orgId + ", appId=" + appId
				+ ", businessNo=" + businessNo + ", cmdCode=" + cmdCode
				+ ", cmdName=" + cmdName + ", cmdLevel=" + cmdLevel
				+ ", connAddr=" + connAddr + ", param=" + param
				+ ", returnValue=" + returnValue + ", innNum=" + innNum
				+ ", status=" + status + ", useTime=" + useTime
				+ ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", ins=" + ins + ", commPwd=" + commPwd + ", currIns="
				+ currIns + ", pushData=" + pushData 
				+  ", requestParams=" + requestParams
				+ "]";
	}

	public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo == null ? null : businessNo.trim();
    }

    public String getCmdCode() {
        return cmdCode;
    }

    public void setCmdCode(String cmdCode) {
        this.cmdCode = cmdCode == null ? null : cmdCode.trim();
    }

    public String getCmdName() {
        return cmdName;
    }

    public void setCmdName(String cmdName) {
        this.cmdName = cmdName == null ? null : cmdName.trim();
    }

    public Integer getCmdLevel() {
        return cmdLevel;
    }

    public void setCmdLevel(Integer cmdLevel) {
        this.cmdLevel = cmdLevel;
    }

    public String getConnAddr() {
        return connAddr;
    }

    public void setConnAddr(String connAddr) {
        this.connAddr = connAddr == null ? null : connAddr.trim();
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param == null ? null : param.trim();
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue == null ? null : returnValue.trim();
    }

    public Integer getInnNum() {
        return innNum;
    }

    public void setInnNum(Integer innNum) {
        this.innNum = innNum;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getUseTime() {
        return useTime;
    }

    public void setUseTime(Integer useTime) {
        this.useTime = useTime;
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

	


	public CmdCallback getCallback() {
		return callback;
	}

	public void setCallback(CmdCallback callback) {
		this.callback = callback;
	}

	public Map<String,Object> getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(Map<String,Object> requestParams) {
		this.requestParams = requestParams;
	}

	public String getCommPwd() {
		return commPwd;
	}

	public void setCommPwd(String commPwd) {
		this.commPwd = commPwd;
	}

	public Integer getHandleTimes() {
		return handleTimes;
	}

	public Integer getLimitHandleTimes() {
		return limitHandleTimes;
	}

	public void setHandleTimes(Integer handleTimes) {
		this.handleTimes = handleTimes;
	}

	public void setLimitHandleTimes(Integer limitHandleTimes) {
		this.limitHandleTimes = limitHandleTimes;
	}

	/*public TerminalEquip getTerminalEquip() {
		return terminalEquip;
	}*/

	/*public void setTerminalEquip(TerminalEquip terminalEquip) {
		this.terminalEquip = terminalEquip;
	}*/

	/*public NetEquip getNetEquip() {
		return netEquip;
	}*/

	/*public void setNetEquip(NetEquip netEquip) {
		this.netEquip = netEquip;
	}*/


	
    
    
    
}