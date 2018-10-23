package cn.com.tw.saas.serv.controller.audit;

import java.util.List;

import javax.validation.Valid;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.exception.RequestParamValidException;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.audit.AuditAccount;
import cn.com.tw.saas.serv.service.audit.AuditAccountService;

@RestController
@RequestMapping("audit")
@Api(description = "审核")
public class AuditAccountController {
	
	@Autowired
	private AuditAccountService auditAccountSrevice;
	
	@GetMapping("page")
	public Response<?> selectByPage(Page page){
		List<AuditAccount> auditAccounts = auditAccountSrevice.selectByPage(page);
		page.setData(auditAccounts);
		return Response.ok(page);
	}
	
	@GetMapping("")
	public Response<?> selectByRoomId(AuditAccount auditAccount){
		List<AuditAccount> auditAccounts = auditAccountSrevice.selectByEntity(auditAccount);
		return Response.ok(auditAccounts);
	}
	
	/**
	 * 通过   旧的审核通过
	 * @param auditAccount
	 * @param br
	 * @return
	 */
	@PutMapping("")
	@Deprecated
	public Response<?> updateAuditAccount(@RequestBody @Valid AuditAccount auditAccount, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), "参数异常");
		}
		try {
			auditAccountSrevice.updateSelect(auditAccount);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 驳回状态修改  久的驳回
	 * @param auditAccount
	 * @param br
	 * @return
	 */
	@PutMapping("back")
	@Deprecated
	public Response<?> updateAuditStatus(@RequestBody @Valid AuditAccount auditAccount, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), "参数异常");
		}
		try {
			auditAccountSrevice.updateAuditStatus(auditAccount);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}

}
