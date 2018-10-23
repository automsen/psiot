package cn.com.tw.paas.monit.controller.org;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.core.cache.redis.RedisService;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.db.org.CmdExe;
import cn.com.tw.paas.monit.entity.db.org.InsExe;
import cn.com.tw.paas.monit.service.inn.CmdIssueService;
import cn.com.tw.paas.monit.service.org.CmdExeService;
import cn.com.tw.paas.monit.service.org.InsExeService;

/**
 * 命令指令状态更新
 * @author liming
 * 2018年8月24日 15:44:07
 */

@RestController
@RequestMapping("cmd")
public class OrgCmdExeController {

	@Autowired
	private CmdExeService cmdExeService;
	
	@Autowired
	private InsExeService insExeService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private CmdIssueService cmdIssueService;
	
	private final String RETRY_KEY = "ps:retrycmd";
	
	@RequestMapping("page")
	public Response<?> pageList( Page page){
		List<CmdExe> cmds = cmdExeService.selectByPage(page);
		page.setData(cmds);
		return Response.ok(page);
	}
	
	@RequestMapping("retryCmd/{cmdId}")
	public Response<?> retryCmd(@PathVariable("cmdId")String cmdId){
		CmdExe cmd = cmdExeService.findCmdIns(cmdId);
		if(cmd== null){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "命令不存在！");
		}
		Integer handleTimes = cmd.getHandleTimes();
		Integer limtHandleTimes = cmd.getLimitHandleTimes();
		if(handleTimes != null && handleTimes < limtHandleTimes){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "正在执行，请稍后查看结果！");
		}
		try {
			cmd.setHandleTimes(1); //设置为1，防止直接被抛弃
			cmdExeService.updateCmdExeSelective(cmd);
			cmdIssueService.sendCmdToRedis(cmd, null); 
			return Response.ok();
		} catch (Exception e) {
			return Response.retn(ResultCode.UNKNOW_ERROR, "系统异常！");
		}
	}
	
	@GetMapping("innDetail/{cmdId}")
	public Response<?> innDetail(@PathVariable("cmdId")String cmdId){
		List<InsExe> ins = null;
		try {
			InsExe queryModel = new InsExe();
			queryModel.setCmdExeId(cmdId);
			ins = insExeService.selectByEntity(queryModel);
			if(ins== null){
				return Response.retn(ResultCode.PARAM_VALID_ERROR, "指令执行情况不存在！");
			}
			return Response.ok(ins);
		} catch (Exception e) {
			return Response.retn(ResultCode.UNKNOW_ERROR, "系统异常！");
		}
	}
	
	
	@PostMapping("updateCmdStatus")
	public Response<?> updateCmdStatus(@RequestBody CmdExe cmd){
		if(StringUtils.isEmpty(cmd.getId())){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR,"cmdId can not be empty!");
		}
		if(StringUtils.isEmpty(cmd.getStatus())){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR,"status can not be empty!");
		}
		try {
			cmdExeService.updateCmdInsStatus(cmd);
		} catch (Exception e) {
			 return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR,"system error！");
		}
		return Response.ok();
	}
	
	/**
	 *  获取命令指令相关信息
	 * @param cmdId
	 * @return
	 */
	@PostMapping("findCmdIns")
	public Response<?> findCmdIns(@RequestParam("cmdId")String cmdId){
		if(StringUtils.isEmpty(cmdId)){
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR,"cmdId can not be empty!");
		}
		CmdExe cmd = null;
		try {
			cmd = cmdExeService.findCmdIns(cmdId);
			return Response.ok(cmd); 
		} catch (Exception e) {
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR,"system error！");
		}
	}
	
	
}
