package cn.com.tw.saas.serv.mapper.org;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.org.OrgAccountRecord;

public interface OrgAccountRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(OrgAccountRecord record);

    int insertSelective(OrgAccountRecord record);

    OrgAccountRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrgAccountRecord record);

    int updateByPrimaryKey(OrgAccountRecord record);

	List<OrgAccountRecord> selectByPage(Page page);

	List<OrgAccountRecord> orgAccountRecordExpert(OrgAccountRecord orgAccountRecord);
}