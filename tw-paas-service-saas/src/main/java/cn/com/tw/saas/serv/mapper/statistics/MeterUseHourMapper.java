package cn.com.tw.saas.serv.mapper.statistics;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.statistics.MeterUseHour;
import cn.com.tw.saas.serv.entity.statistics.MeterUseQuantum;

public interface MeterUseHourMapper {

	List<MeterUseQuantum> selectByDate(MeterUseQuantum useQuantum);


	void deleteByFreezeTd(String format);

	void insertSelective(MeterUseHour meterUseHour);

	List<MeterUseQuantum> selectMeterUseHour(Page page);

	List<MeterUseQuantum> selectByPage(Page page);

	List<MeterUseQuantum> selectHistoryExorptByhour(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> hoursStaticsExport(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> selectHoursStatics(Page page);


	List<MeterUseQuantum> selectByDate1(MeterUseQuantum useQuantum);

}
