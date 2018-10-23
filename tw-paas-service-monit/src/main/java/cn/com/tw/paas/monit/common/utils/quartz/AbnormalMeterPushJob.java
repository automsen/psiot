package cn.com.tw.paas.monit.common.utils.quartz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.tw.common.core.jms.cons.PsMqCons;
import cn.com.tw.common.core.jms.kafka.KafkaMqService;
import cn.com.tw.common.enm.notify.NotifyBusTypeEm;
import cn.com.tw.common.enm.notify.NotifyLvlEm;
import cn.com.tw.common.utils.notify.Notify;
import cn.com.tw.common.utils.notify.NotifyUtils;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.org.OrgExpand;
import cn.com.tw.paas.monit.entity.db.org.OrgApplication;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.entity.db.read.ReadHistory;
import cn.com.tw.paas.monit.service.inn.CmdIssueService;
import cn.com.tw.paas.monit.service.org.AbnormalMeterPushService;
import cn.com.tw.paas.monit.service.org.OrgApplicationService;
import cn.com.tw.paas.monit.service.org.OrgService;
import cn.com.tw.paas.monit.service.org.TerminalEquipService;
import cn.com.tw.paas.monit.service.read.ReadService;

@Component
public class AbnormalMeterPushJob {
	
	@Autowired
	private KafkaMqService mqHandler;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private OrgApplicationService orgApplicationService;
	
	@Autowired
	private TerminalEquipService terminalEquipService;
	
	@Autowired
	private ReadService readService;
	
	@Autowired
	private CmdIssueService cmdIssueService;
	
	@Autowired
	private AbnormalMeterPushService abnormalMeterPushService;
	
	private static Logger log = LoggerFactory.getLogger(AbnormalMeterPushJob.class);
	
	public AbnormalMeterPushJob() {
		/*this.orgService = (OrgService) SpringContext.getBean("orgServiceImpl");
		this.orgApplicationService = (OrgApplicationService) SpringContext.getBean("orgApplicationServiceImpl");
		this.terminalEquipService = (TerminalEquipService) SpringContext.getBean("terminalEquipServiceImpl");
		this.readService = (ReadService) SpringContext.getBean("readServiceImpl");
		this.cmdIssueService = (CmdIssueService) SpringContext.getBean("cmdIssueServiceImpl");*/
		//this.mqHandler = (MqHandler)SpringContext.getBean("kafkaMqService");
		//this.abnormalMeterPushService = (AbnormalMeterPushService) SpringContext.getBean("abnormalMeterPushService");
	}
	
	//@Scheduled(cron = " 0 0/30 * * * ? ")
	public void scheduled(){
		//List<OrgExpand> list = selectOrgList();
		List<OrgApplication> appList = selectAppList();
		for (OrgApplication app : appList) {
			List<String> dataList = selectMeterByOrgId(app.getOrgId());
			for (String equipNumnerAndEquipType : dataList) {
				issueToOrg(equipNumnerAndEquipType, app.getOrgId(), app.getCallbackUrl());
			}
		}
	}
	
	//查询机构
	public List<OrgExpand> selectOrgList() {
		return orgService.selectOrgAll(null);
	}
	
	//查询机构应用ID
	public List<OrgApplication> selectAppList() {
		OrgApplication orgApplication = new OrgApplication();
		String orgId = "1008";//不包含工厂机构
		orgApplication.setNotIn(orgId);
		return orgApplicationService.selectAll(orgApplication);
	}
	
	//通过机构查询下联仪表
	//防止数据量过大，采用分页查询方式，每页1000条
	public List<String> selectMeterByOrgId(String orgId) {
		Page page = new Page();
		List<String> meterNumbers = new ArrayList<String>();
		int row = 1000;
		page.setRows(row);
		//查询该机构下仪表总数
		int totalNum = terminalEquipService.selectMeterTotalNum(orgId);
		if(totalNum != 0){
			//double totalPage = (double)totalNum / (double)row;
			int totalPage = (totalNum % row == 0 ? totalNum / row : totalNum / row + 1);
			for(int i=1; i <= totalPage; i++){
				//List<String> meterIdList = new ArrayList<String>();
				page.setPage(i);//当前页码
				List<TerminalEquip> list = terminalEquipService.selectByPage(page);//1000块仪表
				selectLastTime(list, meterNumbers);//(拼接要查询的表号, 表厂数据总集合)
			}
		}
		return meterNumbers;
	}
	
	
	//循环仪表查询采集频率，最后一次采集时间
	@SuppressWarnings("unchecked")
	public void selectLastTime(List<TerminalEquip> list, List<String> meterNumbers){
		StringBuffer sb = new StringBuffer();
		List<Map<String, String>> meterList = new ArrayList<>();
		try {
			for (TerminalEquip terminalEquip : list) {
				//如果仪表没有设置采集频率，直接跳过
				if(terminalEquip.getGatherHz() != null){
					Map<String, String> map = new HashMap<>();
					map.put("equipNumber", terminalEquip.getEquipNumber());
					map.put("gatherHz", terminalEquip.getGatherHz());
					sb = sb.append(terminalEquip.getEquipNumber() + ",");
					meterList.add(map);
				}
			}
			sb.deleteCharAt(sb.length() - 1);
			
			ReadHistory rh = new ReadHistory();
			rh.setEquipNumbers(sb.toString());
			rh.setItemCode("tNo,rTm,tTy");//rTm读取采集时间
			Response<?> result = abnormalMeterPushService.selectMeterDataToHB(rh);
			/*String reqContent = JsonUtils.objectToJson(rh);
			StringEntity entity = new StringEntity(reqContent, "utf-8");//解决中文乱码问题    
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
	        String url = "http://localhost:8084/read/last/multi";
	        HttpPostReq hp = new HttpPostReq(url, null, entity, 60000, 2000);
	        String req =  hp.excuteReturnStr();*/
	        if(result != null){
	        	List<Map<String, Object>> mapList = JsonUtils.jsonToPojo(JsonUtils.objectToJson(result.getData()), new ArrayList<Object>().getClass());
	        	Date date = new Date();
	        	for (Map<String, Object> map : mapList) {
	        		String meterNumber = (String) map.get("tNo");
	        		Long time = (Long) map.get("rTm");
	        		String equipType = (String) map.get("tTy");
	        		Long nowTime = date.getTime();
	        		Long num = nowTime - time;
	        		for (Map<String, String> equipData : meterList) {
	        			String Hz = equipData.get("gatherHz");
	        			if(meterNumber.equals(equipData.get("equipNumber")) && num > (Long.valueOf(Hz)*60*1000*2)){
    	        			meterNumbers.add(meterNumber+","+equipType);
	        			}
					}
				}
	        }
		} catch (Exception e) {
			log.error("selectLastTime {}", e);
			e.printStackTrace();
		}
	}
	
	//下发到机构
	public void issueToOrg(String equipNumnerAndEquipType, String orgId, String url){
		if(equipNumnerAndEquipType != null){
			String[] dataArr = equipNumnerAndEquipType.split(",");
			// 发送回调不做任何数据操作
			String returnJson = NotifyUtils.sendTermEventMsg(dataArr[0], dataArr[1], null,NotifyBusTypeEm.termNoContactError, null,
					System.currentTimeMillis() );
			String pushStr = Notify.createEvent(orgId, url, returnJson, 1)
					.setPaasBusData(NotifyBusTypeEm.termNoContactError, null, null, null, orgId, NotifyLvlEm.HIGH)
					.toJsonStr();
			mqHandler.send(PsMqCons.QUEUE_NOTIFY_NAME, pushStr);
		}
	}
}
