package cn.com.tw.common.web.core;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;

public interface IBaseMapper<T> {
	/**
	 * 删除
	 * @param id
	 * @return 删除成功个数
	 */
    int deleteByPrimaryKey(String id);

    /**
	 * 插入
	 * @param t 用户需要插入的信息
	 * @return 插入成功个数
	 */
    int insert(T t);
    
    /**
   	 * 插入
   	 * @param t 用户需要插入的信息
   	 * @return 插入成功个数
   	 */
    int insertSelective(T t);

    /**
     * 主键查询
     * @param id id
     * @return 用户信息
     */
    T selectByPrimaryKey(String id);

    /**
     * 更新用户信息
     * @param t 用户需要修改的信息
     * @return 更新成功个数
     */
    int updateByPrimaryKeySelective(T t);
    
    /**
     * 更新用户信息
     * @param t 用户需要修改的信息
     * @return 更新成功个数
     */
    int updateByPrimaryKey(T t);
    
    /**
     * 分页查询
     * @param page
     * @return
     */
    List<T> selectByPage(Page page);
}
