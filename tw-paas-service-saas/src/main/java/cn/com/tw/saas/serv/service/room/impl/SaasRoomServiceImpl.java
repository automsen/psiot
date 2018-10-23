package cn.com.tw.saas.serv.service.room.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.cons.code.MonitResultCode;
import cn.com.tw.saas.serv.entity.db.cust.CustomerRoom;
import cn.com.tw.saas.serv.entity.excel.RoomExcel;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.room.SaasRegion;
import cn.com.tw.saas.serv.entity.room.SaasRoom;
import cn.com.tw.saas.serv.entity.terminal.SaasMeter;
import cn.com.tw.saas.serv.mapper.cust.CustomerRoomMapper;
import cn.com.tw.saas.serv.mapper.room.SaasRegionMapper;
import cn.com.tw.saas.serv.mapper.room.SaasRoomMapper;
import cn.com.tw.saas.serv.mapper.terminal.SaasMeterMapper;
import cn.com.tw.saas.serv.service.room.SaasRoomService;

@Service
public class SaasRoomServiceImpl implements SaasRoomService {

	@Autowired
	private SaasRoomMapper saasRoomMapper;
	@Autowired
	private SaasRegionMapper saasRegionMapper;
	@Autowired
	private CustomerRoomMapper customerRoomMapper;
	@Autowired
	private SaasMeterMapper saasMeterMapper;

	@Override
	public int deleteById(String id) {
		
		/**
		 * 判断房间是否和客户绑定
		 */
		CustomerRoom customerRoom = new CustomerRoom();
		customerRoom.setRoomId(id);
		List<CustomerRoom> customerRooms = customerRoomMapper.selectByEntity(customerRoom);
		if(customerRooms != null && customerRooms.size() > 0){
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "房间已与客户绑定");
		}
		/**
		 * 判断房间是否和仪表绑定
		 */
		SaasMeter saasMeter = new SaasMeter();
		saasMeter.setRoomId(id);
		List<SaasMeter> saasMeters = saasMeterMapper.selectByEntity(saasMeter);
		if(saasMeters != null && saasMeters.size() > 0){
			throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR, "房间已与仪表绑定");
		}
		// TODO Auto-generated method stub
		return saasRoomMapper.deleteByPrimaryKey(id);
	}

	@Override
	@Transactional
	public int insertSelect(SaasRoom arg0) {
		SaasRoom temp = new SaasRoom();
		temp.setOrgId(arg0.getOrgId());
		temp.setRegionId(arg0.getRegionId());
		temp.setRoomNumber(arg0.getRoomNumber());
		List<SaasRoom> tempList = saasRoomMapper.selectByEntity(temp);
		if (tempList != null&&tempList.size()>0) {
			throw new BusinessException(MonitResultCode.PARAMETER_NULL_ERROR, "该楼栋已存在房间编号");
		}
		SaasRegion region = saasRegionMapper.selectByPrimaryKey(arg0.getRegionId());
		if (region.getIsDelete()== 1){
			region.setIsDelete((byte) 0);
			saasRegionMapper.updateByPrimaryKeySelective(region);
		}
		arg0.setRegionFullName(region.getRegionFullName() + "-" + region.getRegionName());
		arg0.setRegionNo(region.getRegionNo());
		return saasRoomMapper.insert(arg0);
	}

	@Override
	public SaasRoom selectById(String arg0) {
		// TODO Auto-generated method stub
		return saasRoomMapper.selectByPrimaryKey(arg0);
	}

	@Override
	public List<SaasRoom> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return saasRoomMapper.selectByPage(arg0);
	}

	@Override
	public int update(SaasRoom newRoom) {
		int result = 0;
		// 查询旧数据
		SaasRoom oldRoom = saasRoomMapper.selectByPrimaryKey(newRoom.getId());
		if (!StringUtils.isEmpty(oldRoom)) {
			// 房间号不变
			if (oldRoom.getRoomNumber().equals(newRoom.getRoomNumber())) {
				result = saasRoomMapper.updateByPrimaryKeySelective(newRoom);
			} else {
				// 房间号变化时要判断不重复
				SaasRoom param = new SaasRoom();
				param.setOrgId(oldRoom.getOrgId());
				param.setRegionId(oldRoom.getRegionId());
				param.setRoomNumber(newRoom.getRoomNumber());
				List<SaasRoom> roomList = saasRoomMapper.selectByEntity(param);
				if (roomList == null || roomList.size() <= 0) {
					saasRoomMapper.updateByPrimaryKeySelective(newRoom);
				}else{
					throw new BusinessException(MonitResultCode.PARAMETER_NULL_ERROR, "该楼栋已存在房间编号");
				}
			}
		}
		return result;
	}

	@Override
	@Transactional
	public String insertRoom(RoomExcel roomExcel, String regionId,OrgUser user) {
		SaasRoom room = new SaasRoom();		
		room.setOrgId(user.getOrgId());
		room.setRegionId(regionId);
		room.setRoomNumber(roomExcel.getRoomNumber());
		List<SaasRoom> roomList = saasRoomMapper.selectByEntity(room);
		// 房间号不重复可以插入
		if (null == roomList || roomList.size()==0){
			SaasRegion region = saasRegionMapper.selectByPrimaryKey(regionId);
			if (region.getIsDelete()== 1){
				region.setIsDelete((byte) 0);
				saasRegionMapper.updateByPrimaryKeySelective(region);
			}
			room.setRegionFullName(region.getRegionFullName() + "-" + region.getRegionName());
			room.setRegionNo(region.getRegionNo());
			int result = saasRoomMapper.insert(room);
			// 插入成功
			if(result==1){
				return "ok";
			}
			// 插入失败
			else {
				return "未知异常";
			}
		}
		// 房间号重复
		else {
			return "房间号重复";
		}
	}

	@Override
	public List<SaasRoom> selectRoomMeterByregionId(String regionId) {

		return saasRoomMapper.selectRoomMeterByregionId(regionId);
	}

	@Override
	public List<SaasRoom> selectByEntity(SaasRoom saasRoom) {
		return saasRoomMapper.selectByEntity(saasRoom);
	}

}
