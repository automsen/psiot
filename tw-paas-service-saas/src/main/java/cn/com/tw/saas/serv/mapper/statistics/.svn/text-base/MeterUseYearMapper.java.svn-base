package cn.com.tw.saas.serv.mapper.statistics;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.statistics.MeterUseQuantum;
import cn.com.tw.saas.serv.entity.statistics.MeterUseYear;

public interface MeterUseYearMapper {
    int deleteByPrimaryKey(String id);

    int insert(MeterUseYear record);

    int insertSelective(MeterUseYear record);

    MeterUseYear selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(MeterUseYear record);

    int updateByPrimaryKey(MeterUseYear record);

	List<MeterUseYear> meterUseYearJob(String yearFormat);

	void deleteByFreezeTd(String yearFormat);

	List<MeterUseQuantum> selectMeterUseYear(Page page);

	List<MeterUseQuantum> selectByPage(Page page);

	List<MeterUseQuantum> selectHistoryExorptByYear(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> selectByDate(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> selectByDate1(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> selectYearElc(Page page);

	List<MeterUseQuantum> selectHouseUseExpertByYear(MeterUseQuantum useQuantum);

}