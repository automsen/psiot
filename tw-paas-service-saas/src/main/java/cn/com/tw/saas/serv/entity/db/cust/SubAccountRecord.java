/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.entity.db.cust;

import java.math.BigDecimal;
import java.util.Date;

import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.terminal.Meter;

/**
 * 子账户记录
 * 数据库实体
 * 对应t_saas_cust_sub_account_record
 */
public class SubAccountRecord {

    /**
     * 主键
     */
    private String id;

    /**
     * 机构id
     */
    private String orgId;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 房间账户id
     */
    private String roomAccountId;

    /**
     * 房间id
     */
    private String roomId;

    /**
     * 房间名称
     */
    private String roomName;

    /**
     * 房间号
     */
    private String roomNumber;

    /**
     * 房间完整名称
     */
    private String roomFullName;

    private String subAccountId;

    private String subAccountType;

    /**
     * 能耗类型
     */
    private String energyType;

    /**
     * 仪表地址
     */
    private String meterAddr;

    /**
     * 计费方式
     */
    private String priceModeCode;

    /**
     * 订单类型
     * 1、充值
		2、仪表扣费
		3、结算
		4、继承旧表
     */
    private String orderTypeCode;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 支付方式
     */
    private String payModeCode;

    /**
     * 变动金额
     */
    private BigDecimal money;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 状态
0，等待
1，成功
2，失败
3，撤销
     */
    private Byte status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 详情
     */
    private String details;

    /**
     * 票据模版id
     */
    private String billTemplateId;

    /**
     * 客户id
     */
    private String customerId;

    /**
     * 客户编号
     */
    private String customerNo;

    /**
     * 客户电话
     */
    private String customerPhone;

    /**
     * 客户姓名
     */
    private String customerRealname;

    /**
     * 操作人主键
     */
    private String operatorId;

    /**
     * 操作人
     */
    private String operatorName;

    private Date createTime;

    private Date updateTime;
    
    /**
     * 房间类型
     */
    private String roomUse;
    
    /**
     * 客户类型
     */
    private String customerType;
    
    /**
     * 楼栋Id
     */
    private String regionId;
    
    /**
     * 楼栋编号
     */
    private String regionNo;
    
    /**
     * 结算的时候预览的曾经单价
     */
    private String price;
    private String readValue;

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
    public String getOrgName() {
        return orgName;
    }
    public void setOrgName(String orgName) {
        this.orgName = orgName == null ? null : orgName.trim();
    }
    public String getRoomAccountId() {
        return roomAccountId;
    }
    public void setRoomAccountId(String roomAccountId) {
        this.roomAccountId = roomAccountId == null ? null : roomAccountId.trim();
    }
    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId == null ? null : roomId.trim();
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
    public String getSubAccountId() {
        return subAccountId;
    }
    public void setSubAccountId(String subAccountId) {
        this.subAccountId = subAccountId == null ? null : subAccountId.trim();
    }
    public String getSubAccountType() {
        return subAccountType;
    }
    public void setSubAccountType(String subAccountType) {
        this.subAccountType = subAccountType == null ? null : subAccountType.trim();
    }
    public String getEnergyType() {
        return energyType;
    }
    public void setEnergyType(String energyType) {
        this.energyType = energyType == null ? null : energyType.trim();
    }
    public String getMeterAddr() {
        return meterAddr;
    }
    public void setMeterAddr(String meterAddr) {
        this.meterAddr = meterAddr == null ? null : meterAddr.trim();
    }
    public String getPriceModeCode() {
        return priceModeCode;
    }
    public void setPriceModeCode(String priceModeCode) {
        this.priceModeCode = priceModeCode == null ? null : priceModeCode.trim();
    }
    public String getOrderTypeCode() {
        return orderTypeCode;
    }
    public void setOrderTypeCode(String orderTypeCode) {
        this.orderTypeCode = orderTypeCode == null ? null : orderTypeCode.trim();
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }
    public String getPayModeCode() {
        return payModeCode;
    }
    public void setPayModeCode(String payModeCode) {
        this.payModeCode = payModeCode == null ? null : payModeCode.trim();
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public Byte getStatus() {
        return status;
    }
    public void setStatus(Byte status) {
        this.status = status;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details == null ? null : details.trim();
    }
    public String getBillTemplateId() {
        return billTemplateId;
    }
    public void setBillTemplateId(String billTemplateId) {
        this.billTemplateId = billTemplateId == null ? null : billTemplateId.trim();
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
	 * 生成待插入的记录
	 * @param subAcc 仪表子账户
	 * @param room 房间
	 * @param cust 客户
	 * @param user 操作员
	 * @param money 金额变动
	 * @return
	 */
	public static SubAccountRecord generateRecord(Meter oldMeter,Room room,Customer cust,OrgUser user,BigDecimal money){
		SubAccountRecord record = new SubAccountRecord();
		record.setOrgId(oldMeter.getOrgId());
//		record.setOrgName("");
		record.setRoomAccountId(oldMeter.getAccountId());
		record.setRoomId(oldMeter.getRoomId());
		if(null!=room){
			record.setRoomName(room.getRoomName());
			record.setRoomNumber(room.getRoomNumber());
			record.setRoomFullName(room.getRegionFullName()+room.getRoomName());
			record.setRoomUse(room.getRoomUse());
		}
		record.setSubAccountId(oldMeter.getSubAccountId());
//		record.setSubAccountType(subAcc.getSubAccountType());
		record.setEnergyType(oldMeter.getEnergyType());
		record.setMeterAddr(oldMeter.getMeterAddr());
		record.setPriceModeCode(oldMeter.getPriceModeCode());
//		record.setOrderTypeCode("");
//		record.setOrderId("");
//		record.setPayModeCode("");
		record.setMoney(money);
		record.setBalance(oldMeter.getBalance());
		record.setStatus((byte) 1);
//		record.setRemark("");
//		record.setDetails("");
		if (null!=cust){
			record.setCustomerId(cust.getCustomerId());
			record.setCustomerNo(cust.getCustomerNo());
			record.setCustomerPhone(cust.getCustomerMobile1());
			record.setCustomerRealname(cust.getCustomerRealname());
			record.setCustomerType(cust.getCustomerType());
		}
		if (null!=user){
			record.setOperatorId(user.getUserId());
			record.setOperatorName(user.getUserName());
			record.setOrgName(user.getOrgName());
		}
		return record;
	}
	public String getRoomUse() {
		return roomUse;
	}
	public void setRoomUse(String roomUse) {
		this.roomUse = roomUse;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getRegionNo() {
		return regionNo;
	}
	public void setRegionNo(String regionNo) {
		this.regionNo = regionNo;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getReadValue() {
		return readValue;
	}
	public void setReadValue(String readValue) {
		this.readValue = readValue;
	}
}