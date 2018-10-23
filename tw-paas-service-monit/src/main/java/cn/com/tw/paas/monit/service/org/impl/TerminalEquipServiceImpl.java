package cn.com.tw.paas.monit.service.org.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.com.tw.common.exception.BusinessException;
import cn.com.tw.common.exception.RequestParamValidException;
import cn.com.tw.common.utils.CommUtils;
import cn.com.tw.common.utils.cons.ResultCode;
import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.common.utils.cons.code.MonitResultCode;
import cn.com.tw.paas.monit.common.utils.cons.code.TermCode;
import cn.com.tw.paas.monit.entity.business.excel.TerminalEquipDtuExcel;
import cn.com.tw.paas.monit.entity.business.excel.TerminalEquipExcel;
import cn.com.tw.paas.monit.entity.business.org.TerminalEquipExpand;
import cn.com.tw.paas.monit.entity.business.replace.TermReplace;
import cn.com.tw.paas.monit.entity.db.baseEquipModel.BaseEquipModel;
import cn.com.tw.paas.monit.entity.db.org.EquipGroup;
import cn.com.tw.paas.monit.entity.db.org.EquipNetStatus;
import cn.com.tw.paas.monit.entity.db.org.OrgApplication;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquipChildren;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquipParamDtu;
import cn.com.tw.paas.monit.mapper.baseEquipModel.BaseEquipModelMapper;
import cn.com.tw.paas.monit.mapper.org.EquipGroupMapper;
import cn.com.tw.paas.monit.mapper.org.EquipNetStatusMapper;
import cn.com.tw.paas.monit.mapper.org.OrgResidentMapper;
import cn.com.tw.paas.monit.mapper.org.TerminalEquipChildrenMapper;
import cn.com.tw.paas.monit.mapper.org.TerminalEquipMapper;
import cn.com.tw.paas.monit.mapper.org.TerminalEquipParamDtuMapper;
import cn.com.tw.paas.monit.service.org.TerminalEquipService;

@Service
public class TerminalEquipServiceImpl implements TerminalEquipService {
	
	public static final String TERM_CACHE_KEY = "term";

	@Autowired
	private TerminalEquipMapper terminalEquipMapper;
	@Autowired
	private EquipGroupMapper equipGroupMapper;
	@Autowired
	private EquipNetStatusMapper equipNetStatusMapper;
	@Autowired
	private BaseEquipModelMapper baseEquipModelMapper;
	@Autowired
	private TerminalEquipChildrenMapper terminalEquipChildrenMapper;
	@Autowired
	private TerminalEquipParamDtuMapper terminalEquipParamDtuMapper;
	@Autowired
	private OrgResidentMapper orgResidentMapper;

	@Override
	@CacheEvict(value = TERM_CACHE_KEY, allEntries=true)
	@Transactional
	public int deleteById(String equipId) {
		terminalEquipMapper.deleteByPrimaryKey(equipId);
		
		TerminalEquipExpand term = terminalEquipMapper.selectByPrimaryKey(equipId);
		if (term != null) {
			String equipNum = term.getEquipNumber();
			equipNetStatusMapper.deleteByEquipAddr(equipNum);
		}
		return 0;
	}

