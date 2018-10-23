package cn.com.tw.saas.serv.controller.cust;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.business.cust.CustRoomParam;
import cn.com.tw.saas.serv.entity.view.serve.RoomExtend;
import cn.com.tw.saas.serv.service.cust.RoomAccountService;

@RestController
@RequestMapping("roomAccount")
@Api(description = "房间账户接口")
public class RoomAccountController {

	@Autowired
	private RoomAccountService roomAccountService;
	
	@GetMapping("page")
	@ApiOperation(value = "分页查询", notes = "")
	public Response<?> selectCustomerByPage(Page page) {
//		List<RoomAccount> scList = roomAccountService.selectByPage(page);
//		page.setData(scList);
		return Response.ok(page);
	}
	
	@ApiOperation(value = "条件查询，使用中", notes = "")
	@GetMapping("extend")
	public Response<?> selectForExtendList(CustRoomParam param){
		List<RoomExtend> result = roomAccountService.selectForExtendList(param);
		return Response.ok(result);
	}
	
	@ApiOperation(value = "条件查询，包括未使用房间", notes = "")
	@GetMapping("extend/all")
	public Response<?> selectForAllExtendList(CustRoomParam param){
		List<RoomExtend> result;
		// 查询条件有客户信息时，只查询客户信息可以匹配的使用中房间
		if (!StringUtils.isEmpty(param.getCustomerMobile1())||!StringUtils.isEmpty(param.getCustomerRealname())){
			result = roomAccountService.selectForExtendList(param);
		}
		else {
			result = roomAccountService.selectForAllExtendList(param);
		}
		return Response.ok(result);
	}
	
}
