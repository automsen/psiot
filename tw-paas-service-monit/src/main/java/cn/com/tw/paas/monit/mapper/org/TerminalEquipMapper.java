package cn.com.tw.paas.monit.mapper.org;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.org.TerminalEquipExpand;
import cn.com.tw.paas.monit.entity.db.org.TerminalEquip;

public interface TerminalEquipMapper {
    int deleteByPrimaryKey(String equipId);

    int insert(TerminalEquip record);

    int insertSelective(TerminalEquip record);

    TerminalEquipExpand selectByPrimaryKey(String equipId);

    int updateByPrimaryKeySelective(TerminalEquip record);

    int updateByPrimaryKey(TerminalEquip record);

	List<TerminalEquip> selectByPage(Page page);

	List<TerminalEquipExpand> selectByBean(TerminalEquip terminalEquip);

	TerminalEquip selectByEquipNumber(@Param("equipNumber") String equipNumber);

	List<TerminalEquipExpand> selectCountNum();
	
	List<TerminalEquip> selectByAppkey(Map<String,String> param);
	
	List<TerminalEquip> selectByAppIdAndMeterAddr(Map<String,String> param);

	List<TerminalEquip> selectByOrgIdAndMeterAddr(Map<String,String> param);
	
	TerminalEquip selectByNetEquipNumber(@Param("netEquipNumber") String NetEquipNumber);

	List<TerminalEquip> selectTerminalForApi(TerminalEquip param);

	List<TerminalEquip> selectTerminalAll(TerminalEquip terminalEquip);
	
	TerminalEquipExpand selectSaasTerminalInfo(String equipNumber);
	
	List<TerminalEquip> selectLikeEquipNumber(Map<String, Object> map);

	List<TerminalEquip> selectByNetEquipNumberAndSectors(String netEquipNumber);

	int updateByEquipNumber(TerminalEquip terminalEquip);

	List<TerminalEquip> selectMeterByPlant(Page page);

	TerminalEquipExpand selectSaasTerminalInfoByEntity(TerminalEquip te);

	int selectMeterTotalNum(String orgId);

	List<TerminalEquipExpand> selectNetTypeNumber();
	
	List<TerminalEquip> selectEquipList(TerminalEquip terminalEquip);
	
	Map<String, Object> selectInfoByEquipNumber(String equipNumber);

	List<TerminalEquip> selectDaasByPage(Page page);

	TerminalEquip selectDaasByEquipNumber(String equipNumber);

	TerminalEquip selectDtuByPrimaryKey(String equipId);

	List<TerminalEquip> selectDtuAndChildrenByPage(Page page);
}