package cn.com.tw.saas.serv.service.audit.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.cons.code.MonitResultCode;
import cn.com.tw.saas.serv.entity.audit.AuditAccount;
import cn.com.tw.saas.serv.entity.db.cust.SubAccount;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.mapper.audit.AuditAccountMapper;
import cn.com.tw.saas.serv.mapper.cust.SubAccountMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterMapper;
import cn.com.tw.saas.serv.service.audit.AuditAccountService;

@Service
public class AuditAccountSreviceImpl implements AuditAccountService{

	@Autowired
	private AuditAccountMapper auditAccountMapper;
	@Autowired
	private SubAccountMapper subAccountMapper;
	@Autowired
	private MeterMapper meterMapper;
	
	
	
	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(AuditAccount arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelect(AuditAccount arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AuditAccount selectById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AuditAccount> selectByPage(Page page) {
		return auditAccountMapper.selectByPage(page);
	}

	@Override
	public int update(AuditAccount arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 通过
	 */
	@Override
	@Transactional
	public int updateSelect(AuditAccount auditAccount) {
		
		List<AuditAccount> auditAccounts = new ArrayList<AuditAccount>();
		if(!StringUtils.isEmpty(auditAccount.getRoomId())){
			auditAccounts = auditAccountMapper.selectByEntity(auditAccount);
		}
		
		if(auditAccounts == null || auditAccounts.size() == 0){
			throw new BusinessException(MonitResultCode.DATA_EXISTS_ERROR, "数据异常");
		}
		// 审核人
		JwtInfo info = JwtLocal.getJwt();
		OrgUser user = null;
		if (!StringUtils.isEmpty(info.getSubject())){
			user = new OrgUser();
			user.setUserId(info.getSubject());
			user.setUserName(info.getSubName());
		}
		/**
		 * 修改审核记录 和 仪表账户信息
		 */
		for (AuditAccount auditAccount2 : auditAccounts) {
			auditAccount2.setAuditStatus((byte)1);
			if(null!=user){
				auditAccount2.setAuditId(user.getUserId());
				auditAccount2.setAuditName(user.getUserName());
			}
			auditAccount2.setAuditOpinion(auditAccount.getAuditOpinion());
			auditAccountMapper.updateByPrimaryKeySelective(auditAccount2);
			
			Meter meter = meterMapper.selectByMeterAddr(auditAccount2.getMeterAddr());
			
			meter.setAlarmId(auditAccount2.getAlarmId());
			meter.setPriceId(auditAccount2.getPriceId());
			meter.setPriceModeCode(auditAccount2.getPriceModeCode());
			// 开户审核
			if(auditAccount2.getAuditType()==0){
				meter.setSubAccountStatus((byte)1);
				meter.setStartRead(auditAccount2.getStartRead());
				meter.setStartTime(auditAccount2.getStartTime());
				meter.setBalanceUpdateRead(auditAccount2.getStartRead());
				meter.setBalanceUpdateTime(auditAccount2.getStartTime());
				meter.setBalance(BigDecimal.ZERO);
			}
			// 规则变更审核
			else if(auditAccount2.getAuditType()==1){
				
			}
			meterMapper.updateByPrimaryKeySelective(meter);
		}
		
		return 0;
	}

	@Override
	public List<AuditAccount> selectByRoomId(String roomId) {
		return auditAccountMapper.selectByRoomId(roomId);
	}

	@Override
	public List<AuditAccount> selectByEntity(AuditAccount auditAccount) {
		return auditAccountMapper.selectByEntity(auditAccount);
	}

	/**
	 * 驳回修改状态
	 */
	@Override
	public void updateAuditStatus(AuditAccount auditAccount) {
		
		List<AuditAccount> auditAccounts = new ArrayList<AuditAccount>();
		if(!StringUtils.isEmpty(auditAccount.getRoomId())){
			auditAccounts = auditAccountMapper.selectByEntity(auditAccount);
		}
		
		if(auditAccounts == null || auditAccounts.size() == 0){
			throw new BusinessException(MonitResultCode.DATA_EXISTS_ERROR, "数据异常");
		}
		// 审核人
		JwtInfo info = JwtLocal.getJwt();
		OrgUser user = null;
		if (!StringUtils.isEmpty(info.getSubject())){
			user = new OrgUser();
			user.setUserId(info.getSubject());
			user.setUserName(info.getSubName());
		}
		for (AuditAccount auditAccount2 : auditAccounts) {
			auditAccount2.setAuditStatus((byte)2);
			if(null!=user){
				auditAccount2.setAuditId(user.getUserId());
				auditAccount2.setAuditName(user.getUserName());
			}
			auditAccount2.setAuditOpinion(auditAccount.getAuditOpinion());
			auditAccountMapper.updateByPrimaryKeySelective(auditAccount2);
		}
	}

}
