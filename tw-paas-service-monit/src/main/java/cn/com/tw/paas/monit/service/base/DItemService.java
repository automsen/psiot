package cn.com.tw.paas.monit.service.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.paas.monit.entity.db.base.DataItem;
import cn.com.tw.paas.monit.mapper.base.DataItemMapper;

@Service
public class DItemService {
	
	@Autowired
	private DataItemMapper dataItemMapper;
	
	public List<DataItem> queryAll(){
		return dataItemMapper.queryAll();
	}

}
