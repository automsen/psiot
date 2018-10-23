package cn.com.tw.saas.serv.service.cust.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.cust.SubAccountRecord;
import cn.com.tw.saas.serv.mapper.cust.SubAccountRecordMapper;
import cn.com.tw.saas.serv.service.cust.SubAccountRecordService;

@Service
public class SubAccountRecordServiceImpl implements SubAccountRecordService {
	
	@Autowired
	private SubAccountRecordMapper subAccountRecordMapper;

	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(SubAccountRecord arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelect(SubAccountRecord arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public SubAccountRecord selectById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SubAccountRecord> selectByPage(Page page) {
		return subAccountRecordMapper.selectByPage(page);
	}

	@Override
	public int update(SubAccountRecord arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(SubAccountRecord arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
