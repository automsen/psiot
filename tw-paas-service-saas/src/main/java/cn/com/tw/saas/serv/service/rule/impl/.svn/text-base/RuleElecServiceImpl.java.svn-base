package cn.com.tw.saas.serv.service.rule.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.rule.RuleElec;
import cn.com.tw.saas.serv.mapper.rule.RuleElecMapper;
import cn.com.tw.saas.serv.service.rule.RuleElecService;

@Service
public class RuleElecServiceImpl implements RuleElecService{

	@Autowired
	private RuleElecMapper ruleElecMapper;
	
	@Override
	public int deleteById(String ruleId) {
		// TODO Auto-generated method stub
		ruleElecMapper.deleteByPrimaryKey(ruleId);
		return 0;
	}

	@Override
	@Transactional
	public int insert(RuleElec ruleElec) {
		
		//规则名判重
		List<RuleElec> dataList=ruleElecMapper.selectByName(ruleElec.getRuleName());
		if(dataList!=null&&dataList.size()>0){
			throw new BusinessException(ResultCode.DATABASE_OPERATIOIN_IS_ERROR,"规则名已存在");
		}
		
		/**
		 * 判断传入规则 是否为默认规则
		 */
		if(ruleElec.getIsDefault() == 1){
			/**
			 * 根据房间类型和回路类型 查处机构默认规则
			 */
			RuleElec ruleElec2 = new RuleElec();
			
			ruleElec2.setRuleType(ruleElec.getRuleType());
			ruleElec2.setOrgId(ruleElec.getOrgId());
			ruleElec2.setRoomUse(ruleElec.getRoomUse());
			ruleElec2.setIsDefault(ruleElec.getIsDefault());
			List<RuleElec> elecs = ruleElecMapper.selectByEntity(ruleElec2);
			
			/**
			 * 判断是否已存在默认规则
			 */
			if(elecs != null && elecs.size() > 0){
				ruleElec2 = elecs.get(0);
				ruleElec2.setIsDefault((byte)0);
				ruleElecMapper.updateByPrimaryKeySelective(ruleElec2);
			}
		}
		
		
		ruleElecMapper.insert(ruleElec);
		return 0;
	}

	@Override
	public int insertSelect(RuleElec ruleElec) {
		// TODO Auto-generated method stub
		ruleElecMapper.insertSelective(ruleElec);
		return 0;
	}

	@Override
	public RuleElec selectById(String ruleId) {
		// TODO Auto-generated method stub
		return ruleElecMapper.selectByPrimaryKey(ruleId);
	}

	@Override
	public List<RuleElec> selectByPage(Page page) {
		// TODO Auto-generated method stub
		return ruleElecMapper.selectByPage(page);
	}

	@Override
	public int update(RuleElec ruleElec) {
		// TODO Auto-generated method stub
		ruleElecMapper.updateByPrimaryKey(ruleElec);
		return 0;
	}

	@Override
	@Transactional
	public int updateSelect(RuleElec ruleElec) {
		
		/**
		 * 判断传入规则 是否为默认规则
		 */
		if(ruleElec.getIsDefault() == 1){
			/**
			 * 根据房间类型和回路类型 查处机构默认规则
			 */
			RuleElec ruleElec2 = new RuleElec();
			
			ruleElec2.setRuleType(ruleElec.getRuleType());
			ruleElec2.setOrgId(ruleElec.getOrgId());
			ruleElec2.setRoomUse(ruleElec.getRoomUse());
			ruleElec2.setIsDefault(ruleElec.getIsDefault());
			List<RuleElec> elecs = ruleElecMapper.selectByEntity(ruleElec2);
			
			/**
			 * 判断是否已存在默认规则
			 */
			if(elecs != null && elecs.size() > 0){
				ruleElec2 = elecs.get(0);
				ruleElec2.setIsDefault((byte)0);
				ruleElecMapper.updateByPrimaryKeySelective(ruleElec2);
			}
		}
		
		ruleElecMapper.updateByPrimaryKeySelective(ruleElec);
		return 0;
	}

	@Override
	public List<RuleElec> selectByEntity(RuleElec ruleElec) {
		return ruleElecMapper.selectByEntity(ruleElec);
	}

}
