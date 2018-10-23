package cn.com.tw.saas.serv.entity.db.cust;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.terminal.Meter;

/**
 * 充值订单表 （暂时只用来记录相关信息）
 * @author liming
 *
 */
public class OrderRecharge {
    private String orderId;

    private String orgId;
    
    private String orgName;

    private String orderNo;

    /**
     *  订单来源 
     */
    private String orderSource;


    private String operatorId;
    /**
     * 操作员类型（1011 系统用户，1012 客户）
     */
    private String operatorType;
    
    private String operatorName;
    /**
     *  第三方订单号
     */
    private String externalNo;
    /**
     *  充值类型 暂时为冗余字段
     */
    private String rechargeType;


    /**
     *  订单金额
     */
    private BigDecimal money;
    /**
     *  订单支付方式
     */
    private String payModeCode;


    /**
     *  实际支付金额
     */
    private BigDecimal payMoney;

    private Date rechargeTime;

    private String customerId;

    private String customerNo;

    private String customerPhone;

    private String customerRealname;

    private String roomId;

    private String roomAccountId;

    private String roomName;

    private String roomNumber;

    private String roomFullName;

    private String meterAddr;

    private String meterType;

    private String energyType;

    private Integer rechargeNum;

    private String billTemplateId;

    private String remarks;

    private BigDecimal preReadValue;

    private BigDecimal preMoney;

    private Date preReadTime;

    private BigDecimal aftReadValue;

    private BigDecimal aftMoney;

    private Date aftReadTime;

    private Byte status;

    private Date createTime;

    private Date updateTime;
    
    /**
     *  查询字段。用来搜索最近几个小时的订单
     */
    private Integer lastHours;
    
    private String dateType;
    
    private String createTimeStr;
    
    public Integer getLastHours() {
		return lastHours;
	}

	public void setLastHours(Integer lastHours) {
		this.lastHours = lastHours;
	}
	
	//是否导出英文excel
    private String isEnExcel;
    
    
    public String getIsEnExcel() {
		return isEnExcel;
	}
	public void setIsEnExcel(String isEnExcel) {
		this.isEnExcel = isEnExcel;
	}
	
