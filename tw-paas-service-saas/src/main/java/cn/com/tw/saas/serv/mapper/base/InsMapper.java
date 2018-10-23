/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.mapper.base;

import cn.com.tw.saas.serv.entity.db.base.Ins;

public interface InsMapper {
//    int deleteByPrimaryKey(Integer id);

    int insert(Ins record);

    Ins selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Ins record);

}