package cn.com.tw.engine.core.utils;

import java.net.SocketException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.com.tw.common.core.cache.redis.RedisService;
import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.tools.ip.IpAddrUtils;
import cn.com.tw.engine.core.utils.cons.RedisKeyCons;

@Component
public class RegCacheService {
	
	private static Logger logger = LoggerFactory.getLogger(RegCacheService.class);
	
	@Autowired
	private RedisService redisService;
	
	private String ip;
	
	@Value("${server.port}")
	private String port;
	
	@Value("${engine.cluster:false}")
	private boolean isCluster = false;
	
	public void regisHostPort(String termNo) {
		
		if (!isCluster()) {
			return;
		}
		
		try {
			getIp();
		} catch (SocketException e) {
			throw new BusinessException(e.getMessage(), e.toString());
		}
		logger.debug("termNo {} registering redis, local machine {} : {} ", new Object[]{termNo, ip, port});
		redisService.set(RedisKeyCons.regTermNoKey + termNo, new HostPort(ip, port));
		logger.debug("registering redis success.");
	}
	
	public void getIp() throws SocketException {
		
		if (StringUtils.isEmpty(this.ip)) {
			this.ip = IpAddrUtils.getLocalIp();
			
			if (StringUtils.isEmpty(ip)) {
				throw new IllegalArgumentException("ip is null");
			}
		}
		
	}
	
	public boolean isCluster() {
		return isCluster;
	}

	public class HostPort {
		
		private String ip;
		
		private String port;
		
		public HostPort(String ip, String port) {
			this.ip = ip;
			this.port = port;
		}

		@Override
		public String toString() {
			return "HostPort [ip=" + ip + ", port=" + port + "]";
		}
	}
}
