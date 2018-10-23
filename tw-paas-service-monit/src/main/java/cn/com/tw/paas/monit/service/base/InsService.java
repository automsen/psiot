package cn.com.tw.paas.monit.service.base;

import java.util.List;

import cn.com.tw.paas.monit.entity.db.base.CmdIns;
import cn.com.tw.paas.monit.entity.db.base.Ins;

public interface InsService {

	List<Ins> selectInsByCmdIns(CmdIns cmdins);
}
