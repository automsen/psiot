package cn.com.tw.paas.monit.mapper.sys;

import java.util.List;

import cn.com.tw.common.web.core.IBaseMapper;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.db.sys.Dict;

public interface DictMapper extends IBaseMapper<Dict>{
	
	Dict selectByPrimaryKey(Integer dictId);

	void deleteByPrimaryKey(Integer dictId);

	List<Dict> selectByDictType(String dictType);
	Dict selectByCode(String dictCode);
	List<Dict> selectAll(Dict dict);

	List<Dict> selectDictByPage(Page page);
}