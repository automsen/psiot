package cn.com.tw.paas.monit.service.statis;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.com.tw.common.utils.tools.quartz.utils.DateFormatUtil;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.entity.statis.EquipStatis;
import cn.com.tw.paas.monit.mapper.org.TerminalEquipMapper;
import cn.com.tw.paas.monit.mapper.statis.EquipStatisMapper;

@Service
public class StatisService {
	
	@Autowired
	private EquipStatisMapper equipStatisMapper;
	
	@Autowired
	private TerminalEquipMapper terminalEquipMapper;
	
	public void queryEquipStatisByPage(Page page) {
		List<EquipStatis> equipStatisList = equipStatisMapper.selectByPage(page);
		page.setData(equipStatisList);
	}
	
	public void insertEquipStatis(EquipStatis equipStatis) {
		equipStatisMapper.insert(equipStatis);
	}
	
	public void updateEquipStatisCollectNum(String dayTime, String equipNumber) {
		EquipStatis equipStatis = new EquipStatis();
		equipStatis.setDayTime(dayTime);
		equipStatis.setEquipNumber(equipNumber);
		equipStatis.setUpdateTime(new Date());
		TerminalEquip termEquip = terminalEquipMapper.selectByEquipNumber(equipNumber);
		
		if (termEquip == null) {
			//如果为空，则插入
			//insert
		}
		
		//获取最新采集次数
		EquipStatis retequipStatis = equipStatisMapper.queryByEquip(equipStatis);
		
		//获取采集频率
		String gatherHz = termEquip.getGatherHz();
		String equipType = termEquip.getEquipTypeCode();
		
		equipStatis.setCollectFreq(Integer.parseInt(gatherHz));
		
		int gatherHzMM = 10;
		
		//暂时加在这里
		if ("110000".equals(equipType)) {
    		gatherHzMM = 10;
    	} else if ("120000".equals(equipType)) {
    		gatherHzMM = 8 * 60;
    	} 
		
		if (!StringUtils.isEmpty(gatherHz)) {
			gatherHzMM = Integer.parseInt(gatherHz);
		}
		
		equipStatis.setCollectFreq(gatherHzMM);
    	
    	int evaNum = 24 * (60 / gatherHzMM);
    	
    	Date curDate = new Date();
    	String curDateStr = DateFormatUtil.format("yyyyMMdd", curDate);
    	if (curDateStr.equals(dayTime)) {
    		int curMM = getDateMM(curDate);
    		
    		if (curMM < gatherHzMM) {
    			evaNum = 1;
    		}else {
    			evaNum = curMM / gatherHzMM + 1;
    		}
    		
    	}
    	
    	int collectNum = retequipStatis.getCollectNum() == null ? 0 : retequipStatis.getCollectNum();
		equipStatis.setCollectSuccRate(new BigDecimal((Math.round((collectNum + 1) / evaNum * 10000) / 100.00)));
		equipStatisMapper.updateByPrimaryKeySelective(equipStatis);
	}
	
	private int getDateMM(Date date){
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    return calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);

	}
	
	public void updateEquipStatis(EquipStatis equipStatis){
		equipStatisMapper.updateByPrimaryKeySelective(equipStatis);
	}

	public static void main(String[] args) {
		Integer a = null;
		System.out.println(a + 1);
	}
}
