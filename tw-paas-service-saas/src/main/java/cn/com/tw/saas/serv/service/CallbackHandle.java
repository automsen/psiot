package cn.com.tw.saas.serv.service;

import cn.com.tw.saas.serv.entity.command.CmdRecord;

public interface CallbackHandle {

	public abstract void callbackSuccess( CmdRecord commands,String result);
	
	public abstract void callbackError(CmdRecord commands,String result);
	
	public void handleCallback(CmdRecord commands,String result);
}
