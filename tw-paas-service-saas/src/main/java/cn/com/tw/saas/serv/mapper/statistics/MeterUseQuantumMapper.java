package cn.com.tw.saas.serv.mapper.statistics;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.statistics.MeterUseQuantum;

public interface MeterUseQuantumMapper {
    int deleteByPrimaryKey(String id);

    int insert(MeterUseQuantum record);

    int insertSelective(MeterUseQuantum record);

    MeterUseQuantum selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MeterUseQuantum record);

    int updateByPrimaryKey(MeterUseQuantum record);

	List<MeterUseQuantum> meterUseQuantumJob(String format);

	void deleteByFreezeTd(String format);

	List<MeterUseQuantum> selectConditionByDay(MeterUseQuantum node);

	List<MeterUseQuantum> selectConditionByMonth(MeterUseQuantum node);

	List<MeterUseQuantum> selectConditionByYear(MeterUseQuantum node);
	
	List<MeterUseQuantum> selectConditionByHour(MeterUseQuantum node);

	List<MeterUseQuantum> selectHistoryExorpt(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> selectHoursStatics(Page page);

	List<MeterUseQuantum> hoursStaticsExport(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> selectByDate1(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> selectDayLineDate(String freezeTd, String meterAddr);

	List<MeterUseQuantum> selectDayLineDate1(String freezeTd, String meterAddr);

	List<MeterUseQuantum> selectMonthLineDate1(String freezeTd, String meterAddr);
}