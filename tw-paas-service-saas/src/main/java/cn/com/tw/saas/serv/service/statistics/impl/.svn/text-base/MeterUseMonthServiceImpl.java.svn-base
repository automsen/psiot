package cn.com.tw.saas.serv.service.statistics.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.statistics.MeterUseMonth;
import cn.com.tw.saas.serv.entity.statistics.MeterUseQuantum;
import cn.com.tw.saas.serv.mapper.statistics.MeterUseMonthMapper;
import cn.com.tw.saas.serv.service.statistics.MeterUseMonthService;

@Service
public class MeterUseMonthServiceImpl implements MeterUseMonthService{

	@Autowired
	private MeterUseMonthMapper meterUseMonthMapper;
	
	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(MeterUseMonth arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelect(MeterUseMonth arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MeterUseMonth selectById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MeterUseMonth> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(MeterUseMonth arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(MeterUseMonth arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<MeterUseQuantum> selectByDate(MeterUseQuantum useQuantum) {
		return meterUseMonthMapper.selectByDate(useQuantum);
	}

	@Override
	public List<MeterUseQuantum> selectByDate1(MeterUseQuantum useQuantum) {
		// TODO Auto-generated method stub
		return meterUseMonthMapper.selectByDate1(useQuantum);
	}

}
