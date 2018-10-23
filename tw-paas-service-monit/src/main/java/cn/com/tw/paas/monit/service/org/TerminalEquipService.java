package cn.com.tw.paas.monit.service.org;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.excel.TerminalEquipDtuExcel;
import cn.com.tw.paas.monit.entity.business.excel.TerminalEquipExcel;
import cn.com.tw.paas.monit.entity.business.org.TerminalEquipExpand;
import cn.com.tw.paas.monit.entity.business.replace.TermReplace;
import cn.com.tw.paas.monit.entity.db.org.OrgApplication;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquipParamDtu;

public interface TerminalEquipService{

	/**
	 * 复合条件查询
	 * @param terminalEquip
	 * @return
	 */
	List<TerminalEquipExpand> selectByBean(TerminalEquip terminalEquip);

	/**
	 * 查询各类设备总数量
	 * @return
	 */
	List<TerminalEquipExpand> selectCountNum();

	int deleteById(String arg0);

	int insert(TerminalEquip arg0);

	int insertSelect(TerminalEquip terminalEquip);

	TerminalEquipExpand selectById(String equipId);

	List<TerminalEquip> selectByPage(Page page);

	int update(TerminalEquip arg0);

	int updateSelect(TerminalEquip terminalEquip);
	
	List<TerminalEquip> selectByAppkey(Map<String,String> param);
	
	List<TerminalEquip> selectByAppIdAndMeterAddr(Map<String,String> param);
	
	List<TerminalEquip> selectByOrgIdAndMeterAddr(Map<String,String> param);
	
	List<TerminalEquip> selectTerminalForApi(TerminalEquip param);

	TerminalEquip selectByEquipNumber(String equip);
	
	TerminalEquipExpand selectSaasTerminalInfo(String equipNumber);

	void inserTerminalEquipExcel(TerminalEquipExcel terminalEquipExcel);
	
	int saveEquipForQRcode(Map<String,String> param, OrgApplication application);
	
	List<TerminalEquip> selectLikeEquipNumber(Map<String, Object> map);
	
	List<TerminalEquipExpand> selectByAppId(String appId, String equipTypeCode);
	
	List<TerminalEquipExpand> selectByOrgId(String orgId, String equipTypeCode);

	int updateByPrimaryKeySelective(TerminalEquip terminalEquip);

	void replaceTerm(TermReplace equip);

	List<TerminalEquip> selectByNetEquipNumberAndSectors(String netEquipNumber);

	List<TerminalEquip> selectMeterByPlant(Page page);

	int updatePlantToOtherOrg(TerminalEquip terminalEquip);

	TerminalEquipExpand selectSaasTerminalInfo(TerminalEquip te);

	int selectMeterTotalNum(String orgId);

	List<TerminalEquipExpand> selectNetTypeNumber();
	
	List<TerminalEquip> selectEquipList(TerminalEquip terminalEquip);

	Map<String, Object> selectInfoByEquipNumber(String equipNumber);

	int insertForDaas(TerminalEquip terminalEquip);

	List<TerminalEquip> selectDaasByPage(Page page);

	int deleteDaasById(String equipId);

	TerminalEquip selectDaasByEquipNumber(String equipNumber);

	TerminalEquip selectDaasByPrimaryKey(String equipId);

	int updateDtu(TerminalEquipParamDtu tepd);

	List<TerminalEquip> selectDtuAndChildrenByPage(Page page);

	int insertNetEquipExcel(TerminalEquipDtuExcel terminalEquipDtuExcel) throws ParseException;
}
