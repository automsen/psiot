package cn.com.tw.paas.monit.common.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cn.com.tw.common.utils.tools.cache.ehcache.EHCache;
import cn.com.tw.paas.monit.common.utils.cons.CacheNameCons;
import cn.com.tw.paas.monit.entity.db.base.Cmd;
import cn.com.tw.paas.monit.entity.db.base.Ins;
import cn.com.tw.paas.monit.mapper.base.CmdMapper;
import cn.com.tw.paas.monit.mapper.base.InsDataItemMapper;

/**
 * 启动后执行
 * 
 * @author Cheng Qi Peng
 *
 */
@Component
public class ApplicationStartup implements CommandLineRunner {

	@Autowired
	private InsDataItemMapper insDataItemMapper;
	
	@Autowired
	private CmdMapper cmdMapper;

	private static Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);

	@Override
	public void run(String... arg0) throws Exception {
		List<Ins> insList = insDataItemMapper.selectAll();
		EHCache cache = EHCache.build();
		for (Ins temp : insList) {
			cache.put(CacheNameCons.ins_dataItem, temp.getDataMarker(), temp.getItems());
		}
		// 获取所有命令 并缓存
		List<Cmd> cmdList = cmdMapper.selectByEntity(null);
		
		for (Cmd cmd : cmdList) {
			cache.put(CacheNameCons.cmd_dataItem, cmd.getCmdCode(), cmd);
		}
		logger.info("预加载" + insList.size() + "条指令与其包含的数据项关系");
		logger.info("预加载" + cmdList.size() + "条命令");
	}

}
