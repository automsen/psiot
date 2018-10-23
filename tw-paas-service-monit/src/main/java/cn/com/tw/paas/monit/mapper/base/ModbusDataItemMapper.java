/**
 * TODO: complete the comment
 */
package cn.com.tw.paas.monit.mapper.base;

import java.util.List;

import cn.com.tw.paas.monit.entity.db.base.ModbusDataItem;

public interface ModbusDataItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ModbusDataItem record);
    
    List<ModbusDataItem> selectByEntity(ModbusDataItem param);

    ModbusDataItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ModbusDataItem record);
}