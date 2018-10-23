package cn.com.tw.paas.monit.controller.sys;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.com.tw.common.web.entity.Response;
import cn.com.tw.paas.monit.entity.db.sys.Area;
import cn.com.tw.paas.monit.service.sys.AreaService;

@RestController
@RequestMapping("area")
public class AreaController {
	@Autowired
	private AreaService areaService;
	
	@GetMapping("parent/{parentId}")
	public Response<?> selectDistrictByParentId(@PathVariable String parentId){
		List<Area> areaList = areaService.selectAreaByParentId(parentId);
		return Response.ok(areaList);
	}
}
