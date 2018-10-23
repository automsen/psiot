package cn.com.tw.paas.monit.mapper.baseEquipModel;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.paas.monit.entity.db.baseEquipModel.BaseEquipModel;

public interface BaseEquipModelMapper {
    int deleteByPrimaryKey(String modelId);

    int insert(BaseEquipModel record);

    int insertSelective(BaseEquipModel record);

    BaseEquipModel selectByPrimaryKey(String modelId);

    int updateByPrimaryKeySelective(BaseEquipModel record);

    int updateByPrimaryKey(BaseEquipModel record);

	List<BaseEquipModel> selectBaseEquipModelPage(Page page);

	List<BaseEquipModel> selectBaseEquipModelAll(BaseEquipModel baseEquipModel);

	BaseEquipModel selectByEquipNumber(@Param("equipNumber") String equipNumber);
	
	BaseEquipModel selectByModelName(String modelName);
}