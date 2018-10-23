package cn.com.tw.paas.monit.mapper.org;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.business.org.NetEquipExpand;
import cn.com.tw.paas.monit.entity.db.org.NetEquip;

public interface NetEquipMapper {
    int deleteByPrimaryKey(String equipId);

    int insert(NetEquip record);

    int insertSelective(NetEquip record);

    NetEquip selectByPrimaryKey(String equipId);

    int updateByPrimaryKeySelective(NetEquip record);

    int updateByPrimaryKey(NetEquip record);

	List<NetEquip> selectByPage(Page page);

	List<NetEquipExpand> selectEquipByPage(Page page);

	List<NetEquipExpand> selectNetEquipExpandAll(NetEquipExpand equipExpand);

	NetEquipExpand selectByEquipId(@Param("equipId") String equipId);

	NetEquipExpand selectByEquipNumber(@Param("equipNumber") String equipNumber);

	List<NetEquip> selectByEntity(NetEquip queryOrg);

	List<NetEquip> selectNetForApi(NetEquip param);
	
	List<NetEquip> selectLikeEquipNumber(String equipNumber);
}