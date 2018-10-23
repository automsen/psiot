package cn.com.tw.common.core.anno;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import cn.com.tw.common.core.cache.redis.RedisConfig;
import cn.com.tw.common.core.cache.redis.RedisService;
import cn.com.tw.common.core.jms.MqUtils;
import cn.com.tw.common.core.jms.activemq.ActiveMqService;
import cn.com.tw.common.core.jms.kafka.KafkaMqService;

public class CusServiceImportSelector implements DeferredImportSelector {
	
	protected String getMessageTypeAttributeName() {
		return "mq";
	}
	
	protected String getCacheTypeAttributeName() {
		return "cache";
	}
	
	public String[] selectImports(AnnotationMetadata importingClassMetadata) {
		AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(
				EnableCusService.class.getName(), false));
		if (attributes == null) {
			throw new IllegalArgumentException(String.format(
					"AnnotationAttributes is not present on importing class '%s' as expected",
					new Object[] {importingClassMetadata.getClassName() }));
		}

		MessgeTypeEum type = (MessgeTypeEum) attributes.getEnum(getMessageTypeAttributeName());
		List<String> serviceSelector = new ArrayList<String>();
		switch (type) {
			case ACTIVEMQ:
				serviceSelector.add(ActiveMqService.class.getCanonicalName());
				break;
			case KAFKA:
				serviceSelector.add(KafkaMqService.class.getCanonicalName());
				serviceSelector.add(MqUtils.class.getCanonicalName());
				break;
			default:
				throw new IllegalArgumentException(String.format("MessgeTypeEum is not match,'%s' ", new Object[] {type}));
		}
		
		CacheTypeEum cacheType = (CacheTypeEum) attributes.getEnum(getCacheTypeAttributeName());
		switch (cacheType) {
			case DEFAULT:
				break;
			case REDIS:
				serviceSelector.add(RedisConfig.class.getCanonicalName());
				serviceSelector.add(RedisService.class.getCanonicalName());
				break;
			default:
				throw new IllegalArgumentException(String.format("CacheTypeEum is not match,'%s' ", new Object[] {type}));
		}
		
		return serviceSelector.toArray(new String[0]);
	}
}