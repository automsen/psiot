package cn.com.tw.saas.serv.controller.command;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.command.CmdRecord;
import cn.com.tw.saas.serv.service.command.CmdRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("cmdRecord")
@Api(description = "命令执行记录")
public class CmdRecordController {
	
	@Autowired
	private CmdRecordService cmdService;

	/**
	 * 主键查询
	 * @param meterAddr
	 * @return
	 */
	@GetMapping("/{cmdId}")
	@ApiOperation(value="主键查询", notes="")
	public Response<?> selectOne(@PathVariable String cmdId) {
		if (StringUtils.isEmpty(cmdId)) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		CmdRecord result = cmdService.selectById(cmdId);
		return Response.ok(result);
	}
	
	@GetMapping("page")
	@ApiOperation(value = "分页查询", notes = "")
	public Response<?> selectByPage(Page page) {
		List<CmdRecord> scList = cmdService.selectByPage(page);
		page.setData(scList);
		return Response.ok(page);
	}
	
	@PostMapping("retry")
	@ApiOperation(value = "重试", notes = "")
	public Response<?> retryCmd(String cmdId) {
		cmdService.retryCmd(cmdId);
		return Response.ok();
	}
}
