package cn.com.tw.common.protocol.gehua.cons;

public enum GehuaControlWord {
	
	READ("82"),READ_OK("22"),READ_ERR("02"),
	WRITE("C2"),WRITE_OK("62"),WRITE_ERR("42"),
	UP("21"),
	EX_READ("90"),EX_READ_OK("30"),EX_READ_ERR("10"),
	EX_WRITE("D0"),EX_WRITE_OK("70"),EX_WRITE_ERR("50");
	
	private String value;
	
	GehuaControlWord(String value){
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
