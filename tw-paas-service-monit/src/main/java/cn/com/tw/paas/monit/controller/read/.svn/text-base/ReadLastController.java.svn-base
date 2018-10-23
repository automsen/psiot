package cn.com.tw.paas.monit.controller.read;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.read.ReadHistoryExtend;
import cn.com.tw.paas.monit.entity.db.read.ReadHistory;
import cn.com.tw.paas.monit.entity.db.read.ReadLast;
import cn.com.tw.paas.monit.service.read.ReadService;

/**
 * 最后读数
 * 
 * @author Administrator
 *
 */
@RestController
@RequestMapping("readLast")
public class ReadLastController {

	@Autowired
	private ReadService readService;

	/**
	 * 历史读数（分页）
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("page")
	public Response<?> selectHistoryPage(Page page) {
		List<ReadHistory> readLasts = readService.selectHistoryPage(page);
		page.setData(readLasts);
		return Response.ok(page);
	}
	
	@GetMapping("show")
	public Response<?> selectHistoryShow(Page page) {
		readService.selectHistoryShow(page);
		return Response.ok(page);
	}

	/**
	 * 最后读数
	 * 
	 * @param readLast
	 * @return
	 */
	@GetMapping()
	public Response<?> selectLast(ReadLast readLast) {
		List<ReadLast> readLasts = readService.selectLast(readLast);
		return Response.ok(readLasts);
	}

	/**
	 * 历史读数
	 * 
	 * @param readLast
	 * @return
	 */
	@GetMapping("history")
	public Response<?> selectHistory(ReadHistoryExtend param) {
		List<ReadHistory> readLasts = readService.selectHistory(param);
		return Response.ok(readLasts);
	}
	
	@GetMapping("24hours")
	public Response<?> select24hours(ReadHistoryExtend readHistoryExtend){
		List<ReadHistory> readHistories = readService.select24hours(readHistoryExtend);
		return Response.ok(readHistories);
	}
	

}
