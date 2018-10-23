package cn.com.tw.saas.serv.service.statistics;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.saas.serv.entity.statistics.MeterUseQuantum;
import cn.com.tw.saas.serv.entity.statistics.MeterUseYear;

public interface MeterUseYearService extends IBaseSerivce<MeterUseYear>{

	List<MeterUseQuantum> selectByDate(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> selectByDate1(MeterUseQuantum useQuantum);

}
