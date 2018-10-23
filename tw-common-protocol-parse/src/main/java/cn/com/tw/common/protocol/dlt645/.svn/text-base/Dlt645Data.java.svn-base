package cn.com.tw.common.protocol.dlt645;

import java.io.Serializable;

import cn.com.tw.common.protocol.ProtoData;

/**
 * DLT645_2007数据内容
 * @author Cheng Qi Peng
 *
 */
public class Dlt645Data extends ProtoData implements Serializable{

	private static final long serialVersionUID = 3468956850006300243L;
	/**
	 * 表地址（必须）
	 */
	private String addr;
	/**
	 * 控制码（必须）
	 */
	private String cotrolCode;
	
	/**
	 * 数据长度（必须）
	 */
	private int dataLength;
	/**
	 * 数据域（必须）
	 * 数据域=数据标识+密码+操作者代码+数据项
	 */
	private String dataField;
	/**
	 * 数据标识（非必须）
	 */
	private String dataMarker;
	/**
	 * 密码（非必须）
	 */
	private String password;
	/**
	 * 操作者编码（非必须）
	 */
	private String operatorCode;
	/**
	 * 时标（非必须，用于曲线指令）
	 */
	private String freezeTd;
	/**
	 * 数据项（非必须）
	 */
	private String dataValues;
	/**
	 * 序列（非必须，用于曲线指令与长指令）
	 */
	private String seq;
	/**
	 * 通过返回指令得出的执行结果
	 */
	private boolean isSuccess;
	/**
	 * 返回指令中截取的异常码
	 * 只有执行失败的指令才可能有该值
	 */
	private String errCode;
	/**
	 * 是否超长
	 */
	private boolean isLonger;
	
	/**
	 * 解释详情
	 */
	private String detail;
	
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getCotrolCode() {
		return cotrolCode;
	}
	public void setCotrolCode(String cotrolCode) {
		this.cotrolCode = cotrolCode;
	}
	public String getDataMarker() {
		return dataMarker;
	}
	public void setDataMarker(String dataMarker) {
		this.dataMarker = dataMarker;
	}
	public String getDataValues() {
		return dataValues;
	}
	public void setDataValues(String dataItems) {
		this.dataValues = dataItems;
	}
	public int getDataLength() {
		return dataLength;
	}
	public void setDataLength(int dataLength) {
		this.dataLength = dataLength;
	}
	public String getDataField() {
		return dataField;
	}
	public void setDataField(String dataField) {
		this.dataField = dataField;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getFreezeTd() {
		return freezeTd;
	}
	public void setFreezeTd(String freezeTd) {
		this.freezeTd = freezeTd;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public boolean isLonger() {
		return isLonger;
	}
	public void setLonger(boolean isLonger) {
		this.isLonger = isLonger;
	}
	@Override
	public String toString() {
		return "DLT645Data [protocolType="+super.getProtocolType()+", addr=" + addr + ", cotrolCode=" + cotrolCode + ", dataLength=" + dataLength + ", dataField="
				+ dataField + ", dataMarker=" + dataMarker + ", password=" + password + ", operatorCode=" + operatorCode
				+ ", freezeTd=" + freezeTd + ", dataValues=" + dataValues + ", seq=" + seq + ", isSuccess=" + isSuccess
				+ ", errCode=" + errCode + ", isLonger=" + isLonger + ", detail=" + detail + "]";
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
}
