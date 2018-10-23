package cn.com.tw.saas.serv.common.utils.quartz.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.com.tw.common.web.utils.bean.SpringContext;
import cn.com.tw.saas.serv.common.utils.DateStrUtils;
import cn.com.tw.saas.serv.common.utils.MeterOpenOrClose;
import cn.com.tw.saas.serv.common.utils.queue.AsyncTaskDelayQueue;
import cn.com.tw.saas.serv.entity.loadShedding.RuleElecOnTimeUnusual;
import cn.com.tw.saas.serv.service.loadShedding.RuleElecOnTimeUnusualService;

@Component
public class NotAllDayPowerSupplyJob{
	
	@Autowired
	private RuleElecOnTimeUnusualService ruleElecOnTimeUnusualService;
	
	
	public NotAllDayPowerSupplyJob() {
		this.ruleElecOnTimeUnusualService = (RuleElecOnTimeUnusualService) SpringContext.getBean("ruleElecOnTimeUnusualServiceImpl");
	}
	
	// 0 0/2 * * * ?
	@Scheduled(cron = "0 0 0 1/1 * ?")
	public void scheduled(){
		List<RuleElecOnTimeUnusual> list = new ArrayList<RuleElecOnTimeUnusual>();
		RuleElecOnTimeUnusual unusual = new RuleElecOnTimeUnusual();
		unusual.setTimeNow(new Date());
		list = ruleElecOnTimeUnusualService.selectBySomeDay(unusual);
		if(null != list && list.size() > 0){
			/*Calendar now = Calendar.getInstance();
			String month = String.valueOf(now.get(Calendar.MONTH) + 1);
			if(month.length() < 2){
				month = "0" + month;
			}
			String day = String.valueOf(now.get(Calendar.DAY_OF_MONTH));
			if(day.length() < 2){
				day = "0" + day;
			}
			String date = now.get(Calendar.YEAR) + month + day*/;
			//String date = DateStrUtils.dateToTd(new Date(),"dayFormat");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
			String date = sdf1.format(new Date());
			try {
				for (RuleElecOnTimeUnusual un : list) {
					if(null != un.getOnTime() && !"".equals(un.getOnTime())){
						Date time = sdf.parse(date+un.getOnTime()+"00");
						MeterOpenOrClose mooc = new MeterOpenOrClose((byte) 0, time, un.getOrgId());
						AsyncTaskDelayQueue.getInstance().put(mooc);
					}
					if(null != un.getOffTime() && !"".equals(un.getOffTime())){
						Date time = sdf.parse(un.getOffTime()+"00");
						MeterOpenOrClose mooc = new MeterOpenOrClose((byte) 1, time, un.getOrgId());
						AsyncTaskDelayQueue.getInstance().put(mooc);
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

}
