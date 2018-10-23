package cn.com.tw.common.web.core;

import java.util.List;

import cn.com.tw.common.web.entity.page.Page;

public interface IBaseSerivce<T> {
	/**
	 * 创建
	 * 
	 * @param t
	 */
	int insert(T t);
	
	/**
	 * 创建
	 * 
	 * @param t
	 */
	int insertSelect(T t);

	/**
	 * 删除
	 * 
	 * @param uuid
	 */
	int deleteById(String uuid);

	/**
	 * 主键查询
	 * 
	 * @param uuid
	 * @return
	 */
	T selectById(String uuid);

	/**
	 * 查询
	 * 
	 * @param t
	 * @return
	 */
	/* T query(T t); */

	/**
	 * 选择更新
	 * 
	 * @param t
	 *            t
	 */
	int updateSelect(T t);
	
	/**
	 * 选择更新
	 * 
	 * @param t
	 *            t
	 */
	int update(T t);

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @return
	 */
	List<T> selectByPage(Page page);
}
