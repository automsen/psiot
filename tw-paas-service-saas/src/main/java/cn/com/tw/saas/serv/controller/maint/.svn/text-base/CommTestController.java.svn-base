package cn.com.tw.saas.serv.controller.maint;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.view.maint.CommTestModel;
import cn.com.tw.saas.serv.service.maint.CommTestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 运维——通信测试
 * @author admin
 *
 */
@RestController
@RequestMapping("commTest")
@Api(description = "运维——通信测试接口")
public class CommTestController {

	@Autowired
	private CommTestService commTestService;

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @return
	 */
	@ApiOperation(value = "分页查询", notes = "")
	@GetMapping("page")
	public Response<?> selectByPage(Page page) {
		try {
			List<CommTestModel> list = commTestService.selectByPage(page);
			page.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok(page);
	}
}
