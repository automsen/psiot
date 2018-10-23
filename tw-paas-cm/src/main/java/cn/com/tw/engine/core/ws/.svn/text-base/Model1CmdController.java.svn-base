package cn.com.tw.engine.core.ws;

import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.SysCons;
import cn.com.tw.engine.core.entity.CmdRequest;
import cn.com.tw.engine.core.entity.CmdResponse;
import cn.com.tw.engine.core.exception.EngineException;
import cn.com.tw.engine.core.exception.code.EngineCode;
import cn.com.tw.engine.core.ws.service.CmdService;

@RestController
@RequestMapping("model1")
public class Model1CmdController {
	
	private static Logger logger = LoggerFactory.getLogger(Model1CmdController.class);
	
	@Autowired
	private CmdService cmdService;
	
	@PostMapping("contr")
	public CmdResponse acceptCmd(@RequestBody CmdRequest req){
		
		logger.debug(SysCons.LOG_START + " acceptCmd req = " + (req == null ? null : req.toString()));
		
		if (StringUtils.isEmpty(req.getMeterAddr()) || StringUtils.isEmpty(req.getGwId()) || StringUtils.isEmpty(req.getMeterType())){
			
			logger.error("**************** param error, all params must be not empty!!");
			
			return CmdResponse.retn(EngineCode.CHANNEL_PARAMS_ERROR, "param error, all params must be not empty!!");
			
		}
		
		if (req.getCmdLvl() < 20 || req.getCmdLvl() > 29){
			
			logger.error("**************** param error, cmdLvl:{} is error, (10 for read, 20 for write)", req.getCmdLvl());
			
			return CmdResponse.retn(EngineCode.CHANNEL_PARAMS_ERROR, "param error, cmdLvl: " + req.getCmdLvl() + " is error, (10 for read, 20.. for write)");
			
		}
		
		try {
			
			CmdResponse resp = cmdService.send(req);
			logger.debug(SysCons.LOG_END + " acceptCmd req complete !!, resp = {}", (resp == null ? "" : resp.toString()));
			return resp;
		} catch (EngineException e) {
			logger.error("************** acceptCmd req EngineException, e = {}", e);
			return CmdResponse.retn(e.getCode(), e.getMessage());
		} catch (ExecutionException e) {
			Object obj = e.getCause();
			if (obj instanceof EngineException){
				EngineException nge = (EngineException) obj;
				logger.error("************** acceptCmd req EngineException, e = {}", e);
				return CmdResponse.retn(nge.getCode(), nge.getMessage());
			}
			logger.error("************** acceptCmd req ExecutionException, e = {}", e);
		} catch (Exception e) {
			logger.error("************** acceptCmd req exception, e = {}", e);
		}
		
		return CmdResponse.retn(EngineCode.CHANNEL_UNKNOW);
	}
}
