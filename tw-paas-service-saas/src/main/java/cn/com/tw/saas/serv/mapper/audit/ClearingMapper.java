package cn.com.tw.saas.serv.mapper.audit;

import java.util.List;
import java.util.Map;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.audit.Clearing;

public interface ClearingMapper {
    int deleteByPrimaryKey(String id);

    int insert(Clearing record);

    int insertSelective(Clearing record);

    Clearing selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Clearing record);

    int updateByPrimaryKey(Clearing record);

	List<Clearing> selectByEntity(Clearing clearing1);

	List<Clearing> selectByPage(Page page);

	List<Clearing> selectClearingAudit(Clearing clearing);

	 List<Clearing> clearingExport(Clearing clearing);

	Map<String, Object> count(Map<String, Object> param);

	List<Clearing> selectClearingByMeterAddr(Clearing clearing1);

	List<Clearing> selectByRoomId(Clearing clearing1);
}