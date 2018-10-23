/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.entity.db.cust;

import java.util.Date;

/**
 * 客户档案
 * 数据库实体
 * 对应t_saas_customer
 */
public class Customer {

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
     * 客户类型
     */
    private String customerType;

    /**
     * 真实姓名
     */
    private String customerRealname;

    /**
     * 密码
     */
    private String customerPassword;

    /**
     * 性别
     */
    private Byte customerSex;

    /**
     * 联系方式
     */
    private String customerMobile1;

    /**
     * 0，使用中 
1，停用中
2，已销户
     */
    private Byte customerStatus;

    /**
     * 邮箱
     */
    private String customerEmail;

    /**
     * qq
     */
    private String customerQq;

    private String openId;

    /**
     * 录入操作者id
     */
    private String operatorId;

    /**
     * 录入操作者姓名
     */
    private String operatorName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     *  前端公众号appId
     */
    private String appId;
    
    
    

    public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId == null ? null : customerId.trim();
    }
    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }
    public String getCustomerNo() {
        return customerNo;
    }
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo == null ? null : customerNo.trim();
    }
    public String getCustomerType() {
        return customerType;
    }
    public void setCustomerType(String customerType) {
        this.customerType = customerType == null ? null : customerType.trim();
    }
    public String getCustomerRealname() {
        return customerRealname;
    }
    public void setCustomerRealname(String customerRealname) {
        this.customerRealname = customerRealname == null ? null : customerRealname.trim();
    }
    public String getCustomerPassword() {
        return customerPassword;
    }
    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword == null ? null : customerPassword.trim();
    }
    public Byte getCustomerSex() {
        return customerSex;
    }
    public void setCustomerSex(Byte customerSex) {
        this.customerSex = customerSex;
    }
    public String getCustomerMobile1() {
        return customerMobile1;
    }
    public void setCustomerMobile1(String customerMobile1) {
        this.customerMobile1 = customerMobile1 == null ? null : customerMobile1.trim();
    }
    public Byte getCustomerStatus() {
        return customerStatus;
    }
    public void setCustomerStatus(Byte customerStatus) {
        this.customerStatus = customerStatus;
    }
    public String getCustomerEmail() {
        return customerEmail;
    }
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail == null ? null : customerEmail.trim();
    }
    public String getCustomerQq() {
        return customerQq;
    }
    public void setCustomerQq(String customerQq) {
        this.customerQq = customerQq == null ? null : customerQq.trim();
    }
    public String getOpenId() {
        return openId;
    }
    public void setOpenId(String openId) {
        this.openId = openId == null ? null : openId.trim();
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
}