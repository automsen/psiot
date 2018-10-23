package cn.com.tw.saas.serv.service.maint;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.read.ReadManual;

public interface ReadManualService {
	/**
	 * 查询人工抄表记录
	 * @param param
	 * @return
	 */
	public List<ReadManual> selectByPage(Page param);

	public int insertOne(ReadManual param);

	public int insertList(List<ReadManual> recordList);

	public List<ReadManual> selectAppReadManual(String regionNo);
}
