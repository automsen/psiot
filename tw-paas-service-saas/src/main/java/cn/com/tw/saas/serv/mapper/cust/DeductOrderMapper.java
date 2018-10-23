/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.mapper.cust;

import java.util.List;

import cn.com.tw.saas.serv.entity.business.cust.DeductOrderParams;
import cn.com.tw.saas.serv.entity.db.cust.DeductOrder;

public interface DeductOrderMapper {
    int deleteByPrimaryKey(String orderId);

    int insert(DeductOrder record);

    DeductOrder selectByPrimaryKey(String orderId);

    int updateByPrimaryKeySelective(DeductOrder record);
    
    DeductOrderParams selectOneParams(DeductOrder param);

	List<DeductOrder> selectByEntity1(DeductOrder deductOrder1);
}