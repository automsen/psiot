package cn.com.tw.paas.monit.entity.business.replace;

import org.hibernate.validator.constraints.NotEmpty;

import cn.com.tw.paas.monit.entity.business.base.BaseParam;

public class TermReplace extends BaseParam{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty
	private String oldEquipNumber;
	
	@NotEmpty
	private String newEquipNumber;

	public String getOldEquipNumber() {
		return oldEquipNumber;
	}

	public void setOldEquipNumber(String oldEquipNumber) {
		this.oldEquipNumber = oldEquipNumber;
	}

	public String getNewEquipNumber() {
		return newEquipNumber;
	}

	public void setNewEquipNumber(String newEquipNumber) {
		this.newEquipNumber = newEquipNumber;
	}
	
	

}
