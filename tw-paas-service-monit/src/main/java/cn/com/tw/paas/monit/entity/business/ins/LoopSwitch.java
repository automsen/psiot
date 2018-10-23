package cn.com.tw.paas.monit.entity.business.ins;

import org.hibernate.validator.constraints.NotEmpty;

import cn.com.tw.paas.monit.entity.business.base.BaseParam;

/**
 * 回路通断控制
 * @author admin
 *
 */
public class LoopSwitch extends BaseParam{

	private static final long serialVersionUID = 1L;
	
	@NotEmpty
	private String equipNumber;
	
	private String loop;

	public String getEquipNumber() {
		return equipNumber;
	}

	public void setEquipNumber(String equipNumber) {
		this.equipNumber = equipNumber;
	}

	public String getLoop() {
		return loop;
	}

	public void setLoop(String loop) {
		this.loop = loop;
	}

	@Override
	public String toString() {
		return "LoopSwitch [equipNumber=" + equipNumber + ", loop=" + loop + "]";
	}
}
