package cn.com.tw.saas.serv.controller.statistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.swagger.annotations.Api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;














import org.springframework.web.servlet.ModelAndView;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.excel.ExcelControllerHelper;
import cn.com.tw.common.utils.tools.excel.ExportUtil;
import cn.com.tw.common.utils.tools.excel.ViewExcel;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.saas.serv.entity.db.read.ReadRecord;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.terminal.Meter;
import cn.com.tw.saas.serv.service.read.ReadService;
import cn.com.tw.saas.serv.service.room.RoomService;
import cn.com.tw.saas.serv.service.terminal.MeterService;


@RestController
@RequestMapping("demand")
@Api(description = "需量数据接口")
public class DemandContronller {
	
	@Autowired
	private MeterService meterService;
	@Autowired
	private ReadService readService;
	@Autowired
	private RoomService roomService;
	/**
	 * 获取需量数据
	 * @param roomId 房间ID
	 * @param time 时间
	 * @return 需量记录
	 */
	@GetMapping("GetDemand")
	public Response<?> selectDemand(@RequestParam("roomId") String roomId,@RequestParam("time") String time){
		if(StringUtils.isBlank(roomId)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "房间号不能为空");
		}
		if(StringUtils.isBlank(time)){
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "时间不能为空");
		}
		SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd", Locale.CHINESE);
		try {
			sdf.parse(time);
		} catch (ParseException e) {
			return Response.retn(ResultCode.PARAM_VALID_ERROR, "日期格式错误");
		}
		roomId=roomId.trim();
		time=time.trim();
		JwtInfo jwt = JwtLocal.getJwt();
		String orgId = (String) jwt.getExtend().get("orgId");						
		String startTime=time+" 00:00:00";
		String endTime=time+" 23:59:59";
		try {
			//根据roomNo查询出房间的电表
			Meter mParm=new Meter();
			mParm.setOrgId(orgId);
			mParm.setRoomId(roomId);
			List<Meter> meters = meterService.selectByEntity(mParm);
			if(meters==null||meters.size()==0){
				return Response.retn(ResultCode.DATA_IS_EMPTY_ERROR, "房间下无仪表");
			}
			String meterAddr="";
			for(Meter meter:meters){
				if(meter.getEnergyType().equals("110000")){
					meterAddr=meter.getMeterAddr();
				}
			}
			if(StringUtils.isEmpty(meterAddr)){
				return Response.retn(ResultCode.PARAM_VALID_ERROR,"房间下无电表");
			}
			Map<String, Object> paramMap=new HashMap<String, Object>();
			paramMap.put("meterAddr", meterAddr);
			paramMap.put("startTime", startTime);
			paramMap.put("endTime", endTime);
			List<ReadRecord> readRecords=readService.selectDemandByMeterAddr(paramMap);
			return Response.ok(readRecords);
		} catch (BusinessException e) {
			return Response.retn(ResultCode.UNKNOW_ERROR,"系统异常");
		}
	}
	/**
	 * 获取带三相电表的房间的房间号和房间Id
	 * @param regionId 楼栋Id
	 * @return 房间号和房间Id的集合
	 */
	@GetMapping("getThreePhaseRoom")
	public Response<?> selectRoomWithThreePhaseByRegionId(@RequestParam(name="regionId") String regionId){
		JwtInfo jwt = JwtLocal.getJwt();
		String orgId = (String) jwt.getExtend().get("orgId");
		Room param=new Room();
		param.setRegionId(regionId);
		param.setOrgId(orgId);
		List<Room> rooms=roomService.selectRoomWithThreePhaseByRegionId(param);
		return Response.ok(rooms);
	}
	/**
	 * 需量数据导出
	 * @param roomId
	 * @param time
	 * @return
	 */
	@GetMapping("export")
	public ModelAndView export(@RequestParam("roomId") String roomId,@RequestParam("time") String time,@RequestParam("orgId") String orgId,@RequestParam("isEnExcel") String isEnExcel){				
		String startTime=time+" 00:00:00";
		String endTime=time+" 23:59:59";
		ExportUtil export = null;
		String[] headers={"时间","有功需量","无功需量"};
		String[] ENheaders={"Time","Active demand","Reactive demands"};
		String[] fields={"readTimeStr","value1","value2"};
		try {
			Meter mParm=new Meter();
			mParm.setOrgId(orgId);
			mParm.setRoomId(roomId);
			List<Meter> meters = meterService.selectByEntity(mParm);
			String meterAddr="";
			for(Meter meter:meters){
				if(meter.getEnergyType().equals("110000")){
					meterAddr=meter.getMeterAddr();
				}
			}
			Map<String, Object> paramMap=new HashMap<String, Object>();
			paramMap.put("meterAddr", meterAddr);
			paramMap.put("startTime", startTime);
			paramMap.put("endTime", endTime);
			List<ReadRecord> dataList=readService.selectDemandByMeterAddr(paramMap);
			//把读取时间转化String类型的
			SimpleDateFormat sdt=new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for(ReadRecord record : dataList){
				record.setReadTimeStr(sdt.format(record.getReadTime()));
			}
			String fileName=time+"需量数据";
			
			if(StringUtils.isEmpty(isEnExcel)){
				export=ExcelControllerHelper.simpleExportData(dataList, headers, fields, fileName, "需量数据");
			}else{
				fileName=time+"DemandData";
				export=ExcelControllerHelper.simpleExportData(dataList, ENheaders, fields, fileName, "DemandData");
			}
			
									
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(new ViewExcel(), export.build());
		
	}
}
