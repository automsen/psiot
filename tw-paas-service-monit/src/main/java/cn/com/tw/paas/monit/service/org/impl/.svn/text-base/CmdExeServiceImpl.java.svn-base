package cn.com.tw.paas.monit.service.org.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.utils.tools.json.JsonUtils;
import cn.com.tw.common.web.entity.Response;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.common.utils.cons.code.MonitResultCode;
import cn.com.tw.paas.monit.entity.db.org.CmdExe;
import cn.com.tw.paas.monit.entity.db.org.EquipGroup;
import cn.com.tw.paas.monit.entity.db.org.EquipInsGroup;
import cn.com.tw.paas.monit.entity.db.org.InsExe;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquipParamDtu;
import cn.com.tw.paas.monit.mapper.org.CmdExeMapper;
import cn.com.tw.paas.monit.mapper.org.EquipGroupMapper;
import cn.com.tw.paas.monit.mapper.org.EquipInsGroupMapper;
import cn.com.tw.paas.monit.mapper.org.InsExeMapper;
import cn.com.tw.paas.monit.mapper.org.TerminalEquipParamDtuMapper;
import cn.com.tw.paas.monit.service.inn.CmdIssueService;
import cn.com.tw.paas.monit.service.org.CmdExeService;

@Service
public class CmdExeServiceImpl implements CmdExeService{

	@Autowired
	private CmdExeMapper cmdExeMapper;
	@Autowired
	private EquipGroupMapper equipGroupMapper;
	@Autowired
	private EquipInsGroupMapper equipInsGroupMapper;
	@Autowired
	private InsExeMapper insExeMapper;
	@Autowired
	private CmdIssueService cmdIssueService;
	
	@Autowired
	private TerminalEquipParamDtuMapper terminalEquipParamDtuMapper;

	@Override
	public CmdExe selectById(String id) {
		return cmdExeMapper.selectByPrimaryKey(id);
	}

	
	@Override
	@Transactional
	public int updateCmdInsStatus(CmdExe arg0) {
		
		Byte startStatus = new Byte("10");
		Byte successStatus = new Byte("1");
		// 更新指令状态
		if(arg0.getCurrIns() != null){
			insExeMapper.updateByPrimaryKeySelective(arg0.getCurrIns());
		}
		// 执行中不更新命令状态
		if(!startStatus.equals(arg0.getStatus())){
			
			
			int handleTimes = arg0.getHandleTimes();
			int limitHandleTimes = arg0.getLimitHandleTimes();
			
			TerminalEquipParamDtu tepd = new TerminalEquipParamDtu();
			// 执行没有成功
			if(!successStatus.equals(arg0.getStatus())){
				// 判断命令重试次数
				if(handleTimes < limitHandleTimes){
					// 执行命令重试
					cmdIssueService.sendCmdToRedis(arg0, null);
				}
				//更新DTU设备下发状态
				if(StringUtils.isNotBlank(arg0.getBusinessNo())&&arg0.getBusinessNo().indexOf("DTU_") != -1){
					tepd.setEquipId(arg0.getConnAddr());
					tepd.setSendStatus((byte) 2);
					terminalEquipParamDtuMapper.updateByPrimaryKeySelective(tepd);
				}
			}else{//执行成功
				if(StringUtils.isNotBlank(arg0.getBusinessNo())&& arg0.getBusinessNo().indexOf("DTU_") != -1){
					tepd.setEquipId(arg0.getConnAddr());
					tepd.setSendStatus((byte) 1);
					terminalEquipParamDtuMapper.updateByPrimaryKeySelective(tepd);
				}
			}
			// 更新状态
			cmdExeMapper.updateByPrimaryKey(arg0);
		}
		return 0;
	}
	
	

