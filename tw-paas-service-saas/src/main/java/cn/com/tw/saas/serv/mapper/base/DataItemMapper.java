/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.mapper.base;

import cn.com.tw.saas.serv.entity.db.base.DataItem;

public interface DataItemMapper {

    int insert(DataItem record);

    DataItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DataItem record);

}