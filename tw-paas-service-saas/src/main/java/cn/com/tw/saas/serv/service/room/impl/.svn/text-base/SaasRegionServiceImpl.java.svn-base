package cn.com.tw.saas.serv.service.room.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.security.entity.JwtInfo;
import cn.com.tw.common.security.utils.JwtLocal;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.common.utils.cons.code.MonitResultCode;
import cn.com.tw.saas.serv.entity.excel.RegionExcel;
import cn.com.tw.saas.serv.entity.org.Org;
import cn.com.tw.saas.serv.entity.org.OrgUser;
import cn.com.tw.saas.serv.entity.room.Room;
import cn.com.tw.saas.serv.entity.room.RoomTree;
import cn.com.tw.saas.serv.entity.room.SaasRegion;
import cn.com.tw.saas.serv.entity.room.SaasRoom;
import cn.com.tw.saas.serv.mapper.org.OrgMapper;
import cn.com.tw.saas.serv.mapper.room.RoomMapper;
import cn.com.tw.saas.serv.mapper.room.SaasRegionMapper;
import cn.com.tw.saas.serv.service.room.SaasRegionService;

@Service
public class SaasRegionServiceImpl implements SaasRegionService {
	// ROOMId的默认起始数
	private final static String DEFAULT_CODE = "001";
	// 层级跨度
	private final static String STEP_LEN = "3";
	@Autowired
	private SaasRegionMapper saasRegionMapper;
	@Autowired
	private OrgMapper orgMapper;
	@Autowired
	private RoomMapper roomMapper;
	@Override
	@Transactional
	public int deleteById(String regionId) {
		int result = 0;
		SaasRegion region = saasRegionMapper.selectByPrimaryKey(regionId);
		// 查询子节点
		SaasRegion temp = new SaasRegion();
		temp.setOrgId(region.getOrgId());
		temp.setPRegionNo(region.getRegionNo());
		List<SaasRegion> child = saasRegionMapper.selectByEntity(temp);
		// 存在子节点不可删除
		if (child != null && child.size() > 0){
			return 0;
		}
		else {
			// 查询兄弟节点包括自己
			temp = new SaasRegion();
			temp.setOrgId(region.getOrgId());
			temp.setPRegionNo(region.getPRegionNo());
			List<SaasRegion> brother = saasRegionMapper.selectByEntity(temp);
			// 自己是最后一个节点
			if (null != brother && brother.size() == 1){
				// 查询父级节点
				temp = new SaasRegion();
				temp.setOrgId(region.getOrgId());
				temp.setRegionNo(region.getPRegionNo());
				List<SaasRegion> pRegions = saasRegionMapper.selectByEntity(temp);
				// 更新辅助字段
				if (pRegions.size() > 0 && pRegions != null) {
					temp = pRegions.get(0);
					temp.setIsDelete((byte) 1);
					temp.setIsLeaf((byte) 1);
					saasRegionMapper.updateByPrimaryKeySelective(temp);
				}
			}
			result = saasRegionMapper.deleteByPrimaryKey(regionId);
		}
		return result;
	}

