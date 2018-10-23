package cn.com.tw.saas.serv.common.utils.cons;
/**
 * 阿里云短信模板
 * @author admin
 *
 */
public class AliSmsTemplateCons {
	/**
	 * 验证码短信
	 */
	public static AliSmsTemplate verify = new AliSmsTemplate("时波网络","SMS_140625141","{\"code\":\"{}\"}");
}
