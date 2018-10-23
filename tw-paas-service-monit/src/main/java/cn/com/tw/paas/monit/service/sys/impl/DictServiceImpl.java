package cn.com.tw.paas.monit.service.sys.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.db.sys.Dict;
import cn.com.tw.paas.monit.mapper.sys.DictMapper;
import cn.com.tw.paas.monit.service.sys.DictService;

@Service
public class DictServiceImpl implements DictService{
	
	@Autowired
	private DictMapper dictMapper;

	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(Dict arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelect(Dict arg0) {
		return dictMapper.insertSelective(arg0);
	}

	@Override
	public Dict selectById(String dictId) {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(dictId);
		return dictMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Dict> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Dict arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(Dict arg0) {
		return dictMapper.updateByPrimaryKeySelective(arg0);
	}

	@Override
	public List<Dict> selectDictAll(Dict dict) {
		List<Dict> dicts = dictMapper.selectAll(dict);
		return dicts;
	}

	@Override
	public List<Dict> selectDictByPage(Page page) {
		List<Dict> dicts = dictMapper.selectDictByPage(page);
		return dicts;
	}

}
