package cn.com.tw.paas.monit.service.seria;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import cn.com.tw.common.web.core.seria.SeriaNumber;
import cn.com.tw.paas.monit.entity.seria.Seria;
import cn.com.tw.paas.monit.mapper.seria.SeriaMapper;

public abstract class SeriaService extends SeriaNumber{
	
	@Autowired
	private SeriaMapper seriaMapper;
	
	@Override
	protected long readNum() {
		Seria seria = seriaMapper.selectByPrimaryKey(gSeriaKey());
		return seria.getSeriaValue();
	}

	@Override
	protected int updateNum(long updateNum) {
		Seria record = new Seria();
		record.setSeriaKey(gSeriaKey());
		record.setSeriaValue(updateNum);
		record.setUpdateTime(new Date());
		return seriaMapper.updateByPrimaryKey(record);
	}
	
	protected abstract String gSeriaKey();

}
