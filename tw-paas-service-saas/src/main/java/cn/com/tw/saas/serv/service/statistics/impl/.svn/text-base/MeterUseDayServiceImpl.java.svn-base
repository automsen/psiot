package cn.com.tw.saas.serv.service.statistics.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.statistics.MeterUseDay;
import cn.com.tw.saas.serv.entity.statistics.MeterUseQuantum;
import cn.com.tw.saas.serv.mapper.statistics.MeterUseDayMapper;
import cn.com.tw.saas.serv.service.statistics.MeterUseDayService;

@Service
public class MeterUseDayServiceImpl implements MeterUseDayService{

	@Autowired
	private MeterUseDayMapper meterUseDayMapper;
	
	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(MeterUseDay arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelect(MeterUseDay arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MeterUseDay selectById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MeterUseDay> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(MeterUseDay arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(MeterUseDay arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<MeterUseQuantum> selectByDate(MeterUseQuantum useQuantum) {
		return meterUseDayMapper.selectByDate(useQuantum);
	}

	@Override
	public List<MeterUseQuantum> selectByDate1(MeterUseQuantum useQuantum) {
		// TODO Auto-generated method stub
		return meterUseDayMapper.selectByDate1(useQuantum);
	}

}
