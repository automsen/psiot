package cn.com.tw.saas.serv.service.maint.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.view.maint.CommTestModel;
import cn.com.tw.saas.serv.mapper.view.CommTestMapper;
import cn.com.tw.saas.serv.service.maint.CommTestService;
@Service
public class CommTestServiceImpl implements CommTestService {

	@Autowired
	private CommTestMapper commTestMapper;

	@Override
	public List<CommTestModel> selectByPage(Page page) {
		return commTestMapper.selectByPage(page);
	}
}
