/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.mapper.read;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.read.ReadManual;

public interface ReadManualMapper {
    int deleteByPrimaryKey(String id);

    int insert(ReadManual record);

    ReadManual selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ReadManual record);
    
    List<ReadManual> selectByEntity(ReadManual param);
    
    List<ReadManual> selectByPage(Page param);

	List<ReadManual> selectAppReadManual(@Param("regionId") String regionId);
}