package cn.com.tw.saas.serv.controller.finance;

import java.util.List;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.com.tw.common.utils.tools.excel.ExcelControllerHelper;
import cn.com.tw.common.utils.tools.excel.ExportUtil;
import cn.com.tw.common.utils.tools.excel.ViewExcel;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.ExcelUtils;
import cn.com.tw.saas.serv.entity.db.cust.RoomAccountRecord;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.service.room.RoomService;

@RestController
@RequestMapping("dormbalance")
@Api(description = "宿舍房间余额")
public class DormRoomBalanceController {

	@Autowired
	private RoomService roomService;
	
	@GetMapping("page")
	public Response<?> selectByPage(Page page){
		List<Room> saasRooms = roomService.selectDormRoomBalanceByPage(page);
		page.setData(saasRooms);
		return Response.ok(page);
	}
	
	/**
	 * 宿舍房间余额清了
	 * @param rooms
	 * @return
	 */
	@PutMapping()
	public Response<?> updateRoomBalabce(@RequestBody List<Room> rooms){
		if(rooms == null || rooms.size() == 0 ){
			return Response.retn("111111", "请选择房间！");
		}
		
		try {
			roomService.roomBalabceReset(rooms);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return Response.ok();
	}
	
	
	@GetMapping("/export")
	public ModelAndView dormRoomBalanceExpert(Room room){
		ExportUtil exprot = null;
		try {
			String[] headers = {"楼栋","房间号","账户可用余额（元）","水表余额（元）","电表余额（元）","水计划用量（吨）","电计划用量（度）","合计余额（元）"};
			String[] ENheaders = {"Building","Room","Available Balance","Water's Balance","Electric's Balance","Planned Water","Planned Electricity","Amount"};
			String[] fields = {"regionFullName","roomNumber","balance","subWaterBalance","subElecBalance","waterAllowance","elecAllowance","totleBalance"};
			List<Room> dataList = roomService.dormRoomBalanceExpert(room);
			if(dataList == null || dataList.size() == 0){
				Room room1 = new Room();
				dataList.add(room1);
			}
			Room  record = (Room) ExcelUtils.loadTotalColumns(dataList,"regionFullName",new String[]{"balance","subWaterBalance","subElecBalance","waterAllowance","elecAllowance","totleBalance"});
			if(!StringUtils.isEmpty(room.getIsEnExcel())){
				record.setRegionFullName("Total");
			}
			dataList.add(record);
			
			if(StringUtils.isEmpty(room.getIsEnExcel())){
				exprot  = ExcelControllerHelper.simpleExportData(dataList, headers, fields, "宿舍房间余额","宿舍房间余额");
			}else{
				exprot  = ExcelControllerHelper.simpleExportData(dataList, ENheaders, fields, "Dorm Balance","Dorm Balance");
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ModelAndView(new ViewExcel(), exprot.build());
	}
	
	
}
