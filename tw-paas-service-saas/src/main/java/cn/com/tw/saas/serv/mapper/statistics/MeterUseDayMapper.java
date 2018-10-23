package cn.com.tw.saas.serv.mapper.statistics;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.statistics.MeterUseDay;
import cn.com.tw.saas.serv.entity.statistics.MeterUseQuantum;

public interface MeterUseDayMapper {
    int deleteByPrimaryKey(String id);

    int insert(MeterUseDay record);

    int insertSelective(MeterUseDay record);

    MeterUseDay selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MeterUseDay record);

    int updateByPrimaryKey(MeterUseDay record);

	List<MeterUseDay> meterUseDayJob(String format, String lastDayTime);

	void deleteByFreezeTd(String format);

	List<MeterUseQuantum> selectMeterUseDay(Page page);

	List<MeterUseQuantum> selectByPage(Page page);

	List<MeterUseQuantum> selectHistoryExorptByday(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> selectByDate(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> selectByDate1(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> selectDayElc(Page page);

	List<MeterUseQuantum> selectMonthLineDate(String freezeTd, String meterAddr);

	List<MeterUseQuantum> selectMonthLineDate1(String freezeTd, String meterAddr);

	List<MeterUseQuantum> selectHouseUseExpertByday(MeterUseQuantum useQuantum);
}