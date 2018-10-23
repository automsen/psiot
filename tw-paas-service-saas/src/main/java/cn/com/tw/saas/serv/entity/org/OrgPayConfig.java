package cn.com.tw.saas.serv.entity.org;

public class OrgPayConfig {
	
    private String payConfigId;
   /**
    *  机构ID
    */
    private String payOrgId;
    /**
     *  支付类型（2031 微信支付  2032 支付宝）
     */
    private String payType;
    
    private String payConfigJson;
    /**
     * 配置状态（0,正常，1 失效）
     */
    private Byte configStatus;
    
    
    public String getPayConfigId() {
        return payConfigId;
    }

    public void setPayConfigId(String payConfigId) {
        this.payConfigId = payConfigId == null ? null : payConfigId.trim();
    }

    public String getPayOrgId() {
        return payOrgId;
    }

    public void setPayOrgId(String payOrgId) {
        this.payOrgId = payOrgId == null ? null : payOrgId.trim();
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType == null ? null : payType.trim();
    }

    public String getPayConfigJson() {
        return payConfigJson;
    }

    public void setPayConfigJson(String payConfigJson) {
        this.payConfigJson = payConfigJson == null ? null : payConfigJson.trim();
    }

    public Byte getConfigStatus() {
        return configStatus;
    }

    public void setConfigStatus(Byte configStatus) {
        this.configStatus = configStatus;
    }
    
    
}