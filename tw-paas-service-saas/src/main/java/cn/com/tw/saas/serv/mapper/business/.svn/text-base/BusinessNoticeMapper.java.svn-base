package cn.com.tw.saas.serv.mapper.business;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.business.BusinessNotice;

public interface BusinessNoticeMapper {
    int deleteByPrimaryKey(String id);

    int insert(BusinessNotice record);

    int insertSelective(BusinessNotice record);

    BusinessNotice selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BusinessNotice record);

    int updateByPrimaryKey(BusinessNotice record);

	List<BusinessNotice> selectByPage(Page page);
}