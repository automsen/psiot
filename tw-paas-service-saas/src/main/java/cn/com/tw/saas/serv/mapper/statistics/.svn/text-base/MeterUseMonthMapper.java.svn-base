package cn.com.tw.saas.serv.mapper.statistics;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.statistics.MeterUseMonth;
import cn.com.tw.saas.serv.entity.statistics.MeterUseQuantum;

public interface MeterUseMonthMapper {
    int deleteByPrimaryKey(String id);

    int insert(MeterUseMonth record);

    int insertSelective(MeterUseMonth record);

    MeterUseMonth selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MeterUseMonth record);

    int updateByPrimaryKey(MeterUseMonth record);

	List<MeterUseMonth> meterUseMonthJob(String monthFormat, String lastMonthTime);

	void deleteByFreezeTd(String monthFormat);

	List<MeterUseQuantum> selectMeterUseMonth(Page page);

	List<MeterUseQuantum> selectByPage(Page page);

	List<MeterUseQuantum> selectHistoryExorptByMonth(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> selectByDate(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> selectByDate1(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> selectMonthElc(Page page);

	List<MeterUseQuantum> selectYearLineDate(String freezeTd, String meterAddr);

	List<MeterUseQuantum> selectYearLineDate1(String freezeTd, String meterAddr);

	List<MeterUseQuantum> selectHouseUseExpertByMonth(MeterUseQuantum useQuantum);

}