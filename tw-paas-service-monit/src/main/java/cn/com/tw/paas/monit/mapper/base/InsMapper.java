/**
 * TODO: complete the comment
 */
package cn.com.tw.paas.monit.mapper.base;

import java.util.List;

import cn.com.tw.paas.monit.entity.db.base.CmdIns;
import cn.com.tw.paas.monit.entity.db.base.Ins;

public interface InsMapper {
//    int deleteByPrimaryKey(Integer id);

    int insert(Ins record);

    Ins selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Ins record);
    
    List<Ins> selectInsByCmdIns(CmdIns cmdins);

}