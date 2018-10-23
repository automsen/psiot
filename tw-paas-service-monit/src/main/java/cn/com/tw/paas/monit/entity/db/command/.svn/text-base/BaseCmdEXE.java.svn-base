package cn.com.tw.paas.monit.entity.db.command;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.com.tw.paas.monit.service.dlt645.CmdCallBack;

public class BaseCmdEXE implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -8630960981749333917L;

	private String cmdId;

    private String orgId;

    private String meterId;

    private String meterAddr;

    private String cmdCode;

    private String taskId;

    private String cmdName;

    private String cmdLevel;

    private Integer useTime;

    private String param;

    private String reason;

    private String returnValue;

    private Byte status;

    private Date createTime;

    private Date updateTime;
    
    private String orgName;
    
    private String RequestUrl;
    
    private int success;
    
    private int fail;
    
    private int timeout;
    
    private int totalAvg;
    
    private int codeTotal;
    
    
    /**
     * 冗余字段，用来放任务所需字段
     */
    private String tempStr;
    
    private Object paramObj;
    
   private List<BaseInnEXE> inns;
    
    private BaseInnEXE innEXE;
   
    private int ReqNum = 0;
    
    private int retryNum = 0;
    
    private String cmdObjid;

    private String cmdTable;
    
    private String cmdCallback;   
    
    /**
     * 485指令回调使用
     */
    private CmdCallBack callback;
    
    private Boolean isBlock;
    
    
    /**
     * 扩展字段
     * @return
     */
    private BigDecimal successAvg;
    
    private BigDecimal failAvg;
    
    private BigDecimal timeoutAvg;
    
    

	public BigDecimal getSuccessAvg() {
		return successAvg;
	}

	public void setSuccessAvg(BigDecimal successAvg) {
		this.successAvg = successAvg;
	}

	public BigDecimal getFailAvg() {
		return failAvg;
	}

	public void setFailAvg(BigDecimal failAvg) {
		this.failAvg = failAvg;
	}

	public BigDecimal getTimeoutAvg() {
		return timeoutAvg;
	}

	public void setTimeoutAvg(BigDecimal timeoutAvg) {
		this.timeoutAvg = timeoutAvg;
	}

	public CmdCallBack getCallback() {
		return callback;
	}

	public void setCallback(CmdCallBack callback) {
		this.callback = callback;
	}

	public int getRetryNum() {
		return retryNum;
	}

	public void setRetryNum(int retryNum) {
		this.retryNum = retryNum;
	}

	public Object getParamObj() {
		return paramObj;
	}

	public void setParamObj(Object paramObj) {
		this.paramObj = paramObj;
	}

	public String getTempStr() {
		return tempStr;
	}

	public void setTempStr(String tempStr) {
		this.tempStr = tempStr;
	}

	public int getCodeTotal() {
		return codeTotal;
	}

	public void setCodeTotal(int codeTotal) {
		this.codeTotal = codeTotal;
	}

	public int getTotalAvg() {
		return totalAvg;
	}

	public void setTotalAvg(int totalAvg) {
		this.totalAvg = totalAvg;
	}

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getFail() {
		return fail;
	}

	public void setFail(int fail) {
		this.fail = fail;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getRequestUrl() {
		return RequestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		RequestUrl = requestUrl;
	}

	public int getReqNum() {
		return ReqNum;
	}

	public void setReqNum(int reqNum) {
		ReqNum = reqNum;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId == null ? null : cmdId.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId == null ? null : meterId.trim();
    }

    public String getMeterAddr() {
        return meterAddr;
    }

    public void setMeterAddr(String meterAddr) {
        this.meterAddr = meterAddr == null ? null : meterAddr.trim();
    }

    public String getCmdCode() {
        return cmdCode;
    }

    public void setCmdCode(String cmdCode) {
        this.cmdCode = cmdCode == null ? null : cmdCode.trim();
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId == null ? null : taskId.trim();
    }

    public String getCmdName() {
        return cmdName;
    }

    public void setCmdName(String cmdName) {
        this.cmdName = cmdName == null ? null : cmdName.trim();
    }

    public String getCmdLevel() {
        return cmdLevel;
    }

    public void setCmdLevel(String cmdLevel) {
        this.cmdLevel = cmdLevel == null ? null : cmdLevel.trim();
    }

    public Integer getUseTime() {
        return useTime;
    }

    public void setUseTime(Integer useTime) {
        this.useTime = useTime;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param == null ? null : param.trim();
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    public String getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(String returnValue) {
        this.returnValue = returnValue == null ? null : returnValue.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

	public String getCmdObjid() {
		return cmdObjid;
	}

	public void setCmdObjid(String cmdObjid) {
		this.cmdObjid = cmdObjid;
	}

	public String getCmdTable() {
		return cmdTable;
	}

	public void setCmdTable(String cmdTable) {
		this.cmdTable = cmdTable;
	}

	public Boolean getIsBlock() {
		return isBlock;
	}

	public void setIsBlock(Boolean isBlock) {
		this.isBlock = isBlock;
	}

	public String getCmdCallback() {
		return cmdCallback;
	}

	public void setCmdCallback(String cmdCallback) {
		this.cmdCallback = cmdCallback;
	}

	public List<BaseInnEXE> getInns() {
		return inns;
	}

	public void setInns(List<BaseInnEXE> inns) {
		this.inns = inns;
	}

	public BaseInnEXE getInnEXE() {
		return innEXE;
	}

	public void setInnEXE(BaseInnEXE innEXE) {
		this.innEXE = innEXE;
	}
	
	
	
}