package cn.com.tw.saas.serv.service.rule.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.cons.code.MonitResultCode;
import cn.com.tw.saas.serv.entity.db.rule.RuleAlarm;
import cn.com.tw.saas.serv.entity.db.rule.RuleAppend;
import cn.com.tw.saas.serv.mapper.rule.RuleAppendMapper;
import cn.com.tw.saas.serv.service.rule.RuleAppendService;

@Service
public class RuleAppendServiceImpl implements RuleAppendService{
	
	@Autowired
	private RuleAppendMapper ruleAppendMapper;

	@Override
	public int deleteById(String appendId) {
		// TODO Auto-generated method stub
		return ruleAppendMapper.deleteByPrimaryKey(appendId);
	}

	@Override
	public int insert(RuleAppend ruleAppend) {
		
		//规则名判重
		RuleAppend param=new RuleAppend();
		param.setContName(ruleAppend.getContName());
		List<RuleAppend> dataList=ruleAppendMapper.selectByEntity(param);
		if(dataList!=null&&dataList.size()>0){
			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR,"规则名已存在");
		}
		
		/**
		 *判断是否为默认规则 
		 */
		if(ruleAppend.getIsDefault() == 1){
			RuleAppend ruleAppend2 = new RuleAppend();
			/**
			 * 查出机构下的默认规则
			 */
			ruleAppend2.setOrgId(ruleAppend.getOrgId());
			ruleAppend2.setIsDefault(ruleAppend.getIsDefault());
			ruleAppend2.setChargeType(ruleAppend.getChargeType());
			List<RuleAppend> appends = ruleAppendMapper.selectByEntity(ruleAppend2);
			/**
			 * 判断是否为第一条插进来的 默认规则
			 */
			if(appends != null && appends.size() > 0){
				ruleAppend2 = appends.get(0);
				ruleAppend2.setIsDefault((byte)0);
				ruleAppendMapper.updateByPrimaryKeySelective(ruleAppend2);
			}
		}
		
		return ruleAppendMapper.insert(ruleAppend);
	}

	@Override
	public int insertSelect(RuleAppend ruleAppend) {
		// TODO Auto-generated method stub
		return ruleAppendMapper.insertSelective(ruleAppend);
	}

	@Override
	public RuleAppend selectById(String appendId) {
		// TODO Auto-generated method stub
		return ruleAppendMapper.selectByPrimaryKey(appendId);
	}

	@Override
	public List<RuleAppend> selectByPage(Page page) {
		// TODO Auto-generated method stub
		return ruleAppendMapper.selectByPage(page);
	}

	@Override
	public int update(RuleAppend ruleAppend) {
		// TODO Auto-generated method stub
		return ruleAppendMapper.updateByPrimaryKey(ruleAppend);
	}

	@Override
	public int updateSelect(RuleAppend ruleAppend) {
		//规则名判重
		RuleAppend append=ruleAppendMapper.selectByPrimaryKey(ruleAppend.getAppendId());
		if(!append.getContName().equals(ruleAppend.getContName())){
			RuleAppend param=new RuleAppend();
			param.setOrgId(ruleAppend.getOrgId());
			param.setContName(ruleAppend.getContName());
			List<RuleAppend> appends=ruleAppendMapper.selectByEntity(param);
			if(appends!=null&&appends.size()>0){
				throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR,"规则名已存在");
			}
		}
		
		
		
		/**
		 *判断是否为默认规则 
		 */
		if(ruleAppend.getIsDefault() == 1){
			RuleAppend ruleAppend2 = new RuleAppend();
			/**
			 * 查出机构下的默认规则
			 */
			ruleAppend2.setOrgId(ruleAppend.getOrgId());
			ruleAppend2.setIsDefault(ruleAppend.getIsDefault());
			ruleAppend2.setChargeType(ruleAppend.getChargeType());
			List<RuleAppend> appends = ruleAppendMapper.selectByEntity(ruleAppend2);
			/**
			 * 判断是否为第一条插进来的 默认规则
			 */
			if(appends != null && appends.size() > 0){
				ruleAppend2 = appends.get(0);
				ruleAppend2.setIsDefault((byte)0);
				ruleAppendMapper.updateByPrimaryKeySelective(ruleAppend2);
			}
		}
		
		return ruleAppendMapper.updateByPrimaryKeySelective(ruleAppend);
	}

	@Override
	public List<RuleAppend> selectByEntity(RuleAppend ruleAppend) {
		return ruleAppendMapper.selectByEntity(ruleAppend);
	}

}
