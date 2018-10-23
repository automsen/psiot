package cn.com.tw.common.utils.cons;

/**
 * @author admin
 */
public class ResultCode {
    /* 定义规范，成功已后缀SUCCESS结尾，失败已后缀ERROR结尾*/
	
	/**
	 * 返回成功消息
	 */
	public static final String OPERATION_IS_SUCCESS = "000000";
	
	/**
	 * 参数校验错误
	 */
	public static final String PARAM_VALID_ERROR = "000997";
	
    /**
     * 数据库操作失败
     */
    public static final String DATABASE_OPERATIOIN_IS_ERROR= "000998";
    
    /**
     * 查询结果为空
     */
    public static final String DATA_IS_EMPTY_ERROR = "000990";
    
    /**
     * 未知错误(业务)
     */
    public static final String UNKNOW_ERROR = "000999";
    
    /**
     * 用户未登录
     */
    public static final String USER_NO_LOGIN_ERROR = "010999";
    
    /**
     * 远程调用异常
     */
    public static final String REMOTE_CALL_ERROR = "000996";
    
    /**
     * 服务需要交费使用 
     */
    public static final String SERVICE_EXPIRE_ERROR = "000111";
}
