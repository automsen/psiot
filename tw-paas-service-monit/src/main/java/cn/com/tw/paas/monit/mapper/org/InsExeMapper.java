package cn.com.tw.paas.monit.mapper.org;

import java.util.List;

import cn.com.tw.paas.monit.entity.db.org.InsExe;

public interface InsExeMapper {
    int deleteByPrimaryKey(String id);

    int insert(InsExe record);

    int insertSelective(InsExe record);

    InsExe selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(InsExe record);

    int updateByPrimaryKey(InsExe record);
    
    List<InsExe> selectByEntity(InsExe record);
}