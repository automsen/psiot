package cn.com.tw.saas.serv.entity.excel;

/**
 * 客户导入
 * @author Administrator
 *
 */
public class SaasCustomerExcel {
	
	/**
	 * 客户姓名
	 */
	private String customerRealname;
	
	/**
	 * 手机号
	 */
	private String customerMobile1;
	
	/**
	 * 客户类型
	 */
	private String customerType;
	
	/**
	 * 身份标识
	 */
	private String customerNo;

    /*private String customerPassword;*/

	/**
	 * 性别 1-男 0-女
	 */
    private String customerSex;

    

    private String customerEmail;

    private String customerQq;
    
    private String message;

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo == null ? null : customerNo.trim();
	}

	public String getCustomerRealname() {
		return customerRealname;
	}

	public void setCustomerRealname(String customerRealname) {
		this.customerRealname = customerRealname == null ? null : customerRealname.trim();
	}

	public String getCustomerSex() {
		return customerSex;
	}

	public void setCustomerSex(String customerSex) {
		this.customerSex = customerSex == null ? null : customerSex.trim();
	}

	public String getCustomerMobile1() {
		return customerMobile1;
	}

	public void setCustomerMobile1(String customerMobile1) {
		this.customerMobile1 = customerMobile1 == null ? null : customerMobile1.trim();
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

/*	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}*/
    
}
