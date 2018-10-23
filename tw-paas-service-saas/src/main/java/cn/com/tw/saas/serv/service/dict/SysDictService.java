package cn.com.tw.saas.serv.service.dict;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.saas.serv.entity.dict.SysDict;

public interface SysDictService extends IBaseSerivce<SysDict>{

	
	List<SysDict> selectByDictType(String dictType);

	List<SysDict> selectAll(SysDict sysDict);
}
