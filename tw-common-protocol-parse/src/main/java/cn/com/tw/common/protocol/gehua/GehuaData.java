package cn.com.tw.common.protocol.gehua;

import java.io.Serializable;
import cn.com.tw.common.protocol.ProtoData;

public class GehuaData extends ProtoData implements Serializable{

	private static final long serialVersionUID = -7782348484082070346L;
	
	private String sourceCode;
	private String cotrolWord;
	private String dataMarker;
	private String dataJson;
	
	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getCotrolWord() {
		return cotrolWord;
	}

	public void setCotrolWord(String cotrolWord) {
		this.cotrolWord = cotrolWord;
	}

	public String getDataMarker() {
		return dataMarker;
	}

	public void setDataMarker(String dataMarker) {
		this.dataMarker = dataMarker;
	}

	public String getDataJson() {
		return dataJson;
	}

	public void setDataJson(String dataJson) {
		this.dataJson = dataJson;
	}

	@Override
	public String toString() {
		return "GehuaData [protocolType="+super.getProtocolType()+", sourceCode=" + sourceCode + ", cotrolWord=" + cotrolWord + ", dataMarker=" + dataMarker
				+ ", dataJson=" + dataJson + "]";
	}
}