package cn.com.tw.saas.serv.service.org.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.org.OrgAccountRecord;
import cn.com.tw.saas.serv.mapper.org.OrgAccountRecordMapper;
import cn.com.tw.saas.serv.service.org.OrgAccountRecordService;

@Service
public class OrgAccountRecordServiceImpl implements OrgAccountRecordService{
	
	@Autowired
	private OrgAccountRecordMapper orgAccountRecordMapper;

	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(OrgAccountRecord arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelect(OrgAccountRecord arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public OrgAccountRecord selectById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrgAccountRecord> selectByPage(Page page) {
		List<OrgAccountRecord> orgAccountRecord = orgAccountRecordMapper.selectByPage(page);
		for (OrgAccountRecord orgAccountRecord2 : orgAccountRecord) {
			if(orgAccountRecord2.getDisburseMoney()==null){
				orgAccountRecord2.setDisburseMoneyStr("0");
			}else{
				orgAccountRecord2.setDisburseMoneyStr(orgAccountRecord2.getDisburseMoney().toPlainString());
			}
			
			//充值的笔数减去 撤销的笔数
			if(orgAccountRecord2.getbNumber() != null){
				orgAccountRecord2.setRevenueNumber(orgAccountRecord2.getRevenueNumber() - orgAccountRecord2.getbNumber());
			}
			//充值的金额减去 撤销的金额
			if(orgAccountRecord2.getbMoney() != null){
				orgAccountRecord2.setDayMoney(orgAccountRecord2.getDayMoney().add(orgAccountRecord2.getbMoney()));
			}
		}
		return orgAccountRecord;
	}

	@Override
	public int update(OrgAccountRecord arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(OrgAccountRecord arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<OrgAccountRecord> orgAccountRecordExpert(OrgAccountRecord orgAccountRecord) {
		
		List<OrgAccountRecord> accountRecords = orgAccountRecordMapper.orgAccountRecordExpert(orgAccountRecord);
		for (OrgAccountRecord orgAccountRecord2 : accountRecords) {
			if(orgAccountRecord2.getDisburseMoney()==null){
				orgAccountRecord2.setDisburseMoneyStr("0.00");
			}else{
				orgAccountRecord2.setDisburseMoneyStr(orgAccountRecord2.getDisburseMoney().toString());
			}
			if(orgAccountRecord2.getDisburseNumber()==null){
				orgAccountRecord2.setDisburseNumber(0);
			}
			if(orgAccountRecord2.getMinMoney()==null){
				orgAccountRecord2.setMinMoney(BigDecimal.ZERO);
			}
			
		}
		return accountRecords;
	}

}
