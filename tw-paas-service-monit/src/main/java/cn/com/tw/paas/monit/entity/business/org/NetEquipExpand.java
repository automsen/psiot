package cn.com.tw.paas.monit.entity.business.org;

import cn.com.tw.paas.monit.entity.db.org.NetEquip;

/**
 * 集抄设备扩展字段
 * @author Administrator
 *
 */
public class NetEquipExpand extends NetEquip{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3760372800177230745L;
	
	/**
	 * 型号名称
	 */
	private String modelName;

	/**
	 * 完整型号名称
	 */
	private String fullName;

	/**
	 * 产品名称
	 */
	private String productName;
	
	/**
	 * 生产厂家
	 */
	private String factory;
	
	//扩展字段
	
	private String netNumberSearch;

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getFactory() {
		return factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getNetNumberSearch() {
		return netNumberSearch;
	}

	public void setNetNumberSearch(String netNumberSearch) {
		this.netNumberSearch = netNumberSearch;
	}
	
	

}
