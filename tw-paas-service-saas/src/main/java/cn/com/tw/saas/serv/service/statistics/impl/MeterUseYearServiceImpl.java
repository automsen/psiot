package cn.com.tw.saas.serv.service.statistics.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.statistics.MeterUseQuantum;
import cn.com.tw.saas.serv.entity.statistics.MeterUseYear;
import cn.com.tw.saas.serv.mapper.statistics.MeterUseYearMapper;
import cn.com.tw.saas.serv.service.statistics.MeterUseYearService;

@Service
public class MeterUseYearServiceImpl implements MeterUseYearService{

	@Autowired
	private MeterUseYearMapper meterUseYearMapper;
	
	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(MeterUseYear arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelect(MeterUseYear arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MeterUseYear selectById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MeterUseYear> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(MeterUseYear arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(MeterUseYear arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<MeterUseQuantum> selectByDate(MeterUseQuantum useQuantum) {
		return meterUseYearMapper.selectByDate(useQuantum);
	}

	@Override
	public List<MeterUseQuantum> selectByDate1(MeterUseQuantum useQuantum) {
		// TODO Auto-generated method stub
		return meterUseYearMapper.selectByDate1(useQuantum);
	}

}
