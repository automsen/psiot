package cn.com.tw.saas.serv.mapper.rule;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.rule.EnergyPrice;

public interface EnergyPriceMapper {
    int deleteByPrimaryKey(String priceId);

    int insert(EnergyPrice record);

    EnergyPrice selectByPrimaryKey(String priceId);

    int updateByPrimaryKeySelective(EnergyPrice record);

	List<EnergyPrice> selectByPage(Page page);

	List<EnergyPrice> selectByBean(EnergyPrice ladderPrice);

	EnergyPrice selectByPriceName(EnergyPrice arg0);

	EnergyPrice selectIsDefaultPrice(String orgId, String energyType);
}