package cn.com.tw.saas.serv.mapper.audit;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.audit.AuditAccount;
import feign.Param;

public interface AuditAccountMapper {
    int deleteByPrimaryKey(String id);

    int insert(AuditAccount record);

    AuditAccount selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(AuditAccount record);

	List<AuditAccount> selectByPage(Page page);

	List<AuditAccount> selectByRoomId(@Param("roomId") String roomId);

	List<AuditAccount> selectByEntity(AuditAccount auditAccount);
}