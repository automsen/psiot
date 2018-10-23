package cn.com.tw.saas.serv.entity.order;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.saas.serv.entity.db.cust.Customer;
import cn.com.tw.saas.serv.entity.db.cust.OrderRecharge;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.terminal.Meter;

/**
 * 结算订单表 t_saas_cust_order_recharge
 * @author Administrator
 *
 */
public class OrderClearing {
	/**
	 * 订单ID
	 */
    private String orderId;

    /**
     * 组织机构
     */
    private String orgId;

    private String orgName;

    /**
     * 订单号
     */
    private String orderNo;

    private String orderSource;

    /**
     * 系统内部操作者id
     */
    private String operatorId;

    /**
     * 操作员类型（1011 系统用户，1012 客户）
     */
    private String operatorType;

    private String operatorName;

    private String externalNo;

    /**
     * 结算类型 1710.商铺结算  1720.宿舍结算
     */
    private String clearingType;

    /**
     * 结算金额
     */
    private BigDecimal money;

    /**
     * 结算方式
     */
    private String clearingModeCode;

    /**
     * 实际结算金额
     */
    private BigDecimal realityMoney;

    private Date clearingTime;

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

    private Integer clearingNum;

    /**
     * 票据id
     */
    private String billTemplateId;

    /**
     * 备注信息
     */
    private String remarks;

    /**
     * 结算前读数
     */
    private BigDecimal clearingReadValue;

    /**
     * 结算前余额
     */
    private BigDecimal clearingMoney;

    private Date preReadTime;

    private BigDecimal aftReadValue;

    private BigDecimal aftMoney;

    private Date aftReadTime;

    private Byte status;

    private Date createTime;

    private Date updateTime;

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

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType == null ? null : operatorType.trim();
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

    public String getClearingType() {
        return clearingType;
    }

    public void setClearingType(String clearingType) {
        this.clearingType = clearingType == null ? null : clearingType.trim();
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getClearingModeCode() {
        return clearingModeCode;
    }

    public void setClearingModeCode(String clearingModeCode) {
        this.clearingModeCode = clearingModeCode == null ? null : clearingModeCode.trim();
    }

    public BigDecimal getRealityMoney() {
        return realityMoney;
    }

    public void setRealityMoney(BigDecimal realityMoney) {
        this.realityMoney = realityMoney;
    }

    public Date getClearingTime() {
        return clearingTime;
    }

    public void setClearingTime(Date clearingTime) {
        this.clearingTime = clearingTime;
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

    public Integer getClearingNum() {
        return clearingNum;
    }

    public void setClearingNum(Integer clearingNum) {
        this.clearingNum = clearingNum;
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

    public BigDecimal getClearingReadValue() {
        return clearingReadValue;
    }

    public void setClearingReadValue(BigDecimal clearingReadValue) {
        this.clearingReadValue = clearingReadValue;
    }

    public BigDecimal getClearingMoney() {
        return clearingMoney;
    }

    public void setClearingMoney(BigDecimal clearingMoney) {
        this.clearingMoney = clearingMoney;
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
    
    
    
    /**
     *  初始化订单信息
     * @param room
     * @param cust
     * @param user
     * @param money
     * @param meter
     * @return
     */
    public static OrderClearing generateOrder(Room room,Customer cust,OrgUser user,BigDecimal money,Meter meter){
    	OrderClearing record = new OrderClearing();
    	record.setOrderId(CommUtils.getUuid());
    	record.setOrderNo(createOrderNo(room));
    	if(room != null){
    		record.setOrgId(room.getOrgId());
    		record.setRoomAccountId(room.getAccountId());
    		record.setRoomId(room.getRoomId());
    		record.setRoomName(room.getRoomName());
    		record.setRoomNumber(room.getRoomNumber());
    		record.setRoomFullName(room.getRegionFullName());
    		record.setClearingType(room.getRoomUse());
    		record.setClearingMoney(room.getBalance());
    		record.setAftMoney(room.getBalance().add(money));
    	}
    	record.setClearingModeCode("1901");//现金支付
    	record.setOperatorType("1011");//系统用户
		record.setMoney(money);
		record.setRealityMoney(money);
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
		record.setClearingTime(new Date());
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
}