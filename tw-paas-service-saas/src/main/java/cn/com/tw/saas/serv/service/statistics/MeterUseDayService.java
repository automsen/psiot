package cn.com.tw.saas.serv.service.statistics;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.saas.serv.entity.statistics.MeterUseDay;
import cn.com.tw.saas.serv.entity.statistics.MeterUseQuantum;

public interface MeterUseDayService extends IBaseSerivce<MeterUseDay>{

	List<MeterUseQuantum> selectByDate(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> selectByDate1(MeterUseQuantum useQuantum);

}