	/**
     * 房间类型
     */
    private String roomUse;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName == null ? null : orgName.trim();
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource == null ? null : orderSource.trim();
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId == null ? null : operatorId.trim();
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }

    public String getExternalNo() {
        return externalNo;
    }

    public void setExternalNo(String externalNo) {
        this.externalNo = externalNo == null ? null : externalNo.trim();
    }

    public String getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(String rechargeType) {
        this.rechargeType = rechargeType == null ? null : rechargeType.trim();
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getPayModeCode() {
        return payModeCode;
    }

    public void setPayModeCode(String payModeCode) {
        this.payModeCode = payModeCode == null ? null : payModeCode.trim();
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public Date getRechargeTime() {
        return rechargeTime;
    }

    public void setRechargeTime(Date rechargeTime) {
        this.rechargeTime = rechargeTime;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId == null ? null : customerId.trim();
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo == null ? null : customerNo.trim();
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone == null ? null : customerPhone.trim();
    }

    public String getCustomerRealname() {
        return customerRealname;
    }

    public void setCustomerRealname(String customerRealname) {
        this.customerRealname = customerRealname == null ? null : customerRealname.trim();
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId == null ? null : roomId.trim();
    }

    public String getRoomAccountId() {
        return roomAccountId;
    }

    public void setRoomAccountId(String roomAccountId) {
        this.roomAccountId = roomAccountId == null ? null : roomAccountId.trim();
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName == null ? null : roomName.trim();
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber == null ? null : roomNumber.trim();
    }

    public String getRoomFullName() {
        return roomFullName;
    }

    public void setRoomFullName(String roomFullName) {
        this.roomFullName = roomFullName == null ? null : roomFullName.trim();
    }

    public String getMeterAddr() {
        return meterAddr;
    }

    public void setMeterAddr(String meterAddr) {
        this.meterAddr = meterAddr == null ? null : meterAddr.trim();
    }

    public String getMeterType() {
        return meterType;
    }

    public void setMeterType(String meterType) {
        this.meterType = meterType == null ? null : meterType.trim();
    }

    public String getEnergyType() {
        return energyType;
    }

    public void setEnergyType(String energyType) {
        this.energyType = energyType == null ? null : energyType.trim();
    }

    public Integer getRechargeNum() {
        return rechargeNum;
    }

    public void setRechargeNum(Integer rechargeNum) {
        this.rechargeNum = rechargeNum;
    }

    public String getBillTemplateId() {
        return billTemplateId;
    }

    public void setBillTemplateId(String billTemplateId) {
        this.billTemplateId = billTemplateId == null ? null : billTemplateId.trim();
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public BigDecimal getPreReadValue() {
        return preReadValue;
    }

    public void setPreReadValue(BigDecimal preReadValue) {
        this.preReadValue = preReadValue;
    }

    public BigDecimal getPreMoney() {
        return preMoney;
    }

    public void setPreMoney(BigDecimal preMoney) {
        this.preMoney = preMoney;
    }

    public Date getPreReadTime() {
        return preReadTime;
    }

    public void setPreReadTime(Date preReadTime) {
        this.preReadTime = preReadTime;
    }

    public BigDecimal getAftReadValue() {
        return aftReadValue;
    }

    public void setAftReadValue(BigDecimal aftReadValue) {
        this.aftReadValue = aftReadValue;
    }

    public BigDecimal getAftMoney() {
        return aftMoney;
    }

    public void setAftMoney(BigDecimal aftMoney) {
        this.aftMoney = aftMoney;
    }

    public Date getAftReadTime() {
        return aftReadTime;
    }

    public void setAftReadTime(Date aftReadTime) {
        this.aftReadTime = aftReadTime;
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
    
    
    
    public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	/**
     *  初始化订单信息
     * @param room
     * @param cust
     * @param user
     * @param money
     * @param meter
     * @return
     */
    public static OrderRecharge generateOrder(Room room,Customer cust,OrgUser user,BigDecimal money,Meter meter){
    	OrderRecharge record = new OrderRecharge();
    	record.setOrderId(CommUtils.getUuid());
    	record.setOrderNo(createOrderNo(room));
    	if(room != null){
    		record.setOrgId(room.getOrgId());
    		record.setRoomAccountId(room.getAccountId());
    		record.setRoomId(room.getRoomId());
    		record.setRoomName(room.getRoomName());
    		record.setRoomNumber(room.getRoomNumber());
    		record.setRoomFullName(room.getRegionFullName());
    		record.setPreMoney(room.getBalance());
    	}
		record.setMoney(money);
		record.setPayMoney(money);
		if (null!=cust){
			record.setCustomerId(cust.getCustomerId());
			record.setCustomerNo(cust.getCustomerNo());
			record.setCustomerRealname(cust.getCustomerRealname());
			record.setCustomerPhone(cust.getCustomerMobile1());
		}
		if (null!=user){
			record.setOperatorId(user.getUserId());
			if(!StringUtils.isEmpty(user.getUserRealName())){
				record.setOperatorName(user.getUserRealName());
			}
			record.setOrgName(user.getOrgName());
		}
		if(meter!= null){
			record.setMeterAddr(meter.getMeterAddr());
			record.setMeterType(meter.getMeterType());
			record.setEnergyType(meter.getEnergyType());
		}
		record.setRechargeTime(new Date());
		record.setCreateTime(new Date());
		return record;
    	
    	
    	
    }
    /**
     * 
     *  机构Id+类型（充值 98）+时间搓+随机数
     * @return
     */
	private static String createOrderNo(Room room) {
		String orgId = room.getOrgId();
		if(orgId.length()>4){
			int startIndex = orgId.length()-5;
			orgId = orgId.substring(startIndex);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
		String timestamp = sdf.format(new Date());
		int randomNum = (int)((Math.random()*9+1)*1000);
		
		String orderNo = orgId+"98"+timestamp+randomNum;
		return orderNo;
	}

	public String getRoomUse() {
		return roomUse;
	}

	public void setRoomUse(String roomUse) {
		this.roomUse = roomUse;
	}

	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
}