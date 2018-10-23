package cn.com.tw.saas.serv.service.hb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.saas.serv.dao.TerminalLastDataDao;

@Service
public class TerminalLastDataService {
	
	@Autowired
	private TerminalLastDataDao terminalLastDataDao;

	public void createLastTable() {
		terminalLastDataDao.createTerminalLastTable();
	}

}
