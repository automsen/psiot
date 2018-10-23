package cn.com.tw.saas.serv.entity.org;


/**
 * 短信发送配置
 * @author liming
 * 2018年9月6日 14:27:31
 *
 */
public class OrgSmsConfig {
    private String id;

    private String orgId;

    private String templateNo;

    private String remark;
    /**
     *  json数组格式
     *  {
     *  	start:"09:00",
     *   	end :"09:00",
     *   	isSpanning:"0" 是否跨天  0 是  1 否	
     *  }
     */
    private String configJson;

    private Byte status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }

    public String getTemplateNo() {
        return templateNo;
    }

    public void setTemplateNo(String templateNo) {
        this.templateNo = templateNo == null ? null : templateNo.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getConfigJson() {
        return configJson;
    }

    public void setConfigJson(String configJson) {
        this.configJson = configJson == null ? null : configJson.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}