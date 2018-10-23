package cn.com.tw.saas.serv.service.dict.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.dict.SysDict;
import cn.com.tw.saas.serv.mapper.dict.SysDictMapper;
import cn.com.tw.saas.serv.service.dict.SysDictService;
@Service
public class SysDictServiceImpl implements SysDictService{

	@Autowired
	private SysDictMapper sysDictMapper;
	
	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return sysDictMapper.deleteByPrimaryKey(Integer.parseInt(arg0));
	}

	@Override
	public int insert(SysDict arg0) {
		// TODO Auto-generated method stub
		return sysDictMapper.insertSelective(arg0);
	}

	@Override
	public int insertSelect(SysDict arg0) {
		// TODO Auto-generated method stub
		return sysDictMapper.insertSelective(arg0);
	}

	@Override
	public SysDict selectById(String arg0) {
		// TODO Auto-generated method stub
		return sysDictMapper.selectByPrimaryKey(Integer.parseInt(arg0));
	}

	@Override
	public List<SysDict> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return sysDictMapper.selectByPage(arg0);
	}

	@Override
	public int update(SysDict arg0) {
		// TODO Auto-generated method stub
		return sysDictMapper.updateByPrimaryKeySelective(arg0);
	}

	@Override
	public int updateSelect(SysDict arg0) {
		// TODO Auto-generated method stub
		return sysDictMapper.updateByPrimaryKeySelective(arg0);
	}

	@Override
	public List<SysDict> selectByDictType(String dictType) {
		// TODO Auto-generated method stub
		return sysDictMapper.selectByDictType(dictType);
	}

	@Override
	public List<SysDict> selectAll(SysDict sysDict) {
		// TODO Auto-generated method stub
		return sysDictMapper.selectAll(sysDict);
	}

}
