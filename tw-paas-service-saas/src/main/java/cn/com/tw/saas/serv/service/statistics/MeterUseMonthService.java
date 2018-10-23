package cn.com.tw.saas.serv.service.statistics;

import java.util.List;

import cn.com.tw.common.web.core.IBaseSerivce;
import cn.com.tw.saas.serv.entity.statistics.MeterUseMonth;
import cn.com.tw.saas.serv.entity.statistics.MeterUseQuantum;

public interface MeterUseMonthService extends IBaseSerivce<MeterUseMonth>{

	List<MeterUseQuantum> selectByDate(MeterUseQuantum useQuantum);

	List<MeterUseQuantum> selectByDate1(MeterUseQuantum useQuantum);

}
