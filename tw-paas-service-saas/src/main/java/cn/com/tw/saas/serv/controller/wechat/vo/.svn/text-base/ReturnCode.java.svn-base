package cn.com.tw.saas.serv.controller.wechat.vo;

public enum ReturnCode {
	SUCCESS_RETURN("000000", "返回成功消息"),
	ERROR_PARAM_FORAMT("111000","参数格式错误"),
	ERROR_PARAM_NOT_EXIST("111001","参数不存在"),
	ERROR_PARAM_NOT_BLANK("111002","参数不能为空"),
	ERROR_PARAM_NOT_CELLPHONE("111003","手机号不存在"),
	ERROR_AUTHORIZATION("112000","数字签名验证失败"),
	ERROR_NOT_EXIST("113000","接口不存在"),
	ERROR_CONN("114000","微信通信异常"),
	ERROR_BUS("114001","微信业务失败"),
	ERROR_BUS_CREATE("115001","本地第三方订单生成失败"),
	ERROR_ALI_CONN("116000","支付宝通信异常"),
	ERROR_ALI_FAILED("116001","支付宝业务失败"),
	ERROR_ALI_UNKNOW("116001","支付宝业务失败"),
	ERROR_EXCEPTION("116000","系统执行异常");
	private String code;
	
	private String msg;
	
	ReturnCode(String code, String msg){
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}
}
