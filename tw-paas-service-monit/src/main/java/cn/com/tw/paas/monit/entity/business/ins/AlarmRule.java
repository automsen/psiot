package cn.com.tw.paas.monit.entity.business.ins;

import org.hibernate.validator.constraints.NotEmpty;

import cn.com.tw.paas.monit.entity.business.base.BaseParam;

public class AlarmRule extends BaseParam{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty
	private String equipNumber;
	
	@NotEmpty
	private String alarmMoney1;
	
	@NotEmpty
	private String alarmMoney2;
	
	@NotEmpty
	private String overdraft;

	public String getEquipNumber() {
		return equipNumber;
	}

	public void setEquipNumber(String equipNumber) {
		this.equipNumber = equipNumber;
	}

	public String getAlarmMoney1() {
		return alarmMoney1;
	}

	public void setAlarmMoney1(String alarmMoney1) {
		this.alarmMoney1 = alarmMoney1;
	}

	public String getAlarmMoney2() {
		return alarmMoney2;
	}

	public void setAlarmMoney2(String alarmMoney2) {
		this.alarmMoney2 = alarmMoney2;
	}

	public String getOverdraft() {
		return overdraft;
	}

	public void setOverdraft(String overdraft) {
		this.overdraft = overdraft;
	}

	@Override
	public String toString() {
		return "AlarmRule [equipNumber=" + equipNumber + ", alarmMoney1="
				+ alarmMoney1 + ", alarmMoney2=" + alarmMoney2 + ", overdraft="
				+ overdraft + "]";
	}
	
	
	

}
