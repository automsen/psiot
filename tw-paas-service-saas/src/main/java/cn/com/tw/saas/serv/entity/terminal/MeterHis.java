/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.entity.terminal;

/**
 * 仪表与仪表账户(历史数据)
 * 数据库实体
 * t_saas_meter_history
 */
public class MeterHis extends Meter{

    private String id;
    
    private String price;
    
    
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}