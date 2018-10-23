/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.mapper.room;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.room.AllowancePlan;

public interface AllowancePlanMapper {
    int deleteByPrimaryKey(String id);

    int insert(AllowancePlan record);

    AllowancePlan selectByPrimaryKey(String id);
    
    List<AllowancePlan> selectByPage(Page param);
    
    List<AllowancePlan> selectByEntity(AllowancePlan record);

    int updateByPrimaryKeySelective(AllowancePlan record);

	void updateRoomAmount(AllowancePlan allowancePlan2);
}