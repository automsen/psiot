package cn.com.tw.saas.serv.controller.cust;

import java.util.List;

import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.cust.SubAccount;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.service.cust.SubAccountService;

@RestController
@RequestMapping("subAccount")
public class SubAccountController {

	@Autowired
	private SubAccountService subAccountService;

	@ApiOperation(value = "客户余额列表", notes = "")
	@GetMapping("balance")
	public Response<?> selectByPage(Page page) {
		List<SubAccount> subAccounts = subAccountService.selectByPage(page);
		page.setData(subAccounts);
		return Response.ok(page);
	}
	
	
	@ApiOperation(value = "客户-基础信息列表", notes = "")
	@GetMapping("cust")
	public Response<?> selectCustomerByPage(Page page){
		List<Room> subAccounts = subAccountService.selectCustomerByPage(page);
		page.setData(subAccounts);
		return Response.ok(page);
	}
	

	@ApiOperation(value = "仪表子账户编辑", notes = "")
	@PutMapping("")
	public Response<?> update(@RequestBody List<Meter> meters) {
		int result = 0;
		try {
			if (meters.size() > 0) {
				result = subAccountService.update(meters);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(result);
	}

}
