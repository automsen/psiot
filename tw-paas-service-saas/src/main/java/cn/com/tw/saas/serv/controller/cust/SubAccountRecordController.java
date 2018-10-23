package cn.com.tw.saas.serv.controller.cust;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.cust.SubAccountRecord;
import cn.com.tw.saas.serv.service.cust.SubAccountRecordService;

/**
 * 子账户  仪表账户
 * @author Administrator
 *
 */
@RestController
@RequestMapping("subAccountRecord")
public class SubAccountRecordController {

	@Autowired
	private SubAccountRecordService subAccountRecordService;
	
	@GetMapping("page")
	public Response<?> selectByPage(Page page){
		List<SubAccountRecord> accountRecords = subAccountRecordService.selectByPage(page);
		for (SubAccountRecord subAccountRecord : accountRecords) {
			if(!StringUtils.isEmpty(subAccountRecord.getDetails())){
				Map<String, String> map = JsonUtils.jsonToPojo(subAccountRecord.getDetails(), Map.class);
				if(!StringUtils.isEmpty(map)){
					if(!StringUtils.isEmpty(map.get("paice"))){
						subAccountRecord.setPrice(map.get("paice"));
					}
					if(!StringUtils.isEmpty(map.get("readValue"))){
						subAccountRecord.setReadValue(map.get("readValue"));
					}
				}
			}
		}
		page.setData(accountRecords);
		return Response.ok(page);
	}
}
