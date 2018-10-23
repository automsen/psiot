package cn.com.tw.saas.serv.controller.org;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.exception.RequestParamValidException;
import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.cons.SysCommons;
import cn.com.tw.saas.serv.entity.db.cust.Customer;
import cn.com.tw.saas.serv.entity.org.OrgPayConfig;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.service.cust.CustomerService;
import cn.com.tw.saas.serv.service.org.OrgPayConfigService;
import cn.com.tw.saas.serv.service.org.OrgUserService;

@RestController
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private OrgUserService orgUserService;
	
	@Autowired
	private CustomerService customerService;
	@Autowired
	private OrgPayConfigService orgPayConfigService;
	


	@RequestMapping("name/{userName}")
	public Response<?> queryUserByName(@PathVariable String userName){
		OrgUser user = orgUserService.queryUserByName(userName);
		return Response.ok(user);
	}
	
	@RequestMapping("custname/{phone}")
	public Response<?> queryCUserByName(@PathVariable String phone){
		
		Customer customer = new Customer();
		customer.setCustomerMobile1(phone);
		List<Customer> customList = customerService.selectByEntity(customer);
		if (customList.isEmpty()) {
			return Response.ok();
		}
		customer = customList.get(0);
		String orgId = customer.getOrgId();
		/**
		 *  初始化第三方应用需要的参数
		 */
		initThirdConfig(customer, orgId);
		return Response.ok(customer);
	}

	private void initThirdConfig(Customer customer, String orgId) {
		if(!StringUtils.isEmpty(orgId)){
			// 微信配置
			OrgPayConfig config = new OrgPayConfig();
			config.setConfigStatus(new Byte("0"));
			config.setPayOrgId(orgId);
			config.setPayType(SysCommons.payType.Wechat_Pay);
			List<OrgPayConfig> tempConfigs = orgPayConfigService.selectByEntity(config);
			if(tempConfigs != null && tempConfigs.size() != 0){
				config = tempConfigs.get(0);
				if(!StringUtils.isEmpty(config.getPayConfigJson())){
					JSONObject appConfig = JSONObject.parseObject(config.getPayConfigJson());
					String appId = appConfig.getString("appId");
					customer.setAppId(appId);
				}
			}
		}
	}
	
	/**
	 * 机构用户列表查询
	 * @param page
	 * @return
	 */
	@GetMapping("page")
	public Response<?> selectByPage(Page page){
		List<OrgUser> orgUsers = orgUserService.selectByPage(page);
		page.setData(orgUsers);
		return Response.ok(page);
	}
	
	/**
	 * 机构用户添加
	 * @param orgUser
	 * @return
	 */
	@PostMapping("")
	public Response<?> orgUserAdd(@RequestBody @Valid OrgUser orgUser, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), "参数异常");
		}
		try {
			JwtInfo jwt = JwtLocal.getJwt();
			String Roles =jwt.getRoles();
			if(!Roles.contains("admin")){
				return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "无权新增");
			}
			orgUserService.insertSelect(orgUser);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 机构用户修改
	 * @param orgUser
	 * @return
	 */
	@PutMapping("")
	public Response<?> orgUserUpdate(@RequestBody @Valid OrgUser orgUser, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), "参数异常");
		}
		try {
			orgUserService.updateSelect(orgUser);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 机构用户详情
	 * @param userId
	 * @return
	 */
	@GetMapping("details")
	public Response<?> selectOrgUserByUserId(String userId){
		OrgUser orgUser = orgUserService.selectById(userId);
		return Response.ok(orgUser);
	}
	
	/**
	 * 机构用户删除
	 * @param userId
	 * @return
	 */
	@DeleteMapping("{userId}")
	public Response<?> deleteOrgUser(@PathVariable String userId){
		try {
			orgUserService.deleteById(userId);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(), e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 重置密码
	 * @param userId
	 * @return
	 */
	@PutMapping("password/{userId}")
	public Response<?> updatePassword(@PathVariable String userId){
		orgUserService.updatePassword(userId);
		return Response.ok();
	}
	
	/**
	 * 修改密码
	 * @param userId
	 * @return
	 */
	@PutMapping("changePass")
	public Response<?> changePassword(@RequestBody Map<String,String> requestMap){
		JwtInfo info = JwtLocal.getJwt();
		if(info == null|| StringUtils.isEmpty(info.getSubject())){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"登陆信息失效！");
		}
		String userName = info.getSubName();
		String userId = info.getSubject();
		String oldPass = requestMap.get("oldPass");
		String newPass = requestMap.get("newPass");
		if(StringUtils.isEmpty(oldPass) || StringUtils.isEmpty(newPass)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"密码不能为空！");
		}
		
		try {
			OrgUser user = orgUserService.queryUserByName(userName);
			
			if(!user.getPassword().equals(oldPass)){
				return Response.retn(ResultCode.PARAM_VALID_ERROR,"旧密码错误！");
			}
			user = new OrgUser();
			user.setUserId(userId);
			user.setPassword(newPass);
			orgUserService.changePassword(user);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR,"系统异常！");
		}
		return Response.ok();
	}
}
