package cn.com.tw.saas.serv.service.read;

import java.util.List;
import java.util.Map;

import cn.com.tw.saas.serv.entity.db.read.ReadLast;
import cn.com.tw.saas.serv.entity.db.read.ReadRecord;
import cn.com.tw.saas.serv.entity.db.read.VReadLastElec;
import cn.com.tw.saas.serv.entity.room.RoomMeterSwitchingManagent;

public interface ReadService {
	/**
	 * 查询指定时间至今的指定仪表的真实读数记录
	 * @param meterAddr
	 * @param freezeTd
	 * @param dataItem
	 * @param loopType 
	 * @return
	 */
	public List<ReadRecord> selectByAddrAndTd(String meterAddr,String freezeTd,String dataItem, String loopType);

	/**
	 * 保存最近读数
	 * @param jsonStr
	 */
	public void saveRead(String jsonStr);

	/**
	 * 查询仪表实时状态
	 * @param meterAddr
	 * @return
	 */
	public ReadLast readMeterStatus(String meterAddr);

	VReadLastElec selectReadLastElecView(VReadLastElec vReadLastElec);

	public void replaceThreeControlMeterData(List<RoomMeterSwitchingManagent> list, String openOrClose);

	/**
	 * 查询指定日期的仪表的真实读数记录
	 * @param meterAddr
	 * @param freezeTd
	 * @param dataItem
	 * @return
	 */
	public List<ReadRecord> selectByAddrAndTd1(String meterAddr, String freezeTd,String freezeTd1,
			String dataItem);

	public VReadLastElec selectReadLastElecView1(String meterAddr,
			String readTime);
	/**
	 * 查询仪表指定Item的记录
	 * @param meterAddr
	 * @param Item_code
	 * @return
	 */
	public ReadLast selectByAddrAndItem(String meterAddr,String Item_code);
	/**
	 * 查询指定时间段的需量数据
	 * @param meterAddr 仪表地址
	 * @return
	 */
	/**
	 * 查询需量数据
	 * @param paramMap 
	 * meterAddr 仪表地址
	 * startTime 开始时间
	 * endTime 结束时间
	 * @return
	 */
	public List<ReadRecord> selectDemandByMeterAddr(Map<String, Object> paramMap); 
}
