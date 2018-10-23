package cn.com.tw.saas.serv.common.utils.cache;

import java.util.List;
import java.util.TreeMap;

import cn.com.tw.saas.serv.entity.db.base.InsDataItem;

public class InsCacheMap {
	/**
	 * 数据项保存目标表
	 */
	public static TreeMap<String,InsDataItem> itemSaveTable = new TreeMap<String,InsDataItem>(); 
	/**
	 * 指令与数据项关系
	 */
	public static TreeMap<String,List<InsDataItem>> insDataItemMap = new TreeMap<String,List<InsDataItem>>(); 
}
