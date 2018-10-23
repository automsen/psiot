package cn.com.tw.saas.serv.service.room.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.room.SaasSurchargeRoom;
import cn.com.tw.saas.serv.mapper.room.SaasSurchargeRoomMapper;
import cn.com.tw.saas.serv.service.room.SaasSurchargeRoomService;
@Service
public class SaasSurchargeRoomServiceImpl implements SaasSurchargeRoomService{

	@Autowired
	private SaasSurchargeRoomMapper saasSurchargeRoomMapper;
	
	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return saasSurchargeRoomMapper.deleteByPrimaryKey(arg0);
	}

	@Override
	public int insert(SaasSurchargeRoom arg0) {
		// TODO Auto-generated method stub
		return saasSurchargeRoomMapper.insert(arg0);
	}

	@Override
	public int insertSelect(SaasSurchargeRoom arg0) {
		// TODO Auto-generated method stub
		return saasSurchargeRoomMapper.insertSelective(arg0);
	}

	@Override
	public SaasSurchargeRoom selectById(String arg0) {
		// TODO Auto-generated method stub
		return saasSurchargeRoomMapper.selectByPrimaryKey(arg0);
	}

	@Override
	public List<SaasSurchargeRoom> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return saasSurchargeRoomMapper.selectByPage(arg0);
	}

	@Override
	public int update(SaasSurchargeRoom arg0) {
		// TODO Auto-generated method stub
		return saasSurchargeRoomMapper.updateByPrimaryKey(arg0);
	}

	@Override
	public int updateSelect(SaasSurchargeRoom arg0) {
		// TODO Auto-generated method stub
		return saasSurchargeRoomMapper.updateByPrimaryKeySelective(arg0);
	}

}
