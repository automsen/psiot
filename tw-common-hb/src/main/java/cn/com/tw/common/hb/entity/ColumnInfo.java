package cn.com.tw.common.hb.entity;

import org.springframework.util.StringUtils;

public class ColumnInfo {
	
	private String colfamily;
	
	private String colName;
	
	private String colValue;
	
	public ColumnInfo(){
		
	}
	
	public ColumnInfo(String colfamily, String colName, String colValue) {
		this.colfamily = colfamily;
		this.colName = colName;
		this.colValue = colValue;
	}
	
	public boolean isFdEmpty(){
		if (StringUtils.isEmpty(colfamily) || StringUtils.isEmpty(colName) || StringUtils.isEmpty(colValue)) {
			return true;
		}
		
		return false;
	}

	public String getColfamily() {
		return colfamily;
	}

	public void setColfamily(String colfamily) {
		this.colfamily = colfamily;
	}

	public String getColName() {
		return colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	public String getColValue() {
		return colValue;
	}

	public void setColValue(String colValue) {
		this.colValue = colValue;
	}

	@Override
	public String toString() {
		return "ColumnInfo [colfamily=" + colfamily + ", colName=" + colName
				+ ", colValue=" + colValue + "]";
	}
	
}
