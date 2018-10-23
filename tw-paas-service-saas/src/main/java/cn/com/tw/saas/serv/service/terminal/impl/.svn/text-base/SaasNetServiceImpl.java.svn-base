package cn.com.tw.saas.serv.service.terminal.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.tools.http.HttpGetReq;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.common.web.utils.env.Env;
import cn.com.tw.saas.serv.common.utils.MapUtil;
import cn.com.tw.saas.serv.common.utils.SignatureUtil;
import cn.com.tw.saas.serv.common.utils.cons.code.MonitResultCode;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.entity.terminal.SaasMeter;
import cn.com.tw.saas.serv.entity.terminal.SaasNet;
import cn.com.tw.saas.serv.mapper.terminal.MeterMapper;
import cn.com.tw.saas.serv.mapper.terminal.SaasMeterMapper;
import cn.com.tw.saas.serv.mapper.terminal.SaasNetMapper;
import cn.com.tw.saas.serv.service.terminal.FeignNetService;
import cn.com.tw.saas.serv.service.terminal.SaasNetService;
@Service
public class SaasNetServiceImpl implements SaasNetService{

	@Autowired
	private SaasNetMapper saasNetMapper;
	@Autowired
	private FeignNetService feignNetService;
	@Autowired
	private MeterMapper saasMeterMapper;
	
	private static final String  APPKEY = Env.getVal("paas.appKey");
	
	private static final String SECRET = Env.getVal("paas.secret");
	
	private static final String URL = Env.getVal("paas.url");
	
	@Override
	public int deleteById(String arg0) {
		SaasNet saasNet = saasNetMapper.selectByPrimaryKey(arg0);
		/**
		 * 判断网关是否被仪表使用
		 */
		Meter saasMeter = new Meter();
		saasMeter.setNetNumber(saasNet.getNetNumber());
		List<Meter> saasMeters = saasMeterMapper.selectByEntity(saasMeter);
		if(saasMeters != null && saasMeters.size() > 0){
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "网关已被仪表使用");
		}
		
		return saasNetMapper.deleteByPrimaryKey(arg0);
	}

	@Override
	public int insert(SaasNet arg0) {
		// TODO Auto-generated method stub
		return saasNetMapper.insert(arg0);
	}

	@Override
	public int insertSelect(SaasNet arg0) {
		Map<String, String> requestMap=new HashMap<String, String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		requestMap.put("equipNumber", arg0.getNetNumber());
		requestMap.put("appKey", Env.getVal("paas.appKey"));
		requestMap.put("timestamp", sdf.format(new Date()));
		requestMap.put("businessNo", "4445");
		String sign = SignatureUtil.generateSign(requestMap, Env.getVal("paas.secret"));
		requestMap.put("sign", sign);
		//Response<?> data = feignNetService.selectAbnorDate(arg0.getNetNumber(), Env.getVal("paas.appKey"), requestMap.get("timestamp"), "4445", sign);
		String uri = "/usepaas/netinfo/";
        HttpGetReq req = new HttpGetReq(URL+"/"+uri+"?"+MapUtil.mapJoin(requestMap, false, false));
        String result;
		try {
			result = req.excuteReturnStr();
			Response response = JsonUtils.jsonToPojo(result, Response.class);
			Map<String, Object> map = (Map<String, Object>) response.getData();
			String netIp = (String) map.get("netNumber");
			if(!StringUtils.isEmpty(netIp)){
				arg0.setNetIp(netIp);
			}
			String modelName = (String) map.get("modelName");
			if(!StringUtils.isEmpty(modelName)){
				arg0.setModelName(modelName);
			}
			String gatherHz = (String) map.get("gatherHz");
			if(!StringUtils.isEmpty(gatherHz)){
				arg0.setGatherHz(gatherHz);
			}
		    String netType = (String) map.get("netTypeCode");
		    if(!StringUtils.isEmpty(netType)){
				arg0.setNetType(netType);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return saasNetMapper.insertSelective(arg0);
	}

	@Override
	public SaasNet selectById(String arg0) {
		// TODO Auto-generated method stub
		return saasNetMapper.selectByPrimaryKey(arg0);
	}

	@Override
	public List<SaasNet> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return saasNetMapper.selectByPage(arg0);
	}

	@Override
	public int update(SaasNet arg0) {
		// TODO Auto-generated method stub
		return saasNetMapper.updateByPrimaryKey(arg0);
	}

	@Override
	public int updateSelect(SaasNet arg0) {
		// TODO Auto-generated method stub
		return saasNetMapper.updateByPrimaryKeySelective(arg0);
	}

	@Override
	public SaasNet selectByNumber(String netNumber) {
		// TODO Auto-generated method stub
		return saasNetMapper.selectByNumber(netNumber);
	}

	@Override
	public List<SaasNet> selectByRegionId(String regionId) {
		// TODO Auto-generated method stub
		return saasNetMapper.selectByRegionId(regionId);
	}

	@Override
	public List<SaasNet> selectSaasNet(SaasNet saasNet) {
		return saasNetMapper.selectSaasNet(saasNet);
	}

}
