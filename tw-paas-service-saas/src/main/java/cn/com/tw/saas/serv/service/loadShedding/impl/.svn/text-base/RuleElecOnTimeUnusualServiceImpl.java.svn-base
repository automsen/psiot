package cn.com.tw.saas.serv.service.loadShedding.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.loadShedding.RuleElecOnTimeUnusual;
import cn.com.tw.saas.serv.entity.loadShedding.RuleElecOnTimeUsual;
import cn.com.tw.saas.serv.mapper.loadShedding.RuleElecOnTimeUnusualMapper;
import cn.com.tw.saas.serv.mapper.loadShedding.RuleElecOnTimeUsualMapper;
import cn.com.tw.saas.serv.mapper.org.OrgMapper;
import cn.com.tw.saas.serv.service.loadShedding.RuleElecOnTimeUnusualService;

@Service
public class RuleElecOnTimeUnusualServiceImpl implements RuleElecOnTimeUnusualService {

	@Autowired 
	private RuleElecOnTimeUnusualMapper ruleElecOnTimeUnusualMapper;
	
	@Autowired
	private RuleElecOnTimeUsualMapper ruleElecOnTimeUsualMapper;
	
	@Autowired
	private OrgMapper orgMapper;
	
	@Override
	public int deleteById(String arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insert(RuleElecOnTimeUnusual arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertSelect(RuleElecOnTimeUnusual arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RuleElecOnTimeUnusual selectById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RuleElecOnTimeUnusual> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(RuleElecOnTimeUnusual arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateSelect(RuleElecOnTimeUnusual arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<RuleElecOnTimeUnusual> selectByStartAndEndTime(RuleElecOnTimeUnusual reotu) {
		int dayNum = daysBetween(reotu);
		RuleElecOnTimeUsual usual = new RuleElecOnTimeUsual();
		List<RuleElecOnTimeUnusual> newList = new ArrayList<RuleElecOnTimeUnusual>();
		usual.setOrgId(reotu.getOrgId());
		List<RuleElecOnTimeUnusual> unusualList = ruleElecOnTimeUnusualMapper.selectByStartAndEndTime(reotu);
		List<RuleElecOnTimeUsual> usualList = ruleElecOnTimeUsualMapper.selectByAll(usual);
		Calendar cal = Calendar.getInstance();
		cal.setTime(reotu.getStartDay());
		
		for (int i = 0; i < dayNum; i++) {
			boolean isGoOn = true;
			for (RuleElecOnTimeUnusual re : unusualList) {
				if(DateUtils.isSameDay(re.getStartDay(), cal.getTime())){//判断时间等于
					//如果时间等于就将特殊日期数据塞入新数组
					System.out.println(DateUtils.isSameDay(re.getStartDay(), cal.getTime()));
					newList.add(re);
					isGoOn = false;
				}
			}
			if(isGoOn){
				//根据星期循环插入对应数据模版
				int week = cal.get(Calendar.DAY_OF_WEEK);
				for (RuleElecOnTimeUsual re : usualList) {
					if(re.getDayOfWeek() == week){
						RuleElecOnTimeUnusual usual3 = new RuleElecOnTimeUnusual();
						usual3.setOnTime(re.getOnTime());
						usual3.setOffTime(re.getOffTime());
						usual3.setSwitchs(re.getSwitchs());
						usual3.setStartDay(cal.getTime());
						newList.add(usual3);
					}
				}
			}
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		//调用公共方法来处理每日或某日的开关时间设置。
		//getDayOnOrOffTime(unusualList, usualList, dayNum);
		return newList;
	}
	
	//获取时间差
	private int daysBetween(RuleElecOnTimeUnusual reotu) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(reotu.getStartDay());  
        long time1 = cal.getTimeInMillis();  
        cal.setTime(reotu.getEndDay());  
        long time2 = cal.getTimeInMillis();        
        long between_days=(time2-time1)/(1000*3600*24);    
        return Integer.parseInt(String.valueOf(between_days));             
    }

	@Override
	public int replaceRuleElecOnTimeUnusual(RuleElecOnTimeUnusual info) {
		// TODO Auto-generated method stub
		info.setCreateTime(new java.util.Date());
		return ruleElecOnTimeUnusualMapper.replace(info);
	}

	@Override
	public List<RuleElecOnTimeUnusual> selectBySomeDay(RuleElecOnTimeUnusual info) {
		RuleElecOnTimeUsual reotu = new RuleElecOnTimeUsual();
		try {
			//查询所有特殊日期的机构（不区分机构）
			List<RuleElecOnTimeUnusual> list = ruleElecOnTimeUnusualMapper.selectByStartAndEndTimeToAll(info);
			
			//查询对应今日星期数的所有数据（不区分机构）
			Calendar cal = Calendar.getInstance();
			cal.setTime(info.getTimeNow());
			int week = cal.get(Calendar.DAY_OF_WEEK);
			reotu.setDayOfWeek(week);
			List<RuleElecOnTimeUsual> reList = ruleElecOnTimeUsualMapper.selectByScreeningConditionToAll(reotu);
			List<RuleElecOnTimeUnusual> newList = new ArrayList<RuleElecOnTimeUnusual>();
			
			if(list != null && list.size() > 0 ){
				List<String> orgIdList = new ArrayList<String>();
				String oldOrg = "";
				for (RuleElecOnTimeUnusual ruleElecOnTimeUnusual : list) {
					//如果上一个id不等于本次循环id，就更新
					if(!oldOrg.equals(ruleElecOnTimeUnusual.getOrgId())){
						oldOrg = ruleElecOnTimeUnusual.getOrgId();
						//获取今日有特殊安排的机构。
						orgIdList.add(oldOrg);
					}
				}
				
				if(null != reList && reList.size() > 0 ){
					//循环模版表，如果有特殊日期的机构就剔除，做两组集合去重，优先保留第一个集合，从第二个集合中去重，再组合
					for (int i = 0; i < reList.size(); i++) {
						for (String orgId : orgIdList) {
							if(reList.get(i).getOrgId().equals(orgId)){
								reList.remove(i);
							}
						}
					}
					
					//循环没有特殊日期的模版集合转换为后台所需类型对象
					for (RuleElecOnTimeUsual usual : reList) {
						RuleElecOnTimeUnusual unusual = new RuleElecOnTimeUnusual();
						unusual.setOrgId(usual.getOrgId());
						unusual.setOnTime(usual.getOnTime());
						unusual.setOffTime(usual.getOffTime());
						unusual.setSwitchs(usual.getSwitchs());
						list.add(unusual);
					}
				}
				return list;
				
			}else{//如果特殊日期为空，那么只取模版内容。
				if(reList != null && reList.size() > 0){
					for (RuleElecOnTimeUsual usual : reList) {
						RuleElecOnTimeUnusual unusual = new RuleElecOnTimeUnusual();
						unusual.setOrgId(usual.getOrgId());
						unusual.setOnTime(usual.getOnTime());
						unusual.setOffTime(usual.getOffTime());
						unusual.setSwitchs(usual.getSwitchs());
						newList.add(unusual);
					}
					return newList;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
