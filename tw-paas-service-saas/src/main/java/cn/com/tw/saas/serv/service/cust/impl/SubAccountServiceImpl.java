package cn.com.tw.saas.serv.service.cust.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.DeductOrderCalculator;
import cn.com.tw.saas.serv.entity.audit.AuditAccount;
import cn.com.tw.saas.serv.entity.business.cust.DeductOrderParams;
import cn.com.tw.saas.serv.entity.db.cust.DeductOrder;
import cn.com.tw.saas.serv.entity.db.cust.SubAccount;
import cn.com.tw.saas.serv.entity.db.rule.EnergyPrice;
import cn.com.tw.saas.serv.entity.db.rule.RuleAlarm;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.room.SaasRoom;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.mapper.audit.AuditAccountMapper;
import cn.com.tw.saas.serv.mapper.cust.DeductOrderMapper;
import cn.com.tw.saas.serv.mapper.cust.SubAccountMapper;
import cn.com.tw.saas.serv.mapper.room.RoomMapper;
import cn.com.tw.saas.serv.mapper.rule.EnergyPriceMapper;
import cn.com.tw.saas.serv.mapper.rule.RuleAlarmMapper;
import cn.com.tw.saas.serv.mapper.terminal.MeterMapper;
import cn.com.tw.saas.serv.service.cust.SubAccountService;

@Service
public class SubAccountServiceImpl implements SubAccountService {
	
	@Autowired
	private SubAccountMapper subAccountMapper;
	
	@Autowired
	private MeterMapper meterMapper;
	
	@Autowired
	private RoomMapper roomMapper;

	@Autowired
	private EnergyPriceMapper energyPriceMapper;
	
	@Autowired
	private DeductOrderMapper deductOrderMapper;
	
	@Autowired
	private AuditAccountMapper auditAccountMapper;
	
	@Autowired
	private RuleAlarmMapper ruleAlarmMapper;
	
	
	
	@Override
	public int insert(SubAccount record) {
		// TODO Auto-generated method stub
		return subAccountMapper.insert(record);
	}

	@Override
	public SubAccount selectById(String id) {
		// TODO Auto-generated method stub
		return subAccountMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<SubAccount> selectByPage(Page page) {
		// TODO Auto-generated method stub
		return subAccountMapper.selectByPage(page);
	}

	@Override
	public int update(SubAccount record) {
		// TODO Auto-generated method stub
		return subAccountMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int update(List<Meter> params) {
		int result = 0;
		/**
		 * 审核版本
		 */
		// 提交人
		JwtInfo info = JwtLocal.getJwt();
		OrgUser user = null;
		if (!StringUtils.isEmpty(info.getSubject())){
			user = new OrgUser();
			user.setUserId(info.getSubject());
			user.setUserName(info.getSubName());
		}
		String auditVersion = CommUtils.getUuid();
		for (Meter temp : params ){
			if(!StringUtils.isEmpty(temp.getMeterId())){
				Meter meter = meterMapper.selectByPrimaryKey(temp.getMeterId());
				Room room = roomMapper.selectByPrimaryKey(meter.getRoomId());
				
				AuditAccount auditAccount1 = new AuditAccount();
				auditAccount1.setAuditStatus((byte)0);
				auditAccount1.setSubAccountId(meter.getSubAccountId());
				List<AuditAccount> auditAccounts = auditAccountMapper.selectByEntity(auditAccount1);
				/**
				 * 判断是否拥有旧的待审核的记录
				 */
				if(auditAccounts != null && auditAccounts.size() > 0){
					auditAccount1.setAuditStatus((byte)3);
					auditAccount1.setId(auditAccounts.get(0).getId());
					auditAccountMapper.updateByPrimaryKeySelective(auditAccount1);
				}
				
				RuleAlarm ruleAlarm = ruleAlarmMapper.selectByPrimaryKey(temp.getAlarmId());
				EnergyPrice energyPrice = energyPriceMapper.selectByPrimaryKey(temp.getPriceId());
				//temp.setSubAccountStatus((byte)1);
				AuditAccount auditAccount = new AuditAccount();
				/**
				 * 判断审核类型
				 */
				if(meter.getSubAccountStatus() == 1){
					auditAccount.setAuditType((byte)1);//变更审核
				}else if(meter.getSubAccountStatus() == 0){
					auditAccount.setAuditType((byte)0);//开户审核
				}
				auditAccount.setSubAccountId(meter.getSubAccountId());
				auditAccount.setAlarmId(temp.getAlarmId());
				if(ruleAlarm != null){
					auditAccount.setRuleName(ruleAlarm.getRuleName());
				}else{
					auditAccount.setRuleName("");
				}
				auditAccount.setPriceId(temp.getPriceId());
				if(energyPrice != null){
					auditAccount.setPriceName(energyPrice.getPriceName());
				}else{
					auditAccount.setPriceName("");
				}
				auditAccount.setOrgId(meter.getOrgId());
				if(null!=user){
					auditAccount.setSubmitId(user.getUserId());
					auditAccount.setSubmitName(user.getUserName());
					auditAccount.setAuditName("");
				}
				auditAccount.setAuditStatus((byte)0);
				auditAccount.setAuditVersion(auditVersion);
				auditAccount.setEnergyType(meter.getEnergyType());
				auditAccount.setMeterAddr(meter.getMeterAddr());
				auditAccount.setStartRead(temp.getStartRead());
				auditAccount.setPriceModeCode(temp.getPriceModeCode());
				auditAccount.setRegionFullName(room.getRegionFullName());
				auditAccount.setRoomNumber(room.getRoomNumber());
				auditAccount.setRoomId(meter.getRoomId());
				auditAccount.setStartTime(new Date(System.currentTimeMillis()));
				result += auditAccountMapper.insert(auditAccount);
			}
		}
		return result;
	}
	
	public DeductOrder createOrder(String subAccountId,String meterAddr){
		DeductOrder param = new DeductOrder();
		param.setSubAccountId(subAccountId);
		param.setMeterAddr(meterAddr);
		DeductOrderParams orderParam = deductOrderMapper.selectOneParams(param);
		EnergyPrice price = new EnergyPrice();
		price = energyPriceMapper.selectByPrimaryKey(orderParam.getPriceId());
		DeductOrder order = null;
		try {
			order = DeductOrderCalculator.generateOrder(orderParam, price);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return order;
	}

	@Override
	public List<Room> selectCustomerByPage(Page page) {
		return subAccountMapper.selectCustomerByPage(page);
	}

}
