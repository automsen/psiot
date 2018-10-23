package cn.com.tw.paas.monit.service.sys.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.db.sys.Area;
import cn.com.tw.paas.monit.mapper.sys.AreaMapper;
import cn.com.tw.paas.monit.service.sys.AreaService;

@Service
public class AreaServiceImpl implements AreaService {
	
	@Autowired
	private AreaMapper areaMapper;

	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return areaMapper.deleteByPrimaryKey(arg0);
	}

	@Override
	public int insert(Area arg0) {
		// TODO Auto-generated method stub
		return areaMapper.insert(arg0);
	}

	@Override
	public int insertSelect(Area arg0) {
		// TODO Auto-generated method stub
		return areaMapper.insertSelective(arg0);
	}

	@Override
	public Area selectById(String arg0) {
		// TODO Auto-generated method stub
		return areaMapper.selectByPrimaryKey(arg0);
	}

	@Override
	public List<Area> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Area arg0) {
		// TODO Auto-generated method stub
		return areaMapper.updateByPrimaryKey(arg0);
	}

	@Override
	public int updateSelect(Area arg0) {
		// TODO Auto-generated method stub
		return areaMapper.updateByPrimaryKeySelective(arg0);
	}

	@Override
	public List<Area> selectAreaByParentId(String parentId) {
		// TODO Auto-generated method stub
		return areaMapper.selectAreaByParentId(parentId);
	}

}
