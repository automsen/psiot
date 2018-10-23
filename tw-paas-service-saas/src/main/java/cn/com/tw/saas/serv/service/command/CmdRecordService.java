package cn.com.tw.saas.serv.service.command;

import java.util.List;
import java.util.Map;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.cons.ApiTemplate;
import cn.com.tw.saas.serv.entity.business.command.PageCmdResult;
import cn.com.tw.saas.serv.entity.command.CmdRecord;

public interface CmdRecordService{

	void retryCmd(String cmdId);

	PageCmdResult generateCmd(ApiTemplate temp, String meterAddr, Map<String, String> requestMap);

	int deleteById(String paramString);

	List<CmdRecord> selectByPage(Page paramPage);

	int insert(CmdRecord paramT);

	int updateSelect(CmdRecord paramT);

	CmdRecord selectById(String arg0);

	PageCmdResult generateCmdUrlNoAddr(ApiTemplate temp, String meterAddr, Map<String, String> requestMap);

}
