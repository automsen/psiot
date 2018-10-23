package cn.com.tw.saas.serv.mapper.dict;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.dict.SysDict;

public interface SysDictMapper {
    int deleteByPrimaryKey(Integer dictId);

    int insert(SysDict record);

    int insertSelective(SysDict record);

    SysDict selectByPrimaryKey(Integer dictId);

    int updateByPrimaryKeySelective(SysDict record);

    int updateByPrimaryKey(SysDict record);
    
    List<SysDict> selectByPage(Page arg0);
    
    List<SysDict> selectByDictType(String dictType);

	List<SysDict> selectAll(SysDict sysDict);
}