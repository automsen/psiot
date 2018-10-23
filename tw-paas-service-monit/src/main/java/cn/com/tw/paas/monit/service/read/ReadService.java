package cn.com.tw.paas.monit.service.read;

import java.util.List;
import java.util.Map;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.read.ReadHistoryExtend;
import cn.com.tw.paas.monit.entity.business.read.ReadHistorySimple;
import cn.com.tw.paas.monit.entity.db.read.ReadHistory;
import cn.com.tw.paas.monit.entity.db.read.ReadLast;

public interface ReadService {
	/**
	 * 保存最近读数
	 * @param uniqueId
	 * @param dataMap
	 */
	public void saveRead(String uniqueId, Map<String, Object> dataMap);

	/**
	 * 查询最近读数
	 * @param readLast
	 * @return
	 */
	public List<ReadLast> selectLast(ReadLast readLast);

	/**
	 * 查询历史数据（分页）
	 * @param page
	 * @return
	 */
	public List<ReadHistory> selectHistoryPage(Page page);
	
	/**
	 * 查询历史数据（分页）
	 * @param page
	 * @return
	 */
	public List<ReadHistory> selectHistoryShow(Page page);
	
	/**
	 * 查询历史数据
	 * @param param
	 * @return
	 */
	public List<ReadHistory> selectHistory(ReadHistoryExtend param);
	

	ReadLast selectByAddrAndItem(ReadLast record);
	/**
	 * 查询指定时间至今的指定仪表的真实读数记录
	 * @param meterAddr
	 * @param freezeTd
	 * @param dataItem
	 * @return
	 */
//	public List<ReadHistory> selectByAddrAndTd(String meterAddr,String freezeTd,String dataItem);

	List<ReadHistorySimple> selectHistoryForApi(Page param);

	public List<ReadLast> selectRealTimeTrace(String meterAddr);

	public List<ReadHistoryExtend> selectTrace(ReadHistoryExtend param);

	public List<ReadLast> selectReadLast(Page pageData);

	public List<ReadHistory> select24hours(ReadHistoryExtend readHistoryExtend);

	List<ReadLast> selectByMeterAddr(String meterAddr);
	
}
