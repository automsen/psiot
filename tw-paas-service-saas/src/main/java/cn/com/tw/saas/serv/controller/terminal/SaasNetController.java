package cn.com.tw.saas.serv.controller.terminal;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.http.HttpGetReq;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.common.web.utils.env.Env;
import cn.com.tw.saas.serv.common.utils.MapUtil;
import cn.com.tw.saas.serv.common.utils.SignatureUtil;
import cn.com.tw.saas.serv.common.utils.cons.code.MonitResultCode;
import cn.com.tw.saas.serv.entity.terminal.SaasNet;
import cn.com.tw.saas.serv.service.terminal.FeignNetService;
import cn.com.tw.saas.serv.service.terminal.SaasNetService;

@RestController
@RequestMapping("net")
@Api(description = "网关管理接口")
public class SaasNetController {

	@Autowired
	private SaasNetService saasNetService;
	
	@Autowired
	private FeignNetService feignNetService;
	
	private static final String  APPKEY = Env.getVal("paas.appKey");
	
	private static final String SECRET = Env.getVal("paas.secret");
	
	private static final String URL = Env.getVal("paas.url");
	/**
	 * 网关分页
	 * @param page
	 * @return
	 */
	@ApiOperation(value="网关列表", notes="")
	@GetMapping("page")
	@ResponseBody
	public Response<?> selectByPage(Page page){
		try {
			List<SaasNet> list=saasNetService.selectByPage(page);
			page.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(page);
	}
	
	/**
	 * 新增网关
	 * @param saasNet
	 * @return
	 */
	@ApiOperation(value="新增网关", notes="")
	@PostMapping
	@ResponseBody
	public Response<?> addNet(@RequestBody SaasNet saasNet){
		SaasNet net=saasNetService.selectByNumber(saasNet.getNetNumber());
		if(net==null){
			saasNet.setNetId(CommUtils.getUuid());
			saasNet.setCreateTime(new Date(System.currentTimeMillis()));
			saasNetService.insertSelect(saasNet);
		}else{
			return Response.retn(ResultCode.PARAM_VALID_ERROR,"网关编号重复");
		}
		return Response.ok();
	}
	/**
	 * 删除网关
	 * @param netId
	 * @return
	 */
	@ApiOperation(value="删除网关", notes="")
	@DeleteMapping("{netId}")
	public Response<?> delete(@PathVariable String netId){
		if(StringUtils.isEmpty(netId)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		SaasNet saasNet=saasNetService.selectById(netId);
		if(!StringUtils.isEmpty(saasNet.getregionNo())){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "该网关已绑定楼栋！"); 
		}
		try {
			saasNetService.deleteById(netId);
		} catch (BusinessException e) {
			e.printStackTrace();
			return Response.retn(e.getCode(),e.getMessage());
		}
		return Response.ok();
	}
	
	@ApiOperation(value="根据网关编号查询网关", notes="")
	@GetMapping("")
	public Response<?> selectByNetNumber(String netNumber){
		SaasNet saasNet = saasNetService.selectByNumber(netNumber);
		return Response.ok(saasNet);
	}
	
	@ApiOperation(value="根据条件查询网关", notes="")
	@GetMapping("all")
	public Response<?> selectSaasNet(SaasNet saasNet){
		List<SaasNet> saasNets = saasNetService.selectSaasNet(saasNet);
		return Response.ok(saasNets);
	}
	
	
	
	/**
	 * 修改网关信息
	 * @param saasMeter
	 * @return
	 */
	@ApiOperation(value="修改网关信息", notes="")
	@PutMapping()
	public Response<?> update(@RequestBody SaasNet saasNet){
		if(StringUtils.isEmpty(saasNet.getNetId())){
			return Response.retn(ResultCode.PARAM_VALID_ERROR);
		}
		saasNet.setUpdateTime(new Date(System.currentTimeMillis()));
		saasNetService.updateSelect(saasNet);
		return Response.ok();
	}
	@ApiOperation(value="查询该楼栋下所有网关", notes="")
	@GetMapping("getnet/{regionId}")
	@ResponseBody
	public Response<?> selectByregionNo(@PathVariable String regionId){
		List<SaasNet> list=null;
		try {
			list=saasNetService.selectByRegionId(regionId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(list);
	}
	
	/**
	 * 网关详情页
	 * @param meterId
	 * @return
	 */
	@ApiOperation(value="网关详情页", notes="")
	@GetMapping("{netId}")
	public Response<?> details(@PathVariable String netId){
		if(StringUtils.isEmpty(netId)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		return Response.ok(saasNetService.selectById(netId));
	}
	/**
	 * 通过网关编号PAAS平台网关信息
	 * @return
	 */
	@ApiOperation(value="获取PAAS平台网关信息", notes="")
	@GetMapping("paasnet/{netNumber}")
	@ResponseBody
	public Response<?> selectByPaasNetInfo(@PathVariable String netNumber){
		if(StringUtils.isEmpty(netNumber)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		Map<String, String> requestMap=new HashMap<String, String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		try {
			requestMap.put("equipNumber", netNumber);
			requestMap.put("appKey", APPKEY);
			requestMap.put("timestamp", sdf.format(new Date()));
			requestMap.put("businessNo", "4445");
			String sign = SignatureUtil.generateSign(requestMap, SECRET);
			requestMap.put("sign", sign);
			/*Response<?> data=feignNetService.selectAbnorDate(netNumber, APPKEY, requestMap.get("timestamp"), "4445", sign);
			if(StringUtils.isEmpty(data)){
				return Response.retn(MonitResultCode.DATA_EXISTS_NULL, "系统异常");
			}else{
				return data;
			}*/
			String uri = "/usepaas/netinfo/";
	        HttpGetReq req = new HttpGetReq(URL+"/"+uri+"?"+MapUtil.mapJoin(requestMap, false, false));
	        String result = req.excuteReturnStr();
	        if(result.equals("")){
	        	return Response.retn(ResultCode.PARAM_VALID_ERROR, "无此仪表");
	        }
	        Response response = JsonUtils.jsonToPojo(result, Response.class);
	        return response;
		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
	}
	/**
	 * 通过网关编号模糊查询PAAS平台网关信息
	 * @return
	 */
	@ApiOperation(value="通过网关编号模糊查询PAAS平台网关信息", notes="")
	@ResponseBody
	@GetMapping("like/{netNumber}")
	public Response<?> likeNetNumber(@PathVariable String netNumber){
		if(StringUtils.isEmpty(netNumber)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "系统异常");
		}
		Map<String, String> requestMap=new HashMap<String, String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		try {
			requestMap.put("equipNumber", netNumber);
			requestMap.put("appKey", APPKEY);
			requestMap.put("timestamp", sdf.format(new Date()));
			requestMap.put("businessNo", "4445");
			String sign = SignatureUtil.generateSign(requestMap, SECRET);
			requestMap.put("sign", sign);
			/*Response<?> data=feignNetService.selectLikeDate(netNumber, APPKEY, requestMap.get("timestamp"), "4445", sign);
			if(StringUtils.isEmpty(data)){
				return Response.retn(ResultCode.DATABASE_OPERATIOIN_IS_ERROR, "系统异常");
			}else{
				return data;
			}*/
			String uri = "/usepaas/likenet/";
	        HttpGetReq req = new HttpGetReq(URL+"/"+uri+"?"+MapUtil.mapJoin(requestMap, false, false));
	        String result = req.excuteReturnStr();
	        Response response = JsonUtils.jsonToPojo(result, Response.class);
	        return response;
		} catch (Exception e) {
			e.printStackTrace();
			return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "系统异常");
		}
	}
} 