	/**
	 * 读回来的仪表和数据库的拼接
	 */
	@Override
	public Response<?> selectEquipGroup(String id) {
		EquipGroup equipGroup = new EquipGroup();
		CmdExe cmdExe = cmdExeMapper.selectByPrimaryKey(id);
		/**
		 * 数据库取的数据
		 */
		List<EquipGroup> equipGroups = equipGroupMapper.selectAll(equipGroup);
		if(!ResultCode.OPERATION_IS_SUCCESS.equals(cmdExe.getParam())){
			return Response.retn(MonitResultCode.UNKNOW_ERROR, "",equipGroups);
		}
		String returnValue = cmdExe.getReturnValue();
		Map<String, String> map = JsonUtils.jsonToPojo(returnValue, Map.class); 
		String netEquipNumber = map.get("addr");//网关编号
		equipGroup.setCommAddr(netEquipNumber);
		
		String[] resultArr;
		/**
		 * 采集器读回来的
		 */
		List<EquipGroup> meterAddrs = new ArrayList<EquipGroup>();
		String dateValues = map.get("dataValues");
		resultArr = dateValues.split(",");
		for(int i=0;i<resultArr.length;i++){
	        if(i%2==0){
	        	EquipGroup equipGroup2 = new EquipGroup();
	        	equipGroup2.setChildCommAddr(resultArr[i]);
	        	meterAddrs.add(equipGroup2);
	        }
		}
		
		/**
		 * 系统录入的网关下的仪表  与读回来的做匹配
		 */
		List<EquipGroup> equipGroups2 = new ArrayList<EquipGroup>();
		/**
		 * 库里 没有读回来的
		 */
		if(equipGroups ==null || equipGroups.size() <= 0){
			return Response.ok(meterAddrs);
		}
		
		/**
		 * 读回来没有返回库里取的
		 */
		if(meterAddrs ==null || meterAddrs.size() <= 0){
			return Response.ok(equipGroups);
		}
		
		for (EquipGroup meterAddr : meterAddrs) {
			EquipGroup equip1 = new EquipGroup();
			for (EquipGroup equip : equipGroups) {
				/**
				 * 与数据库表一致的 往list里面放 状态为1
				 */
				if(meterAddr.equals(equip.getChildCommAddr())){
					/**
					 * callback
					 */
					equip1.setChildStatus((byte) 1);
					equip1.setChildCommAddr(meterAddr.getChildCommAddr());
					/**
					 * 数据库修改
					 */
					equip.setChildStatus((byte) 1);
					equip.setUpdateTime(new Date(System.currentTimeMillis()));
					equipGroupMapper.updateByChildCommAddr(equip);
					equipGroups.remove(equip);
				}else{
					/**
					 * callback
					 */
					equip1.setChildStatus((byte) 2);
					equip1.setChildCommAddr(meterAddr.getChildCommAddr());
					/**
					 * 数据库修改
					 */
					equip.setChildStatus((byte) 2);
					equip.setUpdateTime(new Date(System.currentTimeMillis()));
					equipGroupMapper.updateByChildCommAddr(equip);
				}
			}
			equipGroups2.add(equip1);
		}
		equipGroups2.addAll(equipGroups);
		
		return Response.ok(equipGroups2);
	}

	
	/**
	 * 读回来的指令与数据库的拼接
	 */
	@Override
	public Response<?> selectEquipInsGroup(String id) {
		EquipInsGroup equipInsGroup = new EquipInsGroup();
		CmdExe cmdExe = cmdExeMapper.selectByPrimaryKey(id);
		/**
		 * 数据库取的数据
		 */
		List<EquipInsGroup> equipInsGroups = equipInsGroupMapper.selectByAll(equipInsGroup);
		if(!ResultCode.OPERATION_IS_SUCCESS.equals(cmdExe.getParam())){
			return Response.retn(MonitResultCode.UNKNOW_ERROR, "",equipInsGroups);
		}
		String returnValue = cmdExe.getReturnValue();
		Map<String, String> map = JsonUtils.jsonToPojo(returnValue, Map.class); 
		String netEquipNumber = map.get("addr");//网关编号
		equipInsGroup.setCommAddr(netEquipNumber);
		
		String[] resultArr;
		/**
		 * 采集器读回来的
		 */
		List<EquipInsGroup> equipInss = new ArrayList<EquipInsGroup>();
		String dateValues = map.get("dataValues");
		resultArr = dateValues.split(",");
		for(int i=0;i<resultArr.length;i++){
			EquipInsGroup equipIns2 = new EquipInsGroup();
        	equipIns2.setDataMarker(resultArr[i]);
        	equipInss.add(equipIns2);
		}
		
		/**
		 * 系统录入的网关下的仪表  与读回来的做匹配
		 */
		List<EquipInsGroup> equipInsGroups2 = new ArrayList<EquipInsGroup>();
		/**
		 * 库里 没有返回读回来的
		 */
		if(equipInsGroups ==null || equipInsGroups.size() <= 0){
			for (EquipInsGroup equipInsGroup2 : equipInss) {
				equipInsGroup2.setId(CommUtils.getUuid());
				equipInsGroup2.setInsStatus((byte)2);
				equipInsGroup2.setCreateTime(new Date(System.currentTimeMillis()));
				equipInsGroup2.setCommAddr(netEquipNumber);
				equipInsGroupMapper.insertSelective(equipInsGroup2);
			}
			return Response.ok(equipInss);
		}
		
		/**
		 * 读回来 没有返回库里取的
		 */
		if(equipInss ==null || equipInss.size() <= 0){
			return Response.ok(equipInsGroups);
		}
		
		for (EquipInsGroup equipIns : equipInss) {
			EquipInsGroup equip1 = new EquipInsGroup();
			for (EquipInsGroup equip : equipInsGroups) {
				/**
				 * 与数据库表一致的 往list里面放 状态为1
				 */
				if(equipIns.getDataMarker().equals(equip.getDataMarker())){
					/**
					 * callback
					 */
					equip1.setInsStatus((byte) 1);
					equip1.setDataMarker(equipIns.getDataMarker());
					/**
					 * 数据库修改
					 */
					equip.setInsStatus((byte) 1);
					equip.setUpdateTime(new Date(System.currentTimeMillis()));
					equipInsGroupMapper.updateByPrimaryKeySelective(equip);
					equipInsGroups.remove(equip);
				}else{
					/**
					 * callback
					 */
					equip1.setInsStatus((byte) 2);
					equip1.setDataMarker(equipIns.getDataMarker());
					/**
					 * 数据库修改
					 */
					equip.setInsStatus((byte) 2);
					equip.setUpdateTime(new Date(System.currentTimeMillis()));
					equipInsGroupMapper.updateByPrimaryKeySelective(equip);
				}
			}
			equipInsGroups2.add(equip1);
		}
		equipInsGroups2.addAll(equipInsGroups);
		
		/**
		 * 都会来的数据与数据库没有匹配上的做插入
		 */
		for (EquipInsGroup equipInsGroup2 : equipInsGroups2) {
			equipInsGroup2.setId(CommUtils.getUuid());
			equipInsGroup2.setInsStatus((byte)2);
			equipInsGroup2.setCreateTime(new Date(System.currentTimeMillis()));
			equipInsGroup2.setCommAddr(netEquipNumber);
			equipInsGroupMapper.insertSelective(equipInsGroup2);
		}
		
		return Response.ok(equipInsGroups2);
	}

	@Override
	public List<CmdExe> selectByEntity(CmdExe query) {
		return cmdExeMapper.selectByEntity(query);
	}

	
	/**
	 *  获取命令指令执行信息
	 */
	@Override
	public CmdExe findCmdIns(String cmdId) {
		CmdExe cmd = cmdExeMapper.selectByPrimaryKey(cmdId);
		InsExe queryModel = new InsExe();
		queryModel.setCmdExeId(cmdId);
		List<InsExe> ins = insExeMapper.selectByEntity(queryModel);
		
		InsExe currIns = null;
		InsExe tempIns = null;
		for (int i = 0,len = ins.size(); i < len; i++) {
			currIns = ins.get(i);
			if(i == 0){
				cmd.setCurrIns(currIns);
			}else{
				tempIns.setNextIns(currIns);
			}
			tempIns = currIns;
		}
		return cmd;
	}


	@Override
	public List<CmdExe> selectByPage(Page page) {
		return cmdExeMapper.selectByPage(page);
	}


	@Override
	public void updateCmdExeSelective(CmdExe cmd) {
		cmdExeMapper.updateByPrimaryKeySelective(cmd);
	}


	


}
