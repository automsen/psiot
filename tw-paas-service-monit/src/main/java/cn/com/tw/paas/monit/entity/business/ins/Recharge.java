package cn.com.tw.paas.monit.entity.business.ins;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotEmpty;

import cn.com.tw.paas.monit.entity.business.base.BaseParam;

public class Recharge extends BaseParam{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty
	private BigDecimal money;
	
	@NotEmpty
	private String equipNumber;

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getEquipNumber() {
		return equipNumber;
	}

	public void setEquipNumber(String equipNumber) {
		this.equipNumber = equipNumber;
	}

	@Override
	public String toString() {
		return "Recharge [money=" + money + ", equipNumber=" + equipNumber
				+ "]";
	}
	

}
