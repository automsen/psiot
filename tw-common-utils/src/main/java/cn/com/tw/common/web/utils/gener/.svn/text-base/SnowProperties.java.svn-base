package cn.com.tw.common.web.utils.gener;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@ConfigurationPropertiesBinding
@ConfigurationProperties(prefix = "spring.snowf")
public class SnowProperties {

	private long workerId = 0;

	private long dataId = 0;

	public long getWorkerId() {
		return workerId;
	}

	public void setWorkerId(long workerId) {
		this.workerId = workerId;
	}

	public long getDataId() {
		return dataId;
	}

	public void setDataId(long dataId) {
		this.dataId = dataId;
	}

	@Override
	public String toString() {
		return "SnowProperties [workerId=" + workerId + ", dataId=" + dataId
				+ "]";
	}
}