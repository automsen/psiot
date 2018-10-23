package cn.com.tw.paas.monit.service.org.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.common.web.utils.gener.NumberGenerator;
import cn.com.tw.paas.monit.common.utils.cons.code.MonitResultCode;
import cn.com.tw.paas.monit.entity.business.org.OrgApplicationExpand;
import cn.com.tw.paas.monit.entity.business.org.OrgExpand;
import cn.com.tw.paas.monit.entity.db.org.NetEquip;
import cn.com.tw.paas.monit.entity.db.org.Org;
import cn.com.tw.paas.monit.entity.db.org.OrgApplication;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.mapper.org.NetEquipMapper;
import cn.com.tw.paas.monit.mapper.org.OrgApplicationMapper;
import cn.com.tw.paas.monit.mapper.org.OrgMapper;
import cn.com.tw.paas.monit.mapper.org.TerminalEquipMapper;
import cn.com.tw.paas.monit.service.org.OrgApplicationService;

@Service
public class OrgApplicationServiceImpl implements OrgApplicationService{
	
	@Autowired
	private OrgApplicationMapper orgApplicationMapper;
	@Autowired
	private NumberGenerator numberGenerator;
	@Autowired
	private TerminalEquipMapper terminalEquipMapper;
	@Autowired
	private NetEquipMapper netEquipMapper;
	@Autowired
	private OrgMapper orgMapper;
	
	@Override
	public int deleteById(String appId) {
		TerminalEquip terminalEquip = new TerminalEquip();
		terminalEquip.setAppId(appId);
		List<TerminalEquip> terminalEquips = terminalEquipMapper.selectTerminalAll(terminalEquip);
		/**
		 * 判断应用是否被终端使用
		 */
		if(terminalEquips != null && terminalEquips.size() >0){
			throw new BusinessException(MonitResultCode.CAN_NOT_DELETE_ERROR,"该应用已有终端使用");
		}
		NetEquip netEquip = new NetEquip();
		netEquip.setAppId(appId);
		List<NetEquip> netEquips = netEquipMapper.selectByEntity(netEquip);
		/**
		 * 判断应用是否被网关使用
		 */
		if(netEquips != null && netEquips.size() > 0){
			throw new BusinessException(MonitResultCode.CAN_NOT_DELETE_ERROR,"该应用已有网关使用");
		}
		orgApplicationMapper.deleteByPrimaryKey(appId);
		return 0;
	}

	@Override
	public int insert(OrgApplication arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelect(OrgApplication orgApplication) {
		// TODO Auto-generated method stub
		String url = orgApplication.getCallbackUrl();
		int i = -1;
		if(StringUtils.isEmpty(url)){
			throw new BusinessException(MonitResultCode.PARAMETER_NULL_ERROR, "推送url不能为空"); 
		}
			i =url.indexOf("http://");
			if (i!=0){
				throw new BusinessException(MonitResultCode.URL_ILLEGAL_ERROR, "url非法"); 
			}
		String orgId = orgApplication.getOrgId();
		
		int maxAppId = orgApplicationMapper.selectAppMaxIdByOrgId(orgId);
		if (maxAppId == 0) {
			orgApplication.setAppId(orgId + "01");
		}else {
			orgApplication.setAppId(String.valueOf(maxAppId + 1));
		}
		orgApplication.setOrgId(orgId);
		orgApplication.setAppKey(numberGenerator.getHexStrIdByByPrefix("APP_").replaceAll("0*$",""));
		orgApplication.setSecretKey(CommUtils.getUuid());
		orgApplication.setCreateTime(new Date(System.currentTimeMillis()));
		orgApplicationMapper.insertSelective(orgApplication);
		
		return 0;
	}

	@Override
	public OrgApplication selectById(String arg0) {
		return null;
	}

	@Override
	public List<OrgApplication> selectByPage(Page arg0) {
		return null;
	}

	@Override
	public int update(OrgApplication arg0) {
		return 0;
	}

	@Override
	public int updateSelect(OrgApplication orgApplication) {
		orgApplication.setUpdateTime(new Date(System.currentTimeMillis()));
		//不允许修改机构Id
		orgApplication.setOrgId(null);
		return orgApplicationMapper.updateByPrimaryKeySelective(orgApplication);
	}

	@Override
	public List<OrgApplicationExpand> selectOrgApplicationPage(Page page) {
		List<OrgApplicationExpand> orgApplications = orgApplicationMapper.selectOrgApplicationByPage(page);
		return orgApplications;
	}

	@Override
	public List<OrgApplication> selectOrgApplicationAll(OrgApplication orgApplication) {
		List<OrgApplication> orgApplications = orgApplicationMapper.selectOrgApplicationAll(orgApplication);
		return orgApplications;
	}

	@Override
	public OrgApplicationExpand selectByppId(String appId) {
		return orgApplicationMapper.selectByPrimaryKey(appId);
	}

	@Override
	public OrgApplication selectByAppKey(String appKey) {
		return orgApplicationMapper.selectByAppKey(appKey);
	}

	@Override
	public OrgApplication selectByTerminalEquip(String equipNumber) {
		return orgApplicationMapper.selectByTerminalEquip(equipNumber);
	}

	@Override
	public OrgApplication selectByNetEquip(String equipNumber) {
		return orgApplicationMapper.selectByNetEquip(equipNumber);
	}

	@Override
	public List<Map<String, Object>> selectOrgAndApplicationByApp() {
		List<Map<String, Object>> listTop = new ArrayList<Map<String, Object>>();
		
		OrgExpand org = new OrgExpand();
		OrgApplication oa = new OrgApplication();
		List<OrgApplication> list = orgApplicationMapper.selectOrgApplicationAll(oa);
		List<OrgExpand> orgList = orgMapper.selectOrgAll(org);
		
		if(orgList != null && orgList.size() > 0){
			//根据前端所需数据格式组装对应数据
			for (int i = 0; i < orgList.size(); i++) {
				Map<String, Object> map = new HashMap<String, Object>();
				List<Object> childrenList = new ArrayList<Object>();
				Org oe =  orgList.get(i);
				String value = oe.getOrgId();
				String text = oe.getOrgName();
				map.put("value", value);
				map.put("text", text);
				if(list != null && list.size() > 0){
					for (int j = 0; j < list.size(); j++) {
						if(list.get(j).getOrgId().equals(oe.getOrgId())){
							Map<String, Object> map2 = new HashMap<String, Object>();
							map2.put("value", list.get(j).getAppId());
							map2.put("text", list.get(j).getAppName());
							childrenList.add(map2);
						}
					}
				}
				map.put("children", childrenList);
				listTop.add(map);
			}
			return listTop;
		}
		return null;
	}

	@Override
	public List<OrgApplication> selectAll(OrgApplication orgApplication) {
		// TODO Auto-generated method stub
		return orgApplicationMapper.selectAll(orgApplication);
	}


}
