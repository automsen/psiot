package cn.com.tw.saas.serv.service.statistics;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.statistics.MeterUseQuantum;

public interface MeterUseQuantumService extends IBaseSerivce<MeterUseQuantum>{

	void meterUseQuantumJob(String format);

	void meterUseDayAndMonthAndYearJob(String format);

	List<MeterUseQuantum> selectMeterUseLevel(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> selectMeterUseday(Page page);

	List<MeterUseQuantum> selectMeterUseMonth(Page page);

	List<MeterUseQuantum> selectMeterUseYear(Page page);

	List<MeterUseQuantum> selectConditionByDay(MeterUseQuantum node);

	List<MeterUseQuantum> selectConditionByMonth(MeterUseQuantum node);

	List<MeterUseQuantum> selectConditionByYear(MeterUseQuantum node);
	
	List<MeterUseQuantum> selectConditionByHour(MeterUseQuantum node);

	List<MeterUseQuantum> historyExpert(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> selectMeterUseHour(Page page);

	List<MeterUseQuantum> selectHoursStatics(Page page);


	List<MeterUseQuantum> hoursStaticsExport(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> selectByDate1(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> selectDayElc(Page page);

	List<MeterUseQuantum> selectMonthElc(Page page);

	List<MeterUseQuantum> selectYearElc(Page page);

	List<MeterUseQuantum> selectDayLineDate(String freezeTd,String meterAddr);

	List<MeterUseQuantum> selectMonthLineDate(String freezeTd, String meterAddr);

	List<MeterUseQuantum> selectYearLineDate(String freezeTd, String meterAddr);

	List<MeterUseQuantum> selectDayLineDate1(String freezeTd, String meterAddr);

	List<MeterUseQuantum> selectMonthLineDate1(String freezeTd, String meterAddr);

	List<MeterUseQuantum> selectYearLineDate1(String freezeTd, String meterAddr);

	List<MeterUseQuantum> selectHouseUseExpert(MeterUseQuantum useQuantum);

	void meterUseMonthAndYearJob(String format);

}
