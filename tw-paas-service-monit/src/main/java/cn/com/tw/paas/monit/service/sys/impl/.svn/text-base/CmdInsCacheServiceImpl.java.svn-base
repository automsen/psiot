package cn.com.tw.paas.monit.service.sys.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import cn.com.tw.common.core.cache.redis.RedisService;
import cn.com.tw.paas.monit.entity.db.base.Cmd;
import cn.com.tw.paas.monit.entity.db.base.Ins;
import cn.com.tw.paas.monit.mapper.base.CmdMapper;
import cn.com.tw.paas.monit.mapper.base.InsDataItemMapper;
import cn.com.tw.paas.monit.service.sys.CmdInsCacheService;
/**
 *  命令指令集 redis缓存
 * @author liming
 * 2018年10月16日 10:50:01
 */
@Service
public class CmdInsCacheServiceImpl implements CmdInsCacheService{
	
	private static Logger log = LoggerFactory.getLogger(CmdInsCacheServiceImpl.class);

	@Autowired
	private RedisService redisService;
	
	private final boolean DATA_BY_REDIS = false; 
	
	@Autowired
	private CmdMapper cmdMapper;
	@Autowired
	private InsDataItemMapper insDataItemMapper;
	
	private final String CMD_REDIS_CACHE_KEY = "CACHE:CMD:LIST";
	
	private final String INS_REDIS_CACHE_KEY_HEADER = "CACHE:INS:LIST";
	
	
	/**
	 *  缓存命令信息
	 */
	@Override
	public Object selectCmdById(String cmdCode) {
		Object resultCmd = null;
		if(DATA_BY_REDIS){
			resultCmd = redisService.hGet(CMD_REDIS_CACHE_KEY, cmdCode);
		}
		if(resultCmd == null ){
			try {
				resultCmd = queryOrCacheCmd(cmdCode);
			} catch (Exception e) {
				log.error("### cmd query or cache Error exception:{}###",e);
			}
		}
		return resultCmd;
	}

	
	
	/**
	 *  缓存命令相关的指令信息
	 */
	@Override
	public Object selectInsByDataMarker(String datamarker) {
		
		Object resultIns = null;
		if(DATA_BY_REDIS){
			resultIns = redisService.hGet(INS_REDIS_CACHE_KEY_HEADER, datamarker);
		}
		if(resultIns == null ){
			try {
				resultIns = queryOrCacheIns(datamarker);
			} catch (Exception e) {
				log.error("### Ins query or cache Error exception:{}###",e);
			}
		}
		return resultIns;
	}

	@Override
	public void refreshAll() throws DataAccessException{
		redisService.execute(new RedisCallback<String>() {
			@Override
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				try {
					connection.multi();
					connection.del(rawKey(CMD_REDIS_CACHE_KEY));
					connection.del(rawKey(INS_REDIS_CACHE_KEY_HEADER));
					connection.exec();
				} catch (DataAccessException e) {
					log.error("### redis will be rollback,exception:{}",e);
					connection.discard();
					throw e;  // 放弃整个任务
				}
				return null;
			}
		});
	}
	
	private byte[] rawKey(String key) {
		StringRedisSerializer stringredis = new StringRedisSerializer();
		return stringredis.serialize(key);
	}
	
	
	
	private Object queryOrCacheCmd(String cmdCode) {
		Cmd resultCmd = null;
		List<Cmd> tempList = null;
		if(DATA_BY_REDIS){
			Map<String,Object> resultMap = new HashMap<String, Object>();
			tempList = cmdMapper.selectByEntity(null);
			for (Cmd cmd : tempList) {
				resultMap.put(cmd.getCmdCode(), cmd);
				if(cmd.getCmdCode().equals(cmdCode)){
					resultCmd = cmd;
				}
			}
			redisService.hSetMap(CMD_REDIS_CACHE_KEY, resultMap);
			log.debug("### CMD CACHE SUCCUESS###");
		}else{
			Cmd queryModel = new Cmd();
			queryModel.setCmdCode(cmdCode);
			tempList = cmdMapper.selectByEntity(queryModel);
			if(tempList != null){
				resultCmd = tempList.get(0);
			}
		}
		return resultCmd;
	}
	
	private Object queryOrCacheIns(String datamarker) {
		Map<String, Object> resultMap = new HashMap<String,Object>();
		Object resultIns = null;
		List<Ins> tempList = null;
		if(DATA_BY_REDIS){
			tempList = insDataItemMapper.selectAll();
			for (Ins ins : tempList) {
				resultMap.put(ins.getDataMarker(), ins.getItems());
				if(datamarker.equals(ins.getDataMarker())){
					resultIns = ins.getItems();
				}
			}
			redisService.hSetMap(INS_REDIS_CACHE_KEY_HEADER, resultMap);
			log.debug("### INS CACHE by datamarker {} SUCCUESS###",datamarker);
		} else{
			Ins queryModel = new Ins();
			queryModel.setDataMarker(datamarker);
			tempList = insDataItemMapper.selectByEntity(queryModel);
		}
		return resultIns;
	}

}
