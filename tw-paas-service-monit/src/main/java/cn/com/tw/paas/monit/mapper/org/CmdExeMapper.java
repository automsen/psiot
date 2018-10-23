package cn.com.tw.paas.monit.mapper.org;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.db.org.CmdExe;

public interface CmdExeMapper {
    int deleteByPrimaryKey(String id);

    int insert(CmdExe record);

    int insertSelective(CmdExe record);

    CmdExe selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CmdExe record);

    int updateByPrimaryKey(CmdExe record);
    
    List<CmdExe> selectByEntity(CmdExe record);
    
    List<CmdExe> selectByPage(Page page);
}