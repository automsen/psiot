package cn.com.tw.saas.serv.controller.agent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.cust.RoomAccountRecord;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.sys.SysUser;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.service.cust.RoomAccountRecordService;
import cn.com.tw.saas.serv.service.sys.SysUserService;
import cn.com.tw.saas.serv.service.terminal.MeterService;

@RestController
@RequestMapping("agent")
@Api(description = "代理商")
public class AgentController {

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private MeterService meterService;
	@Autowired
	private RoomAccountRecordService roomAccountRecordService;
	
	/**
	 * 查机构数量和仪表数量
	 * @param userId
	 * @return
	 */
	@GetMapping("number")
	public Response<?> selectNumber(String userId){
		JwtInfo info = JwtLocal.getJwt();
		OrgUser user = null;
		if (!StringUtils.isEmpty(info.getSubject())) {
			user = new OrgUser();
			user.setUserId(info.getSubject());
			user.setUserName(info.getSubName());
		}
		SysUser sysUser = sysUserService.selectNumber(user.getUserId());
		return Response.ok(sysUser);
	}
	
	/**
	 * 终端饼图
	 * @param userId
	 * @return
	 */
	@GetMapping("terminal")
	public Response<?> selectTerminalNumber(){
		JwtInfo info = JwtLocal.getJwt();
		OrgUser user = null;
		if (!StringUtils.isEmpty(info.getSubject())) {
			user = new OrgUser();
			user.setUserId(info.getSubject());
			user.setUserName(info.getSubName());
		}
		List<Meter> meters = meterService.selectTerminalNumber(user.getUserId());
		return Response.ok(meters);
	}
	
	/**
	 * 
	 * @return
	 */
	@GetMapping("nettype")
	public Response<?> selectTerminalNetType(){
		JwtInfo info = JwtLocal.getJwt();
		OrgUser user = null;
		if (!StringUtils.isEmpty(info.getSubject())) {
			user = new OrgUser();
			user.setUserId(info.getSubject());
			user.setUserName(info.getSubName());
		}
		List<Meter> meters = meterService.selectTerminalNetType(user.getUserId());
		return Response.ok(meters);
	}
	
	/**
	 * 充值订单
	 * @return
	 */
	@GetMapping("record")
	public Response<?> selectAccountRecord(Page page){
		JwtInfo info = JwtLocal.getJwt();
		OrgUser user = null;
		if (!StringUtils.isEmpty(info.getSubject())) {
			user = new OrgUser();
			user.setUserId(info.getSubject());
			user.setUserName(info.getSubName());
		}
		List<RoomAccountRecord> accountRecords = roomAccountRecordService.selectAccountRecord(user.getUserId());
		page.setData(accountRecords);
		return Response.ok(page);
	}
	
	@GetMapping("newRecord")
	public Response<?> selectNewAccountRecord(String orgId, String time ,String id){
		RoomAccountRecord roomAccountRecord = new RoomAccountRecord();
		roomAccountRecord.setId(id);
		roomAccountRecord.setOrgId(orgId);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date date = null; 
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		roomAccountRecord.setCreateTime(date);
		List<RoomAccountRecord> accountRecords = roomAccountRecordService.selectNewAccountRecord(roomAccountRecord);
		return Response.ok(accountRecords);
	}
	
	
}
