package cn.com.tw.paas.monit.entity.db.base;

public class CmdIns {
    private Integer id;

    private String cmdCode;

    private String equipType;
    
    private String protocolType;

    private String modelId;

	private String softVersoin;

    private Integer itemIndex;

    private String insId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCmdCode() {
		return cmdCode;
	}

	public void setCmdCode(String cmdCode) {
		this.cmdCode = cmdCode;
	}

	public String getEquipType() {
        return equipType;
    }

    public void setEquipType(String equipType) {
        this.equipType = equipType == null ? null : equipType.trim();
    }
    
    public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId == null ? null : modelId.trim();
    }

    public String getSoftVersoin() {
        return softVersoin;
    }

    public void setSoftVersoin(String softVersoin) {
        this.softVersoin = softVersoin == null ? null : softVersoin.trim();
    }

    public Integer getItemIndex() {
        return itemIndex;
    }

    public void setItemIndex(Integer itemIndex) {
        this.itemIndex = itemIndex;
    }

    public String getInsId() {
        return insId;
    }

    public void setInsId(String insId) {
        this.insId = insId == null ? null : insId.trim();
    }
}