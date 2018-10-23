package cn.com.tw.saas.serv.entity.business.cust;

import java.math.BigDecimal;

/**
 * 客户房间相关查询参数
 * @author admin
 *
 */
public class CustRoomParam {
	
	/**
	 * 备注 银联信息
	 */
	private String remarks;

    /**
     * 客户主键
     */
    private String customerId;

    /**
     * 机构id
     */
    private String orgId;

    /**
     * 身份标识
     */
    private String customerNo;

    /**
     * 真实姓名
     */
    private String customerRealname;
    
    /**
     * 联系方式
     */
    private String customerMobile1;
    
    /**
     * 楼栋id
     */
    private String regionNo;
    
    /**
     * 房间id
     */
    private String roomId;
    
    /**
     * 房间名
     */
    private String roomName;

    /**
     * 房间号
     */
    private String roomNumber;
    
    /**
     * 房间账户id
     */
    private String roomAccountId;
    
    /**
     * 房间账户充值金额
     */
    private BigDecimal roomChargeMoney;
    
    
    /**
     *  提交额外字段
     *  支付方式
     * @return
     */
    private String payModeCode;
    
    
    /**
     * 0，维护中
		1，使用中
		2，审核中
		3，废弃中
		4.空置中
     */
    private Byte accountStatus;
    
    private String roomCustId;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getCustomerRealname() {
		return customerRealname;
	}

	public void setCustomerRealname(String customerRealname) {
		this.customerRealname = customerRealname;
	}

	public String getCustomerMobile1() {
		return customerMobile1;
	}

	public void setCustomerMobile1(String customerMobile1) {
		this.customerMobile1 = customerMobile1;
	}

	public String getregionNo() {
		return regionNo;
	}

	public void setregionNo(String regionNo) {
		this.regionNo = regionNo;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public String getRoomAccountId() {
		return roomAccountId;
	}

	public void setRoomAccountId(String roomAccountId) {
		this.roomAccountId = roomAccountId;
	}

	public BigDecimal getRoomChargeMoney() {
		return roomChargeMoney;
	}

	public void setRoomChargeMoney(BigDecimal roomChargeMoney) {
		this.roomChargeMoney = roomChargeMoney;
	}


	public void setPayModeCode(String payModeCode) {
		this.payModeCode = payModeCode;
	}

	public String getPayModeCode() {
		return payModeCode;
	}

	public Byte getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(Byte accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getRoomCustId() {
		return roomCustId;
	}

	public void setRoomCustId(String roomCustId) {
		this.roomCustId = roomCustId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	
	
	
	
}
