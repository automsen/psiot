package cn.com.tw.saas.serv.service.cust.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.com.tw.saas.serv.entity.business.cust.CustRoomParam;
import cn.com.tw.saas.serv.entity.view.serve.RoomExtend;
import cn.com.tw.saas.serv.mapper.cust.RoomAccountMapper;
import cn.com.tw.saas.serv.service.cust.RoomAccountService;

@Service
public class RoomAccountServiceImpl implements RoomAccountService {

	@Autowired
	private RoomAccountMapper roomAccountMapper;

	@Override
	public List<RoomExtend> selectForExtendList(CustRoomParam param) {
		return roomAccountMapper.selectForExtendList(param);
	}

	@Override
	public List<RoomExtend> selectForAllExtendList(CustRoomParam param) {
		return roomAccountMapper.selectForAllExtendList(param);
	}

}
