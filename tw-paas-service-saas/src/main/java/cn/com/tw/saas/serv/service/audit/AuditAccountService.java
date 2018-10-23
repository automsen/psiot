package cn.com.tw.saas.serv.service.audit;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.saas.serv.entity.audit.AuditAccount;

public interface AuditAccountService extends IBaseSerivce<AuditAccount>{

	List<AuditAccount> selectByRoomId(String roomId);

	List<AuditAccount> selectByEntity(AuditAccount auditAccount);

	void updateAuditStatus(AuditAccount auditAccount);

}
