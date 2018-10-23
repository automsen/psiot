package cn.com.tw.saas.serv.common.utils.cons;

/**
 * API调用模板
 * @author admin
 *
 */
public class ApiTemplate {

	/**
	 * 访问路径
	 */
	private String url;
	
	/**
	 * 接口名
	 */
	private String apiName;
	
	/**
	 * 必要参数个数
	 */
	private int paramNum;
	
	/**
	 * 回调处理类
	 */
	private String callbackClass;
	
	/**
	 * 命令代码
	 * 非必填
	 */
	private String cmdCode;
	
	public ApiTemplate(String url,String apiName,int paramNum,String callbackClass,String cmdCode){
		this.url = url;
		this.apiName = apiName;
		this.paramNum = paramNum;
		this.callbackClass = callbackClass;
		this.cmdCode = cmdCode == null ? null : cmdCode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public int getParamNum() {
		return paramNum;
	}

	public void setParamNum(int paramNum) {
		this.paramNum = paramNum;
	}

	public String getCallbackClass() {
		return callbackClass;
	}

	public void setCallbackClass(String callbackClass) {
		this.callbackClass = callbackClass;
	}

	public String getCmdCode() {
		return cmdCode;
	}

	public void setCmdCode(String cmdCode) {
		this.cmdCode = cmdCode;
	}
}
