package cn.com.tw.paas.monit.controller.org;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.monit.entity.db.org.OrgLocation;
import cn.com.tw.paas.monit.service.org.OrgLocationService;

@RestController
@RequestMapping("location")
public class OrgLocationController {

	@Autowired
	private OrgLocationService orgLocationService;
	
	/**
	 * 根据村查询下联集合
	 * @param districtId
	 * @return
	 */
	/*@GetMapping("areaId/{areaId}")
	public Response<?> selectHouseNumByDistrictId(@PathVariable String areaId){
		List<OrgResident> residentList = orgLocationService.selectHouseNumByAroeId(areaId);
		return Response.ok(residentList);
	}*/
	
	/**
	 * 返回村下所有门牌号
	 * @param resident
	 * @return
	 */
	@PostMapping("list")
	public Response<?> selectHouseListByLikeForApp(@RequestBody OrgLocation orgLocation){
		List<Map<String, String>> map = orgLocationService.selectHouseListByLikeForApp(orgLocation);
		return Response.ok(map);
	}
}
