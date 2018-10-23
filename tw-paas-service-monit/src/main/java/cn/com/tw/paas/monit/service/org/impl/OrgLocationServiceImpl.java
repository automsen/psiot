package cn.com.tw.paas.monit.service.org.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.db.org.OrgLocation;
import cn.com.tw.paas.monit.entity.db.org.OrgResident;
import cn.com.tw.paas.monit.mapper.org.OrgLocationMapper;
import cn.com.tw.paas.monit.service.org.OrgLocationService;

@Service
public class OrgLocationServiceImpl implements OrgLocationService {
	
	@Autowired
	private OrgLocationMapper orgLocationMapper;

	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return orgLocationMapper.deleteByPrimaryKey(arg0);
	}

	@Override
	public int insert(OrgLocation arg0) {
		// TODO Auto-generated method stub
		return orgLocationMapper.insert(arg0);
	}

	@Override
	public int insertSelect(OrgLocation arg0) {
		// TODO Auto-generated method stub
		return orgLocationMapper.insertSelective(arg0);
	}

	@Override
	public OrgLocation selectById(String arg0) {
		// TODO Auto-generated method stub
		return orgLocationMapper.selectByPrimaryKey(arg0);
	}

	@Override
	public List<OrgLocation> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(OrgLocation arg0) {
		// TODO Auto-generated method stub
		return orgLocationMapper.updateByPrimaryKey(arg0);
	}

	@Override
	public int updateSelect(OrgLocation arg0) {
		// TODO Auto-generated method stub
		return orgLocationMapper.updateByPrimaryKeySelective(arg0);
	}

	@Override
	public List<OrgResident> selectHouseNumByAroeId(String areaId) {
		// TODO Auto-generated method stub
		return orgLocationMapper.selectHouseNumByAroeId(areaId);
	}

	@Override
	public List<Map<String, String>> selectHouseListByLikeForApp(OrgLocation orgLocation) {
		// TODO Auto-generated method stub
		return orgLocationMapper.selectHouseListByLikeForApp(orgLocation);
	}

}
