package cn.com.tw.paas.monit.service.seria;

import org.springframework.stereotype.Service;

@Service
public class OrgSeriaService extends SeriaService{
	
	private String seriaKey = "org_key";

	@Override
	protected String gSeriaKey() {
		return seriaKey;
	}

}