	@Override
	public int insert(TerminalEquip arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@CacheEvict(value = TERM_CACHE_KEY, allEntries=true)
	@Transactional
	public int insertSelect(TerminalEquip terminalEquip) {
		/**
		 * 终端设备写入
		 */
		terminalEquip.setEquipId(CommUtils.getUuid());
		terminalEquip.setCreateTime(new Date(System.currentTimeMillis()));
		/**
		 * 通讯地址唯一校验
		 */
		BaseEquipModel baseEquipModel = baseEquipModelMapper.selectByPrimaryKey(terminalEquip.getModelId());
		if (StringUtils.isEmpty(baseEquipModel)) {
			throw new BusinessException(MonitResultCode.PARAMETER_NULL_ERROR, "型号不存在！");
		}
		/**
		 * 电表
		 */
		if ("110000".equals(baseEquipModel.getEquipType())) {
			terminalEquip.setElecMeterTypeCode(baseEquipModel.getElecMeterTypeCode());
		}
		/**
		 * 是否直连
		 */
		terminalEquip.setIsDirect(baseEquipModel.getIsDirect());
		if (baseEquipModel.getIsDirect() == 1) {
			terminalEquip.setNetTypeCode(baseEquipModel.getNetType());
		}

		TerminalEquip terminalEquip1 = terminalEquipMapper.selectByEquipNumber(terminalEquip.getEquipNumber());
		if (!StringUtils.isEmpty(terminalEquip1)) {
			throw new BusinessException(MonitResultCode.PARAM_EXIST_ERROR, "设备编号重复！");
		}
		
		
		if (StringUtils.isEmpty(terminalEquip.getEquipName())) {
			terminalEquip.setEquipName(terminalEquip.getEquipNumber());
		}
		
		terminalEquipMapper.insertSelective(terminalEquip);

		/*EquipGroup equipGroup = new EquipGroup();
		equipGroup.setId(CommUtils.getUuid());
		equipGroup.setOrgId(terminalEquip.getOrgId());
		equipGroup.setCreateTime(new Date(System.currentTimeMillis()));
		equipGroup.setChildStatus((byte) 0);
		equipGroup.setAppId(terminalEquip.getAppId());
		*//**
		 * 终端设备为非直连
		 *//*
		if (terminalEquip.getIsDirect() == 0) {
			try {
				equipGroup.setCommAddr(terminalEquip.getNetEquipNumber());
				equipGroup.setChildCommAddr(terminalEquip.getEquipNumber());
				equipGroupMapper.insertSelective(equipGroup);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {*/
		if(terminalEquip.getIsDirect() == 1){
			try {
				EquipNetStatus equipNetStatus = new EquipNetStatus();
				equipNetStatus.setId(CommUtils.getUuid());
				equipNetStatus.setCommAddr(terminalEquip.getEquipNumber());
				equipNetStatus.setNetStatus((byte) 2);
				equipNetStatusMapper.insertSelective(equipNetStatus);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}

	@Override
	public TerminalEquipExpand selectById(String equipId) {
		return terminalEquipMapper.selectByPrimaryKey(equipId);
	}

	@Override
	public List<TerminalEquip> selectByPage(Page page) {
		List<TerminalEquip> terminalEquips = terminalEquipMapper.selectByPage(page);
		return terminalEquips;
	}

	@Override
	public int update(TerminalEquip arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	@CacheEvict(value = TERM_CACHE_KEY,allEntries=true)
	public int updateSelect(TerminalEquip terminalEquip) {
		/**
		 * 通讯地址唯一校验
		 */
		TerminalEquip terminalEquip1 = terminalEquipMapper.selectByPrimaryKey(terminalEquip.getEquipId());
		if (!terminalEquip.getEquipNumber().equals(terminalEquip1.getEquipNumber())) {
			TerminalEquip terminalEquip2 = terminalEquipMapper.selectByEquipNumber(terminalEquip.getEquipNumber());
			if (!StringUtils.isEmpty(terminalEquip2)) {
				throw new BusinessException(MonitResultCode.PARAM_EXIST_ERROR, "设备编号重复！");
			}
		}

		BaseEquipModel baseEquipModel = baseEquipModelMapper.selectByPrimaryKey(terminalEquip.getModelId());
		if (StringUtils.isEmpty(baseEquipModel)) {
			throw new BusinessException(MonitResultCode.PARAMETER_NULL_ERROR, "型号不存在！");
		}
		/**
		 * 电表
		 */
		if ("110000".equals(baseEquipModel.getEquipType())) {
			terminalEquip.setElecMeterTypeCode(baseEquipModel.getElecMeterTypeCode());
		}
		/**
		 * 是否直连
		 */
		terminalEquip.setIsDirect(baseEquipModel.getIsDirect());
		if (baseEquipModel.getIsDirect() == 1) {
			terminalEquip.setNetTypeCode(baseEquipModel.getNetType());
		}

		terminalEquipMapper.updateByPrimaryKeySelective(terminalEquip);
		return 0;
	}

	@Override
	//@Cacheable(value = TERM_CACHE_KEY, key = "'key_term_'+#terminalEquip.getOrgId()+#terminalEquip.getEquipTypeCode()")
	public List<TerminalEquipExpand> selectByBean(TerminalEquip terminalEquip) {
		List<TerminalEquipExpand> terminalEquips = terminalEquipMapper.selectByBean(terminalEquip);
		return terminalEquips;
	}
	
	@Override
	@Cacheable(value = TERM_CACHE_KEY, key = "'key_term_orgId_'+#orgId+'_'+#equipTypeCode")
	public List<TerminalEquipExpand> selectByOrgId(String orgId, String equipTypeCode) {
		TerminalEquip terminalEquip = new TerminalEquip();
		terminalEquip.setOrgId(orgId);
		terminalEquip.setEquipTypeCode(equipTypeCode);
		List<TerminalEquipExpand> terminalEquips = terminalEquipMapper.selectByBean(terminalEquip);
		return terminalEquips;
	}
	
	@Override
	@Cacheable(value = TERM_CACHE_KEY, key = "'key_term_appId_'+#appId+'_'+#equipTypeCode")
	public List<TerminalEquipExpand> selectByAppId(String appId, String equipTypeCode) {
		TerminalEquip terminalEquip = new TerminalEquip();
		terminalEquip.setAppId(appId);
		terminalEquip.setEquipTypeCode(equipTypeCode);
		List<TerminalEquipExpand> terminalEquips = terminalEquipMapper.selectByBean(terminalEquip);
		return terminalEquips;
	}
	
	@Override
	public List<TerminalEquipExpand> selectCountNum() {
		List<TerminalEquipExpand> terminalEquips = terminalEquipMapper.selectCountNum();
		return terminalEquips;
	}

	@Override
	public List<TerminalEquip> selectByAppkey(Map<String, String> param) {
		return terminalEquipMapper.selectByAppkey(param);
	}

	@Override
	public List<TerminalEquip> selectTerminalForApi(TerminalEquip param) {
		return terminalEquipMapper.selectTerminalForApi(param);
	}

	@Override
	@Cacheable(value = TERM_CACHE_KEY, key = "'key_termNum_'+#equip")
	public TerminalEquip selectByEquipNumber(String equip) {
		return terminalEquipMapper.selectByEquipNumber(equip);
	}

	@Transactional
	@CacheEvict(value = TERM_CACHE_KEY, allEntries=true)
	public void inserTerminalEquipExcel(TerminalEquipExcel terminalEquipExcel) {
		TerminalEquip terminalEquip = new TerminalEquip();
		terminalEquip.setEquipId(CommUtils.getUuid());
		terminalEquip.setEquipNumber(terminalEquipExcel.getEquipNumber());
		terminalEquip.setCreateTime(new Date(System.currentTimeMillis()));
		terminalEquip.setEquipName(terminalEquipExcel.getEquipName());
		terminalEquip.setOrgId(terminalEquipExcel.getOrgId());
		terminalEquip.setAppId(terminalEquipExcel.getAppId());
		terminalEquip.setEquipCategCode(terminalEquipExcel.getEquipCategCode());
		terminalEquip.setEquipTypeCode(terminalEquipExcel.getEquipTypeCode());
		terminalEquip.setSoftVersoin(terminalEquipExcel.getSoftVersoin());
		terminalEquip.setCommPwd(terminalEquipExcel.getCommPwd());
		terminalEquip.setIsDirect(new Byte(terminalEquipExcel.getIsDirect()));
		// 非直连
		if (terminalEquipExcel.getIsDirect().equals("0")){
			terminalEquip.setLinkTypeCode(terminalEquipExcel.getLinkTypeCode());
			terminalEquip.setNetEquipNumber(terminalEquipExcel.getNetEquipNumber());
			if(terminalEquipExcel.getNetEquipNumber() != null && !terminalEquipExcel.getNetEquipNumber().equals("")){
				terminalEquip.setSectors(Integer.parseInt(terminalEquipExcel.getSectors()));
			}
		}else{
			terminalEquip.setSectors(0);
		}
		terminalEquip.setNetNumber(terminalEquipExcel.getNetNumber());
		terminalEquip.setNetTypeCode(terminalEquipExcel.getNetTypeCode());

		terminalEquip
				.setModelId(baseEquipModelMapper.selectByModelName(terminalEquipExcel.getModelName()).getModelId());
		terminalEquip.setIsUsable(new Byte("1"));
		EquipGroup equipGroup = new EquipGroup();
		equipGroup.setId(CommUtils.getUuid());
		equipGroup.setOrgId(terminalEquip.getOrgId());
		equipGroup.setCreateTime(new Date(System.currentTimeMillis()));
		equipGroup.setChildStatus((byte) 0);
		if (terminalEquip.getIsDirect() == 0) {
			equipGroup.setCommAddr(terminalEquip.getNetEquipNumber());
			equipGroup.setChildCommAddr(terminalEquip.getEquipNumber());
			equipGroup.setSectors(terminalEquip.getSectors());
			equipGroupMapper.insertSelective(equipGroup);
		} else {
			EquipNetStatus equipNetStatus = new EquipNetStatus();
			equipNetStatus.setId(CommUtils.getUuid());
			equipNetStatus.setCommAddr(terminalEquip.getEquipNumber());
			equipNetStatus.setNetStatus((byte) 2);
			equipNetStatusMapper.insertSelective(equipNetStatus);
		}
		if (terminalEquipExcel.getEquipTypeCode().equals("110000")) {
			terminalEquip.setElecMeterTypeCode(terminalEquipExcel.getElecMeterTypeCode());
			terminalEquip.setElecVoltageRating(new BigDecimal(terminalEquipExcel.getElecVoltageRating()));
			terminalEquip.setElecCurrentRating(new BigDecimal(terminalEquipExcel.getElecCurrentRating()));
			terminalEquip.setElecPowerRating(new BigDecimal(terminalEquipExcel.getElecPowerRating()));
			terminalEquip.setElecCurrentMax(new BigDecimal(terminalEquipExcel.getElecCurrentMax()));
			terminalEquip.setElecPowerMax(new BigDecimal(terminalEquipExcel.getElecPowerMax()));
			terminalEquip.setElecPt(new BigDecimal(terminalEquipExcel.getElecPt()));
			terminalEquip.setElecCt(new BigDecimal(terminalEquipExcel.getElecCt()));
		}
		terminalEquipMapper.insertSelective(terminalEquip);

	}

	/**
	 * 二维码扫描录入设备档案
	 */
	@Override
	@CacheEvict(value = TERM_CACHE_KEY,allEntries=true)
	public int saveEquipForQRcode(Map<String, String> param, OrgApplication application) {
		String modelName = param.get("XH");
		modelName = modelName.replaceAll("_", "型");
		String equipNumber = param.get("No");
		String protocol = param.get("Protocol");

		String netNumber = param.get("netNumber");
		BaseEquipModel model = baseEquipModelMapper.selectByModelName(modelName);
		if (null == model) {
			return 0;
		}
		TerminalEquip terminalEquip = new TerminalEquip();
		terminalEquip.setEquipId(CommUtils.getUuid());
		terminalEquip.setEquipNumber(equipNumber);
		terminalEquip.setOrgId(application.getOrgId());
		terminalEquip.setAppId(application.getAppId());
		terminalEquip.setEquipName(equipNumber);
		terminalEquip.setModelId(model.getModelId());
		terminalEquip.setEquipCategCode(model.getEquipType());
		terminalEquip.setEquipTypeCode(model.getEquipType());
		terminalEquip.setSoftVersoin(protocol);
		terminalEquip.setCommPwd("00000002");
		terminalEquip.setIsDirect(model.getIsDirect());
		if (terminalEquip.getIsDirect() == 0) {
			// do nothing
		} else {
			if (!StringUtils.isEmpty(netNumber)) {
				terminalEquip.setNetNumber(netNumber);
			}
			terminalEquip.setNetTypeCode(model.getNetType());
		}
		if (model.getEquipType().equals("110000")) {
			terminalEquip.setElecMeterTypeCode(model.getElecMeterTypeCode());
			String vol = param.get("Vol");
			String voltageRating = "";
			if (!StringUtils.isEmpty(vol)) {
				voltageRating = vol.substring(0, vol.lastIndexOf("V"));
				terminalEquip.setElecVoltageRating(new BigDecimal(voltageRating));
			}
			String cur = param.get("Cur");
			String currentRating = "";
			String currentMax = "";
			if (!StringUtils.isEmpty(cur)) {
				currentRating = cur.substring(0, cur.indexOf("("));
				currentMax = cur.substring(cur.indexOf("(")+1, cur.indexOf(")"));
				terminalEquip.setElecCurrentRating(new BigDecimal(currentRating));
				terminalEquip.setElecCurrentMax(new BigDecimal(currentMax));
			}
			if (!StringUtils.isEmpty(vol)&&!StringUtils.isEmpty(cur)){
				terminalEquip.setElecPowerRating(new BigDecimal(currentRating).multiply(new BigDecimal(voltageRating)).divide(new BigDecimal(1000)));
				terminalEquip.setElecPowerMax(new BigDecimal(currentMax).multiply(new BigDecimal(voltageRating)).divide(new BigDecimal(1000)));
			}
			
		}
		terminalEquip.setCreateTime(new Date(System.currentTimeMillis()));
		terminalEquip.setIsUsable(new Byte("1"));
		return terminalEquipMapper.insertSelective(terminalEquip);
	}

	@Override
	public TerminalEquipExpand selectSaasTerminalInfo(String equipNumber) {
		// TODO Auto-generated method stub
		return terminalEquipMapper.selectSaasTerminalInfo(equipNumber);
	}
	
	@Override
	public TerminalEquipExpand selectSaasTerminalInfo(TerminalEquip te) {
		// TODO Auto-generated method stub
		return terminalEquipMapper.selectSaasTerminalInfoByEntity(te);
	}

	@Override
	public List<TerminalEquip> selectLikeEquipNumber(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return terminalEquipMapper.selectLikeEquipNumber(map);
	}

	@Override
	public List<TerminalEquip> selectByAppIdAndMeterAddr(
			Map<String, String> param) {
		return terminalEquipMapper.selectByAppIdAndMeterAddr(param);
	}

	@Override
	public List<TerminalEquip> selectByOrgIdAndMeterAddr(
			Map<String, String> param) {
		// TODO Auto-generated method stub
		return terminalEquipMapper.selectByOrgIdAndMeterAddr(param);
	}

	@Override
	public int updateByPrimaryKeySelective(TerminalEquip terminalEquip) {
		// TODO Auto-generated method stub
		return terminalEquipMapper.updateByPrimaryKeySelective(terminalEquip);
	}

	@Override
	@Transactional
	public void replaceTerm(TermReplace equip) {
		String oldEquipNumber = equip.getOldEquipNumber();
		String newEquipNumber = equip.getNewEquipNumber();
		
		if (oldEquipNumber.equals(newEquipNumber)) {
			throw new RequestParamValidException("newEquipNumber and oldEquipNumber can not be the same");
		}
		
		//将老表退换到工厂
		TerminalEquip oldTerm = terminalEquipMapper.selectByEquipNumber(oldEquipNumber);
		
		if (oldTerm == null) {
			throw new RequestParamValidException("oldEquipNumber is no exists!");
		}
		
		//如果老表已经在工厂了,返回错误码
		if ("1008".equals(oldTerm.getOrgId())) {
			throw new BusinessException(TermCode.FACTORY_EXISTS, "oldEquipNumber exists factory!!");
		}
		
		if (!equip.getOrgId().equals(oldTerm.getOrgId())) {
			throw new BusinessException(ResultCode.UNKNOW_ERROR, "oldEquipNumber and orgId no match!!");
		}
		
		TerminalEquip upateToSource = new TerminalEquip();
		upateToSource.setEquipId(oldTerm.getEquipId());
		upateToSource.setAppId(oldTerm.getSourceAppId());
		upateToSource.setOrgId(oldTerm.getSourceOrgId());
		upateToSource.setSourceOrgId(oldTerm.getOrgId());
		upateToSource.setSourceAppId(oldTerm.getAppId());
		upateToSource.setOperationRecordingTime(new Date());
		if (StringUtils.isEmpty(oldTerm.getSourceAppId()) || StringUtils.isEmpty(oldTerm.getSourceOrgId())) {
			//如果为空， 默认将老表退到阿迪克工厂
			upateToSource.setAppId("100801");
			upateToSource.setOrgId("1008");
		}
		terminalEquipMapper.updateByPrimaryKeySelective(upateToSource);
		
		
		//将新表从工厂移到该企业机构下面
		TerminalEquip newTerm = terminalEquipMapper.selectByEquipNumber(newEquipNumber);
		
		if (newTerm == null) {
			throw new RequestParamValidException("newEquipNumber is no exists!");
		}
		
		//如果新表已经存在机构，则返回
		if (!"1008".equals(newTerm.getOrgId())) {
			throw new BusinessException(TermCode.FACTORY_EXISTS, "newEquipNumber exists org!");
		}
		
		TerminalEquip sourceToOrg = new TerminalEquip();
		sourceToOrg.setEquipId(newTerm.getEquipId());
		sourceToOrg.setAppId(equip.getAppId());
		sourceToOrg.setOrgId(equip.getOrgId());
		sourceToOrg.setSourceOrgId(newTerm.getOrgId());
		sourceToOrg.setSourceAppId(newTerm.getAppId());
		sourceToOrg.setOperationRecordingTime(new Date());
		terminalEquipMapper.updateByPrimaryKeySelective(sourceToOrg);
	}

	@Override
	public List<TerminalEquip> selectByNetEquipNumberAndSectors(String netEquipNumber) {
		// TODO Auto-generated method stub
		return terminalEquipMapper.selectByNetEquipNumberAndSectors(netEquipNumber);
	}

	@Override
	public List<TerminalEquip> selectMeterByPlant(Page page) {
		// TODO Auto-generated method stub
		return terminalEquipMapper.selectMeterByPlant(page);
	}

	@Override
	public int updatePlantToOtherOrg(TerminalEquip terminalEquip) {
		// TODO Auto-generated method stub
		try {
			if(terminalEquip == null){
				return 1;
			}
			if(terminalEquip.getIdList() != null && terminalEquip.getIdList().size() > 0){
				List<String> list = terminalEquip.getIdList();
				for (String id : list) {
					TerminalEquip te = new TerminalEquip();
					te.setEquipId(id);
					te.setOrgId(terminalEquip.getOrgId());
					te.setAppId(terminalEquip.getAppId());
					te.setSourceOrgId(terminalEquip.getSourceOrgId());
					te.setSourceAppId(terminalEquip.getSourceAppId());
					terminalEquipMapper.updateByPrimaryKeySelective(te);
				}
				return 0;
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 2;
	}

	@Override
	public int selectMeterTotalNum(String orgId) {
		// TODO Auto-generated method stub
		return terminalEquipMapper.selectMeterTotalNum(orgId);
	}

	@Override
	public List<TerminalEquipExpand> selectNetTypeNumber() {
		return terminalEquipMapper.selectNetTypeNumber();
	}

	@Override
	public List<TerminalEquip> selectEquipList(TerminalEquip terminalEquip) {
		// TODO Auto-generated method stub
		return terminalEquipMapper.selectEquipList(terminalEquip);
	}
	
	@Override
	public Map<String, Object> selectInfoByEquipNumber(String equipNumber){
		return terminalEquipMapper.selectInfoByEquipNumber(equipNumber);
	}

	@Override
	@Transactional
	public int insertForDaas(TerminalEquip terminalEquip) {
		List<TerminalEquipChildren> list = terminalEquip.getDteList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		/**
		 * 终端设备写入
		 */
		String uuid = CommUtils.getUuid();
		terminalEquip.setEquipId(uuid);
		terminalEquip.setCreateTime(new Date(System.currentTimeMillis()));
		try {
			/**
			 * 通讯地址唯一校验
			 */
			BaseEquipModel baseEquipModel = baseEquipModelMapper.selectByPrimaryKey(terminalEquip.getModelId());
			if (StringUtils.isEmpty(baseEquipModel)) {
				throw new BusinessException(MonitResultCode.PARAMETER_NULL_ERROR, "型号不存在！");
			}
			TerminalEquip terminalEquip1 = terminalEquipMapper.selectByEquipNumber(terminalEquip.getEquipNumber());
			if (!StringUtils.isEmpty(terminalEquip1)) {
				throw new BusinessException(MonitResultCode.PARAM_EXIST_ERROR, "设备编号重复！");
			}
			terminalEquip.setGatherHz(""+terminalEquip.getReportingInterval());
			terminalEquipMapper.insertSelective(terminalEquip);
			
			//特有属性插入到属性表
			TerminalEquipParamDtu tepd = new TerminalEquipParamDtu();
			tepd.setEquipNumber(terminalEquip.getEquipNumber());
			tepd.setCoordinate(terminalEquip.getCoordinate());
			tepd.setChildrenNum(terminalEquip.getChildrenNum());
			tepd.setInstallSite(terminalEquip.getInstallSite());
			if(null !=terminalEquip.getInstallTimeStr() && !"".equals(terminalEquip.getInstallTimeStr())){
				tepd.setInstallTime(sdf.parse(terminalEquip.getInstallTimeStr()));
			}
			tepd.setWorkingMode(terminalEquip.getWorkingMode());
			tepd.setFunctionDigit(terminalEquip.getFunctionDigit());
			tepd.setRfBaudRate(terminalEquip.getRfBaudRate());
			tepd.setBeattim(terminalEquip.getBeattim());
			tepd.setReportingInterval(terminalEquip.getReportingInterval());
			tepd.setCreateTime(new Date());
			tepd.setSendStatus((byte) 0);
			terminalEquipParamDtuMapper.insertSelective(tepd);
			int orderNum = 0;
			//写入dtu下联设备
			for (TerminalEquipChildren daasTerminalEquip : list) {
				/*TerminalEquipChildren dte = terminalEquipChildrenMapper.selectByEquipNumber(daasTerminalEquip.getChildrenEquipNumber());
				if(dte != null){//如果设备号重复不允许插入
					//continue;
					throw new BusinessException(MonitResultCode.PARAM_EXIST_ERROR, "热力泵已绑定！");
				}else{*/
				daasTerminalEquip.setEquipId(CommUtils.getUuid());
				daasTerminalEquip.setOrgId(terminalEquip.getOrgId());
				daasTerminalEquip.setAppId(terminalEquip.getAppId());
				daasTerminalEquip.setCreateTime(new Date());
				daasTerminalEquip.setDtuId(uuid);
				daasTerminalEquip.setSoftVersoin(terminalEquip.getDtuSoftVersoin());
				daasTerminalEquip.setProtocolVersoin(terminalEquip.getProtocolVersoin());
				daasTerminalEquip.setOrderNum(orderNum+1);
				terminalEquipChildrenMapper.insertSelective(daasTerminalEquip);
				//}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<TerminalEquip> selectDaasByPage(Page page) {
		List<TerminalEquip> terminalEquips = terminalEquipMapper.selectDaasByPage(page);
		return terminalEquips;
	}

	@Override
	@Transactional
	public int deleteDaasById(String equipId) {
		TerminalEquip terminalEquip = terminalEquipMapper.selectByPrimaryKey(equipId);
		terminalEquipMapper.deleteByPrimaryKey(equipId);
		terminalEquipParamDtuMapper.deleteByPrimaryKey(terminalEquip.getEquipNumber());
		terminalEquipChildrenMapper.deleteByDtuId(equipId);
		return 0;
	}

	@Override
	public TerminalEquip selectDaasByEquipNumber(String equipNumber) {
		// TODO Auto-generated method stub
		//查询主表和属性设备表
		TerminalEquip terminalEquip = terminalEquipMapper.selectDaasByEquipNumber(equipNumber);
		TerminalEquipChildren tec = new TerminalEquipChildren();
		tec.setDtuId(terminalEquip.getEquipId());
		//查询下联设备表
		List<TerminalEquipChildren> tecList = terminalEquipChildrenMapper.selectByEntity(tec);
		terminalEquip.setDteList(tecList);
		
		//查询门牌号及居民信息，如果有居民信息为null，查出初始值，不为null直接返回
		/*if(null == terminalEquip.getfUserName() || "".equals(terminalEquip.getfUserName())){
			OrgResident orgResident = new OrgResident();
			orgResident.setLocationId(terminalEquip.getLocationId());
			List<OrgResident> residentList = orgResidentMapper.selectResidentByEntity(orgResident);
			if(null != residentList && residentList.size() > 0){
				OrgResident res = residentList.get(0);
				terminalEquip.setfUserName(res.getResidentName());
				terminalEquip.setfPhone(res.getResidentPhone());
			}
		}*/
		//查询关联居民表
		/*OrgResident orgResident = orgResidentMapper.selectByPrimaryKey(terminalEquip.getfUserName());
		terminalEquip.setOrgResident(orgResident);*/
		return terminalEquip;
	}

	@Override
	public TerminalEquip selectDaasByPrimaryKey(String equipId) {
		TerminalEquipChildren tec = new TerminalEquipChildren();
		TerminalEquip terminalEquip = terminalEquipMapper.selectDtuByPrimaryKey(equipId);
		tec.setDtuId(equipId);
		List<TerminalEquipChildren> list = terminalEquipChildrenMapper.selectByEntity(tec);
		terminalEquip.setDteList(list);
		return terminalEquip;
	}

	@Override
	@Transactional
	public int updateDtu(TerminalEquipParamDtu tepd) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			TerminalEquip te = new TerminalEquip();
			try {
				if(null != tepd.getInstallTimeStr() && !"".equals(tepd.getInstallTimeStr())){
					tepd.setInstallTime(sdf.parse(tepd.getInstallTimeStr()));
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			te.setEquipId(tepd.getEquipId());
			//userName 存储的字段改为居民主键id关联设备和居民表
			if(null != tepd.getfUserName() && !"".equals(tepd.getfUserName())){
				te.setfUserName(tepd.getfUserName());
			}/*else{
				te.setfUserName("");
			}*/
			if(null != tepd.getfPhone() && !"".equals(tepd.getfPhone())){
				te.setfPhone(tepd.getfPhone());
			}/*else{
				te.setfPhone("");
			}*/
			if(null != tepd.getReportingInterval() && !"".equals(tepd.getReportingInterval())){
				te.setGatherHz(""+tepd.getReportingInterval());
			}else{
				te.setGatherHz("0");
			}
			terminalEquipMapper.updateByPrimaryKeySelective(te);
			
			/*if (null != tepd.getOrgResident()
					&& null != tepd.getOrgResident().getResidentPhone()
					&& !"".equals(tepd.getOrgResident().getResidentPhone())) {
				OrgResident resident = tepd.getOrgResident();
				resident.setResidentId(tepd.getfUserName());
				orgResidentMapper.updateByPrimaryKeySelective(resident);
			}*/

			if(null != tepd.getDteList() && tepd.getDteList().size() > 0){
				tepd.setChildrenNum(tepd.getDteList().size());
				for (TerminalEquipChildren tec : tepd.getDteList()) {
					tec.setEquipId(CommUtils.getUuid());
					tec.setDtuId(te.getEquipId());
					tec.setOrgId(te.getOrgId());
					tec.setEquipType("150000");
					tec.setAppId(te.getAppId());
					tec.setCreateTime(new Date());
					if(null == tec.getSoftVersoin() || "".equals(tec.getSoftVersoin())){
						tec.setSoftVersoin("1");
					}
					if(null == tec.getProtocolVersoin() || "".equals(tec.getProtocolVersoin())){
						tec.setProtocolVersoin("1");
					}
					tec.setOrderNum(1);
					terminalEquipChildrenMapper.deleteByDtuId(te.getEquipId());
					if(tec.getChildrenEquipNumber() != null && tec.getManufacturer() != null && tec.getEquipModel() != null){
						terminalEquipChildrenMapper.insertSelective(tec);
					}
				}
			}
			terminalEquipParamDtuMapper.updateByPrimaryKeySelective(tepd);
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}
		return 0;
	}

	@Override
	public List<TerminalEquip> selectDtuAndChildrenByPage(Page page) {
		List<TerminalEquip> terminalEquips = terminalEquipMapper.selectDtuAndChildrenByPage(page);
		for (TerminalEquip terminalEquip : terminalEquips) {
			TerminalEquipChildren tec = new TerminalEquipChildren();
			tec.setDtuId(terminalEquip.getEquipId());
			terminalEquip.setDteList(terminalEquipChildrenMapper.selectByEntity(tec));
		}
		return terminalEquips;
	}

	@Override
	@Transactional
	public int insertNetEquipExcel (TerminalEquipDtuExcel terminalEquipDtuExcel) throws ParseException{
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TerminalEquip te = new TerminalEquip();
		/**
		 * 终端设备写入
		 */
		String uuid = CommUtils.getUuid();
		te.setEquipId(uuid);
		te.setEquipNumber(terminalEquipDtuExcel.getEquipNumber());
		te.setEquipName(terminalEquipDtuExcel.getEquipName());
		te.setModelId(terminalEquipDtuExcel.getModelId());
		te.setCreateTime(new Date(System.currentTimeMillis()));
		te.setGatherHz("10");
		te.setSoftVersoin(terminalEquipDtuExcel.getSoftVersoin());
		te.setHardwareVersoin(terminalEquipDtuExcel.getHardwareVersoin());
		te.setEquipCategCode("100000");
		te.setEquipTypeCode("150000");
		te.setIsDirect((byte) 1);
		te.setIsUsable((byte) 1);
		te.setIsHistory((byte) 0);
		//主表新增
		terminalEquipMapper.insertSelective(te);
		//附表新增
		TerminalEquipParamDtu tepd = new TerminalEquipParamDtu();
		tepd.setEquipNumber(terminalEquipDtuExcel.getEquipNumber());
		tepd.setEquipName(terminalEquipDtuExcel.getEquipName());
		tepd.setCreateTime(new Date());
		tepd.setBeattim((byte) 10);
		tepd.setChildrenNum(0);
		tepd.setReportingInterval(1800);
		tepd.setFunctionDigit("11");
		tepd.setWorkingMode("11");
		tepd.setRfBaudRate("0");
		tepd.setSendStatus((byte) 0);
		return terminalEquipParamDtuMapper.insertSelective(tepd);
	}
}
