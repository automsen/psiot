package cn.com.tw.saas.serv.controller.audit;

import java.util.List;

import javax.validation.Valid;
import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.exception.RequestParamValidException;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.audit.AuditRoom;
import cn.com.tw.saas.serv.service.audit.AuditRoomService;

@RestController
@RequestMapping("auditRoom")
@Api(description = "房间审核")
public class AuditRoomController {

	@Autowired
	private AuditRoomService auditRoomService;
	
	/**
	 * 签约变更 同用
	 * @param page
	 * @return
	 */
	@GetMapping("page")
	public Response<?> selectByPage(Page page){
		List<AuditRoom> auditRooms = auditRoomService.selectByPage(page);
		page.setData(auditRooms);
		return Response.ok(page);
	}
	
	@GetMapping("{id}")
	public Response<?> selectById(@PathVariable String id){
		AuditRoom auditRoom = auditRoomService.selectById(id);
		return Response.ok(auditRoom);
	}
	
	/**
	 * 房间审核通过
	 * @return
	 */
	@PutMapping("pass")
	public Response<?> passAuditRoom(@RequestBody @Valid AuditRoom auditRoom, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), "参数异常");
		}
		try {
			auditRoomService.passAuditRoom(auditRoom);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 房间审核驳回
	 * @return
	 */
	@PutMapping("back")
	public Response<?> backAuditRoom(@RequestBody @Valid AuditRoom auditRoom, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), "参数异常");
		}
		try {
			auditRoomService.backAuditRoom(auditRoom);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
}
