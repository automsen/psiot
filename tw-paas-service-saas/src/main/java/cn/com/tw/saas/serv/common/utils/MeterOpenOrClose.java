package cn.com.tw.saas.serv.common.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.utils.bean.SpringContext;
import cn.com.tw.saas.serv.common.utils.cons.ApiTemplateCons;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.service.command.CmdRecordService;
import cn.com.tw.saas.serv.service.terminal.MeterService;

public class MeterOpenOrClose implements Delayed, Runnable{

	@Autowired
	private MeterService meterService;
	
	@Autowired
	private CmdRecordService cmdService;
	
	/**
	 * 通断类型，0通1短
	 */
	private byte isOpen;
	
	/**
	 * 通段时间
	 */
	private Date time;
	
	/**
	 * 通段时间时间戳
	 */
	private long timeLong;
	/**
	 * 机构id
	 */
	private String orgId;
	
	public MeterOpenOrClose(byte isOpen, Date time, String orgId){
		this.meterService = (MeterService) SpringContext.getBean("meterServiceImpl");
		this.cmdService = (CmdRecordService) SpringContext.getBean("cmdRecordServiceImpl");
		this.isOpen = isOpen;
		this.time = time;
		this.orgId = orgId;
		this.timeLong = this.time.getTime();
	}
	
	public List<Meter> selectMeter(){
		Meter meter = new Meter();
		meter.setIsElecOnTime((byte) 1);
		meter.setOrgId(orgId);
		List<Meter> list = meterService.selectByEntity(meter);
		return list;
	}
	
	/**
	 * 请求关闸
	 * @param list
	 */
	public void openSwitch(List<String> list){
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("equipNumbers", JsonUtils.objectToJson(list));
		cmdService.generateCmdUrlNoAddr(ApiTemplateCons.switchOn, "", requestMap);
	}
	
	/**
	 * 请求开闸
	 * @param list
	 * @return 
	 */
	public void closeSwitch(List<String> list){
		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("equipNumbers", JsonUtils.objectToJson(list));
		cmdService.generateCmdUrlNoAddr(ApiTemplateCons.switchOff, "", requestMap);
	}
	
	@Override
	public int compareTo(Delayed o) {
		MeterOpenOrClose task = (MeterOpenOrClose) o;
		return timeLong > task.timeLong ? 1 : (timeLong < task.timeLong ? -1 : 0);
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(timeLong - System.currentTimeMillis(), TimeUnit.SECONDS);
	}

	/**
	 * 执行通断逻辑
	 */
	@Override
	public void run() {
		List<Meter> equipNumbers = selectMeter();
		if(null != equipNumbers && 0 > equipNumbers.size()){
			List<String> list = new ArrayList<String>();
			for (Meter meter : equipNumbers) {
				list.add(meter.getEquipNumber());
			}
			if(isOpen == 0){
				openSwitch(list);
			}else{
				closeSwitch(list);
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MeterOpenOrClose) {
			MeterOpenOrClose mooc = (MeterOpenOrClose) obj;
			return (timeLong == (mooc.timeLong) && orgId.equals(mooc.orgId));
        }
		return super.equals(obj);
	}
}
