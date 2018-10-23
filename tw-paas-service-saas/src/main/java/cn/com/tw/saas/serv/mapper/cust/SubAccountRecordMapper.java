/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.mapper.cust;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;
import cn.com.tw.saas.serv.entity.db.cust.SubAccountRecord;

public interface SubAccountRecordMapper {
    int deleteByPrimaryKey(String id);

    int insert(SubAccountRecord record);

    int insertSelective(SubAccountRecord record);

    SubAccountRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SubAccountRecord record);

    int updateByPrimaryKey(SubAccountRecord record);

	List<SubAccountRecord> selectByPage(Page page);
}