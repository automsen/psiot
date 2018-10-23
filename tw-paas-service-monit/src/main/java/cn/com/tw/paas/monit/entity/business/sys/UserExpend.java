package cn.com.tw.paas.monit.entity.business.sys;

import cn.com.tw.paas.monit.entity.db.sys.User;

/**
 * 系统用户扩展实体
 * @author Administrator
 *
 */
public class UserExpend extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8165024524624905046L;
	
	/**
	 * 机构ID
	 */
	private String orgId;
	
	/**
	 * 机构名称
	 */
	private String orgName;

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

}
