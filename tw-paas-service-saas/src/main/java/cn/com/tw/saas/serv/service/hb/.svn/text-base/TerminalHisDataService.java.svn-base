package cn.com.tw.saas.serv.service.hb;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.dao.TerminalHisDataDao;
import cn.com.tw.saas.serv.dao.TerminalLastDataDao;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.entity.terminal.SaasMeter;
import cn.com.tw.saas.serv.mapper.terminal.MeterMapper;
import cn.com.tw.saas.serv.mapper.terminal.SaasMeterMapper;

@Service
public class TerminalHisDataService {
	
	@Autowired
	private TerminalHisDataDao terminalHisDataDao;
	@Autowired
	private TerminalLastDataDao terminalLastDataDao; 
	
	/*@Autowired
	private SaasMeterMapper meterMapper;*/
	
	@Autowired
	private MeterMapper meterMapper;

	public Page selectTerminalHisDataPage(Page page) {
		// TODO Auto-generated method stub
		return null;
	}

	public void createHisTable() {
		terminalHisDataDao.createTermHisTable();
	}

	public void putRadHis(String jsonStr) {

		// 接收到的msg转为map格式
		JSONObject jsonMap =JSONObject.parseObject(jsonStr);
		// 仪表地址
		String meterAddr = (String) jsonMap.get("equipNumber");
		// 采集失败的msg
		if (StringUtils.isEmpty(meterAddr)) {
			return;
		}
		// 采集时间搓
		String readTimeStr =  jsonMap.getLong("lastSaveTime")+"";
		if (StringUtils.isEmpty(readTimeStr)) {
			return;
		}
		// msg中有价值的数据
		Map<String,Object> dataMap = (Map<String,Object>) jsonMap.get("data");
		if (null != dataMap && dataMap.size()>0) {
			String meterCateg = "";
			// 查询仪表
			Meter meter = meterMapper.selectByMeterAddr(meterAddr);
			String orgId = "";
			if (null != meter) {
				orgId = meter.getOrgId();
				meterCateg = meter.getEnergyType();
				dataMap.put("termNo", meterAddr);
				dataMap.put("orgId", orgId);
				if("110000".equals(meterCateg)){
					dataMap.put("meterType", meter.getMeterType());
				}
				dataMap.put("meterCateg", meterCateg);
				dataMap.put("updateTime", readTimeStr);
				dataMap.put("roomId", meter.getRoomId());
				
				//terminalHisDataDao.putRadHis(dataMap);
				terminalLastDataDao.putLast(dataMap);
			}
		}
	}
	
	

}
