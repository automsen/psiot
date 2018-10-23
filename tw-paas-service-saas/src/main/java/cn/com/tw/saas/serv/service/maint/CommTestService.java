package cn.com.tw.saas.serv.service.maint;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.view.maint.CommTestModel;

public interface CommTestService {

	public List<CommTestModel> selectByPage(Page page);

}
