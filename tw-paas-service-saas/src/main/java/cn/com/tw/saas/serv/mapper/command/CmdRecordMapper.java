package cn.com.tw.saas.serv.mapper.command;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.command.CmdRecord;

public interface CmdRecordMapper{
	int deleteByPrimaryKey(String arg0);

	int insert(CmdRecord arg0);

	CmdRecord selectByPrimaryKey(String arg0);

	int updateByPrimaryKeySelective(CmdRecord arg0);

	List<CmdRecord> selectByPage(Page arg0);
}