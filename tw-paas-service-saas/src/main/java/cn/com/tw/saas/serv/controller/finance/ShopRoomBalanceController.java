package cn.com.tw.saas.serv.controller.finance;

import java.util.List;

import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import cn.com.tw.common.utils.tools.excel.ExcelControllerHelper;
import cn.com.tw.common.utils.tools.excel.ExportUtil;
import cn.com.tw.common.utils.tools.excel.ViewExcel;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.ExcelUtils;
import cn.com.tw.saas.serv.entity.audit.Clearing;
import cn.com.tw.saas.serv.entity.org.OrgAccountRecord;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.service.room.RoomService;

@RestController
@RequestMapping("shopbalance")
@Api(description = "商铺房间余额")
public class ShopRoomBalanceController {

	@Autowired
	private RoomService roomService;
	
	@GetMapping("page")
	public Response<?> selectByPage(Page page){
		List<Room> saasRooms = roomService.selectShopRoomBalanceByPage(page);
		page.setData(saasRooms);
		return Response.ok(page);
	}
	
	@GetMapping("/export")
	public ModelAndView shopbalanceExport(Room room){
		ExportUtil exprot = null;
		try {
			String[] headers = {"楼栋","房间编号","商铺名字","用户姓名","联系方式","账户可用余额（元）","水表子账户余额（元）","电表子账户余额（元）","合计余额（元）"};
			String[] ENheaders = {"Building","Room No.","Store name","Username","Contact info","Available balance","Water meter balance","Electric meter balance","AmountBalance"};
			String[] fields = {"regionFullName","roomNumber","roomName","customerRealname", "customerMobile1","balance","subWaterBalanceStr","subElecBalanceStr","totleBalance"};
			List<Room> dataList = roomService.shopbalanceExport(room);
			if(dataList == null || dataList.size() <= 0){
				Room room2 = new Room();
				dataList.add(room2);
			}
			Room  record = (Room) ExcelUtils.loadTotalColumns(dataList,"regionFullName",new String[]{"balance","subWaterBalanceStr","subElecBalanceStr","totleBalance"});
			if(!StringUtils.isEmpty(room.getIsEnExcel())){
				record.setRegionFullName("Total");
			}
			
			dataList.add(record);
			if(StringUtils.isEmpty(room.getIsEnExcel())){
				exprot  = ExcelControllerHelper.simpleExportData(dataList, headers, fields, "商铺房间余额","商铺房间余额");
			}else{
				exprot  = ExcelControllerHelper.simpleExportData(dataList, ENheaders, fields, "Shop balance","Shop balance");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return new ModelAndView(new ViewExcel(), exprot.build());
	}
	
}
