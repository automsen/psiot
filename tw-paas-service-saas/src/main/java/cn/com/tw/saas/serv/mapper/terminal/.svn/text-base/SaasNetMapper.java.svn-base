package cn.com.tw.saas.serv.mapper.terminal;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.terminal.SaasNet;

public interface SaasNetMapper {
    int deleteByPrimaryKey(String netId);

    int insert(SaasNet record);

    int insertSelective(SaasNet record);

    SaasNet selectByPrimaryKey(String netId);

    int updateByPrimaryKeySelective(SaasNet record);

    int updateByPrimaryKey(SaasNet record);
    
    List<SaasNet> selectByPage(Page page);
    
    SaasNet selectByNumber(String netNumber);

	List<SaasNet> selectSaasNet(SaasNet saasNet);

	List<SaasNet> selectByRegionId(String regionId);
}