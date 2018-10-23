package cn.com.tw.saas.serv.entity.org;

import java.math.BigDecimal;
import java.util.Date;

import cn.com.tw.saas.serv.entity.db.cust.Customer;
import cn.com.tw.saas.serv.entity.room.Room;

/**
 * t_saas_org_account_record
 * @author Administrator
 *
 */
public class OrgAccountRecord {
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
	 * 房间ID
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

    /**
	 * 变动金额
	 */
    private BigDecimal money;

    /**
	 * 创建时间
	 */
    private Date createTime;

    /**
	 * 修改时间
	 */
    private Date updateTime;

    /**
	 * 生成记录日期 天
	 */
    private String dayTime;

    /**
	 * 余额
	 */
    private BigDecimal balance;

    /**
	 * 记录类型1、充值 2.扣费3.撤销
	 */
    private String orderTypeCode;

    /**
	 * 详情
	 */
    private String details;

    /**
     * 期初余额
     */
    private BigDecimal minMoney;
    
    /**
     * 每天收入
     */
    private BigDecimal dayMoney;
    
    /**
     * 收入数量
     */
    private Integer revenueNumber; 
    
    private String startTime;
    
    private String endTime;
    
    /**
     * 支出数量
     */
    private Integer disburseNumber;

    /**
     * 每天支出
     */
    private BigDecimal disburseMoney;
    /**
     * 撤销金额
     */
    private BigDecimal bMoney;
    /**
     * 撤销数量
     */
    private Integer bNumber;
    
    /**
     * 每天支出的String类型
     */
    private String disburseMoneyStr;
    
    /**
     * 资金账单 字段
     */
    private String fundNumber;
    
    private String fundMoney;
    
  //是否导出英文excel
    private String isEnExcel;
    
    
    public String getIsEnExcel() {
		return isEnExcel;
	}
	public void setIsEnExcel(String isEnExcel) {
		this.isEnExcel = isEnExcel;
	}
    
    /**
     * 资金账单 字段
     */
    
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

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
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

    public String getDayTime() {
        return dayTime;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime == null ? null : dayTime.trim();
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getOrderTypeCode() {
        return orderTypeCode;
    }

    public void setOrderTypeCode(String orderTypeCode) {
        this.orderTypeCode = orderTypeCode == null ? null : orderTypeCode.trim();
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details == null ? null : details.trim();
    }
    
    /**
     * 创建机构账户记录
     * @param subAcc
     * @param room
     * @param cust
     * @param user
     * @param money
     * @param bigDecimal 
     * @return
     */
    public static OrgAccountRecord createOrgAccountRecord(Room room,Customer cust,OrgUser user,BigDecimal money, BigDecimal balance){
    	OrgAccountRecord orgAccountRecord = new OrgAccountRecord();
    	orgAccountRecord.setOrgId(room.getOrgId());
    	orgAccountRecord.setRoomId(room.getRoomId());
		if(null!=room){
			orgAccountRecord.setRoomName(room.getRoomName());
			orgAccountRecord.setRoomNumber(room.getRoomNumber());
			orgAccountRecord.setRoomFullName(room.getRegionFullName()+room.getRoomName());
		}
		orgAccountRecord.setMoney(money);
		orgAccountRecord.setBalance(balance);
		if (null!=cust){
			orgAccountRecord.setCustomerId(cust.getCustomerId());
			orgAccountRecord.setCustomerNo(cust.getCustomerNo());
			orgAccountRecord.setCustomerPhone(cust.getCustomerMobile1());
			orgAccountRecord.setCustomerRealname(cust.getCustomerRealname());
		}
		if (null!=user){
			orgAccountRecord.setOperatorId(user.getUserId());
			orgAccountRecord.setOperatorName(user.getUserName());
			orgAccountRecord.setOrgName(user.getOrgName());
		}
    	
    	return orgAccountRecord;
    }

	public BigDecimal getMinMoney() {
		return minMoney;
	}

	public void setMinMoney(BigDecimal minMoney) {
		this.minMoney = minMoney;
	}

	public BigDecimal getDayMoney() {
		return dayMoney;
	}

	public void setDayMoney(BigDecimal dayMoney) {
		this.dayMoney = dayMoney;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public BigDecimal getDisburseMoney() {
		return disburseMoney;
	}

	public void setDisburseMoney(BigDecimal disburseMoney) {
		this.disburseMoney = disburseMoney;
	}

	public String getDisburseMoneyStr() {
		return disburseMoneyStr;
	}

	public void setDisburseMoneyStr(String disburseMoneyStr) {
		this.disburseMoneyStr = disburseMoneyStr;
	}

	public String getFundNumber() {
		return fundNumber;
	}

	public void setFundNumber(String fundNumber) {
		this.fundNumber = fundNumber;
	}

	public String getFundMoney() {
		return fundMoney;
	}

	public void setFundMoney(String fundMoney) {
		this.fundMoney = fundMoney;
	}

	public BigDecimal getbMoney() {
		return bMoney;
	}

	public void setbMoney(BigDecimal bMoney) {
		this.bMoney = bMoney;
	}

	public Integer getRevenueNumber() {
		return revenueNumber;
	}

	public void setRevenueNumber(Integer revenueNumber) {
		this.revenueNumber = revenueNumber;
	}

	public Integer getDisburseNumber() {
		return disburseNumber;
	}

	public void setDisburseNumber(Integer disburseNumber) {
		this.disburseNumber = disburseNumber;
	}

	public Integer getbNumber() {
		return bNumber;
	}

	public void setbNumber(Integer bNumber) {
		this.bNumber = bNumber;
	}

}