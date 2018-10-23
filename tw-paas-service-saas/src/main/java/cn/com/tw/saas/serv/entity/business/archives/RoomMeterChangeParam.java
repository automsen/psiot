package cn.com.tw.saas.serv.entity.business.archives;

import java.math.BigDecimal;

/**
 * 房间仪表关系变更参数
 * 
 * @author admin
 *
 */
public class RoomMeterChangeParam {

	private String orgId;
	private String roomId;
	// 能源类型
	private String energyType;
	/**
	 * 操作类型 
	 * 1、取消旧表 
	 * 2、更换新表
	 */
	private String operation;
	// 旧表信息
	private String oldMeterAddr;
	private BigDecimal oldMeterRead;
	// 新表信息
	private String newMeterAddr;
	private BigDecimal newMeterRead;
	// 操作人信息
	private String operatorId;
	private String operatorName;

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getEnergyType() {
		return energyType;
	}

	public void setEnergyType(String energyType) {
		this.energyType = energyType;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getOldMeterAddr() {
		return oldMeterAddr;
	}

	public void setOldMeterAddr(String oldMeterAddr) {
		this.oldMeterAddr = oldMeterAddr;
	}

	public BigDecimal getOldMeterRead() {
		return oldMeterRead;
	}

	public void setOldMeterRead(BigDecimal oldMeterRead) {
		this.oldMeterRead = oldMeterRead;
	}

	public String getNewMeterAddr() {
		return newMeterAddr;
	}

	public void setNewMeterAddr(String newMeterAddr) {
		this.newMeterAddr = newMeterAddr;
	}

	public BigDecimal getNewMeterRead() {
		return newMeterRead;
	}

	public void setNewMeterRead(BigDecimal newMeterRead) {
		this.newMeterRead = newMeterRead;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

}
