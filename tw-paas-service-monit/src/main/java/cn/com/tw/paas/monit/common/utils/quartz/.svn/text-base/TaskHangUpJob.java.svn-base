package cn.com.tw.paas.monit.common.utils.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import cn.com.tw.paas.monit.entity.db.org.CmdExe;
import cn.com.tw.paas.monit.entity.db.org.InsExe;
import cn.com.tw.paas.monit.service.inn.CmdIssueService;
import cn.com.tw.paas.monit.service.org.CmdExeService;
import cn.com.tw.paas.monit.service.org.InsExeService;


/**
 *  失效任务挂起功能
 * @author liming
 * 2018年9月18日 15:33:20
 */
@Component
public class TaskHangUpJob {
	
	@Autowired
	private CmdExeService cmdExeService;
	
	@Autowired
	private CmdIssueService cmdIssueService;
	
	@Autowired
	private InsExeService insExeService;
	
	private static String HANGUP_LOG_START = "####################### HANGE UP";
	
	private static String HANGUP_LOG_END = "#######################";
	
	private static Logger log = LoggerFactory.getLogger(TaskHangUpJob.class);
	
	public TaskHangUpJob() {
	}
	
	@Scheduled(cron = "0 0/2 * * * ?")
	public void scheduled(){
		log.debug(HANGUP_LOG_START+"start"+HANGUP_LOG_END);
		CmdExe queryModel = new CmdExe();
		queryModel.setCanStartQuery("true"); // 待执行，执行失败，执行超时
		queryModel.setDelayMinute(5); // 5分钟之前的数据
		
		List<CmdExe> cmdExes = cmdExeService.selectByEntity(queryModel);
		log.debug(HANGUP_LOG_START+"cmd num({})"+HANGUP_LOG_END,cmdExes.size());
		for (CmdExe cmdExe : cmdExes) {
			hangUpCmdExe(cmdExe);
		}
	}
	
	private void hangUpCmdExe(CmdExe cmd){
		InsExe queryModel = new InsExe();
		queryModel.setCmdExeId(cmd.getId());
		List<InsExe> ins = insExeService.selectByEntity(queryModel);
		if(ins.isEmpty()){
			log.debug(HANGUP_LOG_START+"cmd{} ins empty"+HANGUP_LOG_END,cmd.getId());
			return;
		}
		InsExe tempIns = null;
		InsExe currIns = null;
		for (int i = 0,len = ins.size(); i < len; i++) {
			currIns = ins.get(i);
			if(i == 0){
				cmd.setCurrIns(currIns);
			}else{
				tempIns.setNextIns(currIns);
			}
			tempIns = currIns;
		}
		/**
		 *  命令执行相关缓存
		 */
		try {
			cmdIssueService.sendCmdToRedis(cmd, null);
			log.debug(HANGUP_LOG_START+"cmd{} start"+HANGUP_LOG_END,cmd.getId());
		} catch (Exception e) {
			log.debug(HANGUP_LOG_START+"cmd{} redis will be rollback,exception:{}"+HANGUP_LOG_END,cmd.getId(),e);
		}
	}
}
