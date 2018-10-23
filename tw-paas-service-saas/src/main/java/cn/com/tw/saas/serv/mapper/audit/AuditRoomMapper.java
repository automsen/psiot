package cn.com.tw.saas.serv.mapper.audit;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.audit.AuditRoom;

public interface AuditRoomMapper {
    int deleteByPrimaryKey(String id);

    int insert(AuditRoom record);

    int insertSelective(AuditRoom record);

    AuditRoom selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AuditRoom record);

    int updateByPrimaryKey(AuditRoom record);

	List<AuditRoom> selectByEntity(AuditRoom auditRoom1);

	List<AuditRoom> selectByPage(Page page);

	AuditRoom selectAuditShop(AuditRoom auditRoom);

}