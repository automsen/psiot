package cn.com.tw.saas.serv.controller.room;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;








import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.entity.db.cust.RoomAccountRecord;
import cn.com.tw.saas.serv.entity.db.read.ReadLast;
import cn.com.tw.saas.serv.entity.room.AllowanceRecord;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.service.cust.RoomAccountRecordService;
import cn.com.tw.saas.serv.service.read.ReadService;
import cn.com.tw.saas.serv.service.room.AllowanceService;
import cn.com.tw.saas.serv.service.room.RoomService;
import cn.com.tw.saas.serv.service.terminal.MeterService;

@RestController
@RequestMapping("terminal_search")
@Api(description="终端设备查询接口")
public class TerminalSearchController {
	
	
	@Autowired
	private RoomService roomService;
	@Autowired
	private MeterService meterService;
	@Autowired
	private ReadService readService;
	@Autowired
	private AllowanceService allowanceService;
	@Autowired
	private RoomAccountRecordService roomAccountRecordService;
	/**
	 * 
	 * 通过楼栋加房间号查询房间的
	 * 水电剩余量和充值记录(现用于长江大学)
	 * @param orgId 机构Id
	 * @param roomNo 房间号
	 * @return
	 */
	@ApiOperation(value="楼栋加房间号查询")
	@PostMapping("")
	public Response<?> roomSearch(@RequestParam("roomNo") String roomNo,@RequestParam("orgId") String orgId,HttpServletResponse response) {
		if(StringUtils.isEmpty(roomNo)
				||StringUtils.isEmpty(orgId)
				||roomNo.trim().equals("")
				|| orgId.trim().equals("")){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "房间号为空");
		}
		Room param = new Room();
		param.setRoomNumber(roomNo.trim());
		param.setOrgId(orgId.trim());
		try {
			List<Room> rooms=roomService.selectByEntity(param);
			if(rooms==null||rooms.size()==0){
				return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR,"无此房间");
			}
			Room room=rooms.get(0);
			String roomId=room.getRoomId();
			//判断房间使用情况
			if(room.getAccountStatus()!=1 && room.getAccountStatus()!=2){
				return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "该房间未使用");
			}						
			//根据roomId查询房间所使用仪表
			List<Meter> meters=meterService.selectAllInfoByRoomId(roomId);
			if(meters==null||meters.size()==0){
				return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR,"该房间未绑定仪表");
			}
			Meter eMeter=null; //电表
			Meter wMeter=null; //水表
			for(Meter meter:meters){
				if(meter.getEnergyType().equals("110000")){
					eMeter=meter;
				}else if(meter.getEnergyType().equals("120000")){
					wMeter=meter;
				}
			}
			//根据查询出来的电表查询电表通断状况
			BigDecimal isOff=null;
			if(eMeter!=null){
				ReadLast readLast=readService.selectByAddrAndItem(eMeter.getMeterAddr(), "isOff");
				if(readLast!=null){
					isOff=readLast.getReadValue();
				}
			}
			//根据RoomId查询剩余补助用量的更新时间
			Date eDate=null; //电表更新时间
			Date wDate=null; //水表更新时间
			List<AllowanceRecord> records=allowanceService.selectByRoomId(roomId);
			for(AllowanceRecord record:records){
				if(record.getEnergyType()=="110000"){
					eDate=record.getUpdateTime();
				}else if(record.getEnergyType()=="120000"){
					wDate=record.getUpdateTime();
				}
			}
			//根据roomId查询充值记录
			List<RoomAccountRecord> raRecords=roomAccountRecordService.selectRecordsByRoomId(roomId);
			//组织返回数据
			Map<String, Object> result=new HashMap<String, Object>();
			Map<String, Object> roomInfo=new HashMap<String, Object>();
			roomInfo.put("roomRegion", room.getRegionFullName()+"-"+room.getRoomNumber());
			roomInfo.put("roomBlance", room.getBalance());
			if(eMeter!=null){
				roomInfo.put("eMeterAddr", eMeter.getMeterAddr());
				roomInfo.put("isOff", isOff);
			}
			roomInfo.put("eAllowance", room.getElecAllowance());
			if(wMeter!=null){
				roomInfo.put("wMeterAddr", wMeter.getMeterAddr());
			}
			roomInfo.put("wAllowance", room.getWaterAllowance());
			if(eDate!=null){
				roomInfo.put("edate", eDate);
			}
			if(wDate!=null){
				roomInfo.put("wdate", wDate);
			}
			result.put("roomInfo", roomInfo);
			result.put("rechargeRecords", raRecords);
			response.setHeader("Access-Control-Allow-Origin", "*");
 			return Response.retn(ResultCode.OPERATION_IS_SUCCESS, "", result);
		} catch (BusinessException e) {
			return Response.retn(e.getCode(),e.getMessage());
		}	
	}
}
