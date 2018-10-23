package cn.com.tw.paas.monit.controller.term;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.service.org.TerminalEquipService;
import cn.com.tw.paas.monit.service.read.ReadService;


/**
 * 移动端网页接口
 * @author GZH
 *
 */
@RestController
@RequestMapping("mobileApi")
public class mobileApi {
	
	@Autowired
	private TerminalEquipService terminalEquipService;
	@Autowired
	private ReadService readService;
	
	
	/**
	 * 模糊查询设备列表
	 * @param terminalEquip
	 * @param res
	 * @return
	 */
	@GetMapping("selectEquipList")
	public Response<?> selectEquipList(TerminalEquip terminalEquip,HttpServletResponse res){
		terminalEquip.setOrgId(JwtLocal.getJwt().getExt("orgId").toString());
		List<TerminalEquip> list=terminalEquipService.selectEquipList(terminalEquip);
//		res.setHeader("Access-Control-Allow-Origin", "*");
		return Response.ok(list);
	}
	
	/**
	 * 查询设备详情与最近读数
	 * @param equipNum 
	 * @param res
	 * @return
	 */
//	@PostMapping()
//	public Response<?> selectEquipInfoAndReadLast(String equipNum,HttpServletResponse res){
//		Map<String, Object> resultMap=new HashMap<String, Object>();
//		Map<String, Object> equipInfo=terminalEquipService.selectInfoByEquipNumber(equipNum);
//		resultMap.put("equipInfo", equipInfo);
//		Object readLast=readFeignService.readLastByMeterAddr(equipNum).getData();
//		resultMap.put("readLast", readLast);
////		res.setHeader("Access-Control-Allow-Origin", "*");
//		return Response.ok(resultMap);
//	}
	
}