	/**
	 * 组建地址信息树桩结构
	 */
	@Override
	public List<RoomTree> loadRoomNodeTree(OrgUser saasOrgUser, String searchName) {
		List<RoomTree> tree = new ArrayList<RoomTree>();
		RoomTree tempTree = null;
		List<SaasRegion> resultList = null;
		List<SaasRegion> totalList = null;
		/**
		 * 要改 根据机构查询树的根节点
		 */
		
		RoomTree roomTree1 = new RoomTree();
		String orgName = "阿迪克";
		String orgId = "1";
		try {
			JwtInfo info = JwtLocal.getJwt();
			if(info != null){
				orgId = (String) info.getExt("orgId") == null ? orgId : (String) info.getExt("orgId");
				orgName = (String) info.getExt("orgName") == null ? orgName : (String) info.getExt("orgName");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		roomTree1.setName(orgName);
		roomTree1.setId("1");
		roomTree1.setOpen(true);
		roomTree1.setType("0");
		roomTree1.setOrgId(orgId);
		roomTree1.setTier(0);
		roomTree1.setRegionNo("1");
		tree.add(roomTree1);

		final Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("orgId", orgId);
		if (!StringUtils.isEmpty(searchName)) {
			totalList = saasRegionMapper.selectByMap(queryMap);
			queryMap.put("regionName", searchName);
		}
		// 根据名称查询树信息
		resultList = saasRegionMapper.selectByMap(queryMap);

		if (resultList == null || resultList.isEmpty()) {
			tree.add(new RoomTree("-1", "1", "无结果", "99"));
		} else {
			if (totalList != null) {
				mergeSearchNode(resultList, totalList);
			}
			for (SaasRegion saasRegion : resultList) {
				if(StringUtils.isEmpty(saasRegion.getRegionNumber())){
					saasRegion.setRegionNumber("--");
				}
				tempTree = new RoomTree();
				tempTree.setRegionNo(saasRegion.getRegionNo());
				tempTree.setpId(saasRegion.getPRegionNo());
				tempTree.setName(saasRegion.getRegionName()+"(#"+saasRegion.getRegionNumber()+")");
				tempTree.setType("0");
				tempTree.setIsLeaf(saasRegion.getIsLeaf());
				tempTree.setIsTop(saasRegion.getIsTop());
				tempTree.setIsDelete(saasRegion.getIsDelete());
				tempTree.setId(saasRegion.getRegionNo());
				tempTree.setOrgId(saasRegion.getOrgId());
				tempTree.setRegionId(saasRegion.getId());
				// 根据type来分配图片样式
				tree.add(tempTree);
			}
		}
		return tree;
	}

	/**
	 * 保证树结构的同时，将数据添加到树结构中
	 * 
	 * @param resultList
	 * @param totalList
	 * @return
	 */
	private void mergeSearchNode(List<SaasRegion> resultList, List<SaasRegion> totalList) {
		List<String> hasKeys = new ArrayList<String>();
		List<SaasRegion> tempList = new ArrayList<SaasRegion>();
		// 将所有地址保存为key,Object格式
		Map<String, SaasRegion> keyObjs = new HashMap<String, SaasRegion>();
		SaasRegion tempNodes = null;
		// 获取已查询的所有节点ID信息
		for (SaasRegion tempNode : resultList) {
			hasKeys.add(tempNode.getRegionNo());
		}
		// 将所有地址保存到Map中
		for (SaasRegion tempNode2 : totalList) {
			keyObjs.put(tempNode2.getRegionNo(), tempNode2);
		}
		tempList.addAll(resultList);
		for (int i = 0, len = tempList.size(); i < len; i++) {
			tempNodes = tempList.get(i);
			getParentNodes(tempNodes, hasKeys, keyObjs, resultList);
		}
	}

	/**
	 * 通过递归获取向上的子节点
	 * 
	 * @param tempNode
	 * @param hasKeys
	 * @param keyObjs
	 * @param resultList
	 */
	private void getParentNodes(SaasRegion tempNode, List<String> hasKeys, Map<String, SaasRegion> keyObjs,
			List<SaasRegion> resultList) {
		// 根目录直接返回
		if ("1".equals(tempNode.getPRegionNo())) {
			return;
		}
		SaasRegion currNode = keyObjs.get(tempNode.getPRegionNo());
		// 查询数据中不包含当前父节点
		if (!hasKeys.contains(currNode.getRegionNo())) {
			hasKeys.add(currNode.getPRegionNo());
			resultList.add(currNode);
		}
		// 递归上一层
		getParentNodes(currNode, hasKeys, keyObjs, resultList);
	}

	/**
	 * 新增楼栋
	 */
	@Override
	public int insertSelect(SaasRegion pRegion) {
		// 当前最大编号
		String maxNo = "";
		// 新编号
		String regionNo = "";
		// 编号头部
		String head = "";
		// 编号尾部
		String tail = "";
		
		SaasRegion saasRegion = new SaasRegion();
		saasRegion.setPRegionNo(pRegion.getRegionNo());
		saasRegion.setRegionNumber(pRegion.getRegionNumber());
		saasRegion.setOrgId(pRegion.getOrgId());
		List<SaasRegion> saasRegions = saasRegionMapper.selectByEntity(saasRegion);
		if(saasRegions != null && saasRegions.size() > 0){
			throw new BusinessException(MonitResultCode.DATA_EXISTS_ERROR,"楼栋编号已存在");
		}
		
		
		// 新增节点
		SaasRegion region = new SaasRegion();
		/**
		 * 页面传过来子节点名字
		 */
		region.setRegionNumber(pRegion.getRegionNumber());
		region.setRegionName(pRegion.getRegionName());
		int currLength = pRegion.getRegionNo().length();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("regionNo", pRegion.getRegionNo());
		queryMap.put("stepLen", STEP_LEN);
		queryMap.put("orgId", pRegion.getOrgId());
		// 查询下层级最大编号
		maxNo = saasRegionMapper.selectMaxregionNo(queryMap);
		// 无下层
		if (StringUtils.isEmpty(maxNo)) {
			regionNo = pRegion.getRegionNo() + DEFAULT_CODE;
		} else {
			tail = maxNo.substring(currLength - 1);
			head = pRegion.getRegionNo().substring(0, currLength-1);
			regionNo = head + (Integer.valueOf(tail) + 1);
		}
		// 无上级楼栋
		if ("1".equals(pRegion.getRegionNo())) {
			region.setTier((byte) 1); // 一级目录
			region.setIsTop((byte) 1);
			/*Org org = orgMapper.selectByPrimaryKey(pRegion.getOrgId());
			region.setRegionFullName(org.getOrgName());*/
			region.setRegionFullName(pRegion.getRegionName());
		} else {
			pRegion = saasRegionMapper.selectByPrimaryKey(pRegion.getId());
			pRegion.setIsLeaf((byte) 0);
			// 不可删除
			pRegion.setIsDelete((byte) 0);
			saasRegionMapper.updateByPrimaryKeySelective(pRegion);
			if (!StringUtils.isEmpty(pRegion.getRegionFullName())) {
				region.setRegionFullName(pRegion.getRegionFullName() + "-" + region.getRegionName());
			} else {
				region.setRegionFullName(region.getRegionName());
			}
			region.setTier((byte) (pRegion.getTier() + 1));
			region.setIsTop((byte) 0);
		}
		region.setOrgId(pRegion.getOrgId());
		region.setRegionNo(regionNo);
		region.setPRegionNo(pRegion.getRegionNo());
		region.setIsLeaf((byte) 1);
		// 可以删除
		region.setIsDelete((byte) 1);
		return saasRegionMapper.insert(region);
	}

	@Override
	public SaasRegion selectById(String arg0) {
		// TODO Auto-generated method stub
		return saasRegionMapper.selectByPrimaryKey(arg0);
	}

	@Override
	public List<SaasRegion> selectByPage(Page arg0) {
		// TODO Auto-generated method stub
		return saasRegionMapper.selectByPage(arg0);
	}

	@Override
	public int update(SaasRegion arg0) {
		// TODO Auto-generated method stub
		return saasRegionMapper.updateByPrimaryKeySelective(arg0);
	}

	@Override
	public void updateSelect(SaasRegion arg0) {		
		SaasRegion oldRegion=saasRegionMapper.selectByPrimaryKey(arg0.getId());
		if(oldRegion.getRegionName().equals(arg0.getRegionName())&&oldRegion.getRegionNumber().equals(arg0.getRegionNumber())){
			return;
		}
		
		SaasRegion pRegion=null;
		if(oldRegion.getIsTop()!=1){
			SaasRegion pParamRegion=new SaasRegion();
			pParamRegion.setOrgId(oldRegion.getOrgId());
			pParamRegion.setRegionNo(oldRegion.getPRegionNo());
			pRegion=saasRegionMapper.selectByEntity(pParamRegion).get(0);
		}
		SaasRegion regionParam=new SaasRegion();
		List<SaasRegion> regionResult;
		//如果修改楼栋名称
		if(!oldRegion.getRegionName().equals(arg0.getRegionName())){		
			//修改后的楼栋名不能与上级楼栋名相同
			if(pRegion!=null&&pRegion.getRegionName().equals(arg0.getRegionName())){
				throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR,"不能与上级楼栋名相同");
			}
			//修改后的楼栋名不能与同级楼栋名相同
			regionParam=new SaasRegion();
			regionParam.setOrgId(oldRegion.getOrgId());
			regionParam.setPRegionNo(oldRegion.getPRegionNo());
			regionParam.setRegionName(arg0.getRegionName());
			regionResult=saasRegionMapper.selectByEntity(regionParam);
			if(regionResult!=null&&regionResult.size()>0){
				throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR,"已存在该楼栋名称");
			}
		} 
		//如果修改楼栋编号
		if(!oldRegion.getRegionNumber().equals(arg0.getRegionNumber())){	
			//修改后的楼栋编号不能与上级楼栋编号相同
			if(pRegion!=null&&pRegion.getRegionNumber().equals(arg0.getRegionNumber())){
				throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR,"不能与上级楼栋编号相同");
			}
			//修改后的楼栋编号不能与同级楼栋编号相同
			regionParam=new SaasRegion();
			regionParam.setOrgId(oldRegion.getOrgId());
			regionParam.setPRegionNo(oldRegion.getPRegionNo());
			regionParam.setRegionNumber(arg0.getRegionNumber());
			regionResult=saasRegionMapper.selectByEntity(regionParam);
			if(regionResult!=null&&regionResult.size()>0){
				throw new BusinessException(MonitResultCode.ALREADY_EXIST_ERROR,"已存在此楼栋编号");
			}
		}
		if(pRegion==null){
			if(oldRegion.getRegionName().equals(arg0.getRegionName())){
				saasRegionMapper.updateByPrimaryKeySelective(arg0);
				//更新楼栋下房间的regionNumber
				Room roomParam=new Room();
				roomParam.setRegionId(arg0.getId());
				List<Room> roomResult=roomMapper.selectByEntity(roomParam);
				for(Room room:roomResult){
					Room updateRoom=new Room();
					updateRoom.setRoomId(room.getRoomId());
					updateRoom.setRegionNumber(arg0.getRegionNumber());
					roomMapper.updateByPrimaryKeySelective(updateRoom);
				}
				return;
			}
			updateRegion(arg0, "");
		}else{
			if(oldRegion.getRegionName().equals(arg0.getRegionName())){
				saasRegionMapper.updateByPrimaryKeySelective(arg0);
				//更新楼栋下房间的regionNumber
				Room roomParam=new Room();
				roomParam.setRegionId(arg0.getId());
				List<Room> roomResult=roomMapper.selectByEntity(roomParam);
				for(Room room:roomResult){
					Room updateRoom=new Room();
					updateRoom.setRoomId(room.getRoomId());
					updateRoom.setRegionNumber(arg0.getRegionNumber());
					roomMapper.updateByPrimaryKeySelective(updateRoom);
				}
				return;
			}
			updateRegion(arg0, pRegion.getRegionFullName());
		}	

	}
	
	
	/***
	 * 递归更新楼栋信息
	 * @param saasRegion 需要更新的楼栋
	 * @param pRegionFUllName 楼栋父节点的全名,无父节点传空字符串
	 */
	private void updateRegion(SaasRegion saasRegion,String pRegionFUllName){
		if(pRegionFUllName==""){
			saasRegion.setRegionFullName(saasRegion.getRegionName());
		}else{
			saasRegion.setRegionFullName(pRegionFUllName+"-"+saasRegion.getRegionName());
		}
		saasRegionMapper.updateByPrimaryKeySelective(saasRegion);
		//判断是否含有子楼栋
		SaasRegion regionParam=new SaasRegion();
		regionParam.setOrgId(saasRegion.getOrgId());
		regionParam.setPRegionNo(saasRegion.getRegionNo());
		List<SaasRegion> resultList=saasRegionMapper.selectByEntity(regionParam);
		if(resultList.size()>0){
			for(SaasRegion childRegion:resultList){
				updateRegion(childRegion,saasRegion.getRegionFullName());
			}
		}else{
			Room roomParam=new Room();
			roomParam.setRegionId(saasRegion.getId());
			List<Room> roomResult=roomMapper.selectByEntity(roomParam);
			for(Room room:roomResult){
				Room updateRoom=new Room();
				updateRoom.setRoomId(room.getRoomId());
				updateRoom.setRegionFullName(saasRegion.getRegionFullName());
				updateRoom.setRegionNumber(saasRegion.getRegionNumber());
				roomMapper.updateByPrimaryKeySelective(updateRoom);
			}
		}
		return;	
	}
	
	
	
	@Override
	public List<SaasRegion> selectSaasRegion(SaasRegion saasRegion) {
		return saasRegionMapper.selectSaasRegion(saasRegion);
	}

	@Override
	public List<SaasRegion> selectByEntity(SaasRegion param) {
		return saasRegionMapper.selectByEntity(param);
	}

	@Override
	public String insertRegion(RegionExcel regionTemp, OrgUser user) {
		// 当前最大编号
		String maxNo = "";
		// 新编号
		String regionNo = "";
		// 编号头部
		String head = "";
		// 编号尾部
		String tail = "";
		List<SaasRegion> saasRegions = new ArrayList<SaasRegion>();
		//上级楼栋
		SaasRegion pRegion = new SaasRegion();
		/**
		 * 判断如果层级不为1和上级编号不为空 则去查上级楼栋  
		 */
		if("1".equals(regionTemp.getTier()) && StringUtils.isEmpty(regionTemp.getpRegionNumber())){
			pRegion.setRegionNo("1");
			pRegion.setOrgId(user.getOrgId());
		}else{
			SaasRegion saasRegion = new SaasRegion();
			saasRegion.setOrgId(user.getOrgId());
			saasRegion.setRegionNumber(regionTemp.getpRegionNumber());
			saasRegion.setTier((byte)(Integer.parseInt(regionTemp.getTier()) - 1));
			saasRegions = saasRegionMapper.selectByEntity(saasRegion);
			// 上级编号不存在
			if(saasRegions == null|| saasRegions.size() == 0){
				return "上级编号不存在或者楼栋层级错误";
			}
			 pRegion = saasRegions.get(0);
		}
		
		// 新增节点
		SaasRegion region = new SaasRegion();
		
		region.setRegionNumber(regionTemp.getRegionNumber());
		region.setRegionName(regionTemp.getRegionName());
		int currLength = pRegion.getRegionNo().length();
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("regionNo", pRegion.getRegionNo());
		queryMap.put("stepLen", STEP_LEN);
		queryMap.put("orgId", pRegion.getOrgId());
		// 查询下层级最大编号
		maxNo = saasRegionMapper.selectMaxregionNo(queryMap);
		// 无下层
		if (StringUtils.isEmpty(maxNo)) {
			regionNo = pRegion.getRegionNo() + DEFAULT_CODE;
		} else {
			tail = maxNo.substring(currLength - 1);
			head = pRegion.getRegionNo().substring(0, currLength-1);
			regionNo = head + (Integer.valueOf(tail) + 1);
		}
		// 无上级楼栋
		if ("1".equals(pRegion.getRegionNo())) {
			region.setTier((byte) 1); // 一级目录
			region.setIsTop((byte) 1);
			/*Org org = orgMapper.selectByPrimaryKey(pRegion.getOrgId());
			region.setRegionFullName(org.getOrgName());*/
			region.setRegionFullName(pRegion.getRegionName());
		} else {
			pRegion = saasRegionMapper.selectByPrimaryKey(pRegion.getId());
			pRegion.setIsLeaf((byte) 0);
			// 不可删除
			pRegion.setIsDelete((byte) 0);
			saasRegionMapper.updateByPrimaryKeySelective(pRegion);
			if (!StringUtils.isEmpty(pRegion.getRegionFullName())) {
				region.setRegionFullName(pRegion.getRegionFullName() + "-" + region.getRegionName());
			} else {
				region.setRegionFullName(region.getRegionName());
			}
			region.setTier((byte) (Integer.parseInt(regionTemp.getTier()) + 0));
			region.setIsTop((byte) 0);
		}
		region.setOrgId(pRegion.getOrgId());
		region.setRegionNo(regionNo);
		region.setPRegionNo(pRegion.getRegionNo());
		region.setIsLeaf((byte) 1);
		// 可以删除
		region.setIsDelete((byte) 1);
		
		int result = saasRegionMapper.insert(region);
		// 插入成功
		if(result==1){
			return "ok";
		}
		// 插入失败
		else {
			return "未知异常";
		}
	}

}
