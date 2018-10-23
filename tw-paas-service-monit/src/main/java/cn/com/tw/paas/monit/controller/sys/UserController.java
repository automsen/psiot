package cn.com.tw.paas.monit.controller.sys;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;
import cn.com.tw.common.exception.RequestParamValidException;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.common.utils.cons.code.MonitResultCode;
import cn.com.tw.paas.monit.entity.db.sys.User;
import cn.com.tw.paas.monit.service.sys.UserService;

@ApiIgnore
@RestController
@RequestMapping("user")
public class UserController {
	
	
	@Autowired
	private UserService userService;

	/**
	 * 用户页面
	 * @param page
	 * @return
	 */
	@ApiOperation(value="获取用户列表", notes="")
	@ApiImplicitParam(name = "user", value = "用户详细实体user", required = true, dataType = "Page")
	@GetMapping("page")
	public Response<?> selectUserPage(Page page){
		List<User> users = userService.selectByPage(page);
		page.setData(users);
		return Response.ok(page);
	}
	
	/**
	 * 用户查询
	 * @param user
	 * @return
	 */
	@GetMapping("all")
	public Response<?> selectUserAll(User user){
		List<User> users = userService.selectUserAll(user);
		return Response.ok(users);
	}
	
	/**
	 * 添加
	 * @param user
	 * @return
	 */
	@PostMapping()
	public Response<?> addUser(@RequestBody @Valid User user, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), br.toString());
		}
		try {
			userService.insertSelect(user);
		} catch (Exception e) {
            return Response.retn(MonitResultCode.DATA_EXISTS_ERROR, e.getMessage());
		}
		return Response.ok();
	}
	
	/**
	 * 详情查询
	 * @param equipId
	 * @return
	 */
	@GetMapping("{userId}")
	public Response<?> selectByEquipId(@PathVariable String userId){
		User user = userService.selectById(userId);
		return Response.ok(user);
	}
	
	/**
	 * 修改
	 * @param dict
	 * @return
	 */
	@PutMapping()
	public Response<?> updateUser(@RequestBody @Valid User user, BindingResult br){
		if(br.hasErrors()){
			throw new  RequestParamValidException(br.getAllErrors(), br.toString());
		}
		try {
			userService.updateSelect(user);
		} catch (Exception e) {
			return Response.retn(MonitResultCode.DATA_EXISTS_ERROR, e.getMessage());
		}
		return Response.ok();
	}

	/**
	 * 删除
	 * @param userId
	 * @return
	 */
	@DeleteMapping("{userId}")
	public Response<?> deleteUser(@PathVariable String userId){
		try {
			userService.deleteById(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Response.ok();
	}
	
	/**
	 * 
	 * @return
	 */
	@GetMapping("name/{name}")
	public Response<?> selectByName(@PathVariable String name){
		User user = userService.selectByName(name);
		return Response.ok(user);
	}
	
}
