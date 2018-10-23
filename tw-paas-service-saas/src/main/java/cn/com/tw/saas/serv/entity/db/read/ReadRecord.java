package cn.com.tw.saas.serv.entity.db.read;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 读数记录
 * 数据库实体
 * 对应t_read_elec_use_total_active等11张表
 */
public class ReadRecord implements Serializable{

    private static final long serialVersionUID = -2162260888868207228L;

	/**
     * 主键
     */
    private String id;

    /**
     * 机构ID(外键关联机构信息表)
     */
    private String orgId;

    /**
     * 仪表通讯地址
     */
    private String meterAddr;
    
    /**
     * 回路类型
		0，总回路
		1，主回路
		2，副回路
		3、第三回路
     */
    private Integer loopType;

    /**
     * 读数1
     */
    private BigDecimal value1;

    /**
     * 读数2
     */
    private BigDecimal value2;

    /**
     * 读数3
     */
    private BigDecimal value3;

    /**
     * 读数4
     */
    private BigDecimal value4;

    /**
     * 读数5
     */
    private BigDecimal value5;

    /**
     * 读数6
     */
    private BigDecimal value6;

    /**
     * 时标，格式是yyyyMMddhhmm
     */
    private String freezeTd;
    
    /**
     * 时标，格式是yyyyMMddhhmm
     */
    private String freezeTd1;

    /**
     * 旧表读数与余额是否人工读取
            0，系统获取
            1，人工读取
     */
    private Byte isManual;

    /**
     * 读取时间
     */
    private Date readTime;
    
    private String readTimeStr;

    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 保存记录的表名
     * 非数据库字段
     */
    private String saveTable;

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
    public String getMeterAddr() {
        return meterAddr;
    }
    public void setMeterAddr(String meterAddr) {
        this.meterAddr = meterAddr == null ? null : meterAddr.trim();
    }
    public Integer getLoopType() {
		return loopType;
	}
	public void setLoopType(Integer loopType) {
		this.loopType = loopType;
	}
	public BigDecimal getValue1() {
        return value1;
    }
    public void setValue1(BigDecimal value1) {
        this.value1 = value1;
    }
    public BigDecimal getValue2() {
        return value2;
    }
    public void setValue2(BigDecimal value2) {
        this.value2 = value2;
    }
    public BigDecimal getValue3() {
        return value3;
    }
    public void setValue3(BigDecimal value3) {
        this.value3 = value3;
    }
    public BigDecimal getValue4() {
        return value4;
    }
    public void setValue4(BigDecimal value4) {
        this.value4 = value4;
    }
    public BigDecimal getValue5() {
        return value5;
    }
    public void setValue5(BigDecimal value5) {
        this.value5 = value5;
    }
    public BigDecimal getValue6() {
        return value6;
    }
    public void setValue6(BigDecimal value6) {
        this.value6 = value6;
    }
    public String getFreezeTd() {
        return freezeTd;
    }
    public void setFreezeTd(String freezeTd) {
        this.freezeTd = freezeTd == null ? null : freezeTd.trim();
    }
    public Byte getIsManual() {
        return isManual;
    }
    public void setIsManual(Byte isManual) {
        this.isManual = isManual;
    }
    public Date getReadTime() {
        return readTime;
    }
    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
	@Override
	public String toString() {
		return "ReadRecord [id=" + id + ", orgId=" + orgId + ", meterAddr=" + meterAddr + ", value1=" + value1
				+ ", value2=" + value2 + ", value3=" + value3 + ", value4=" + value4 + ", value5=" + value5
				+ ", value6=" + value6 + ", freezeTd=" + freezeTd + ", isManual=" + isManual + ", readTime=" + readTime
				+ ", createTime=" + createTime + "]";
	}
	public String getSaveTable() {
		return saveTable;
	}
	public void setSaveTable(String saveTable) {
		this.saveTable = saveTable;
	}
	public String getReadTimeStr() {
		return readTimeStr;
	}
	public void setReadTimeStr(String readTimeStr) {
		this.readTimeStr = readTimeStr;
	}
	public String getFreezeTd1() {
		return freezeTd1;
	}
	public void setFreezeTd1(String freezeTd1) {
		this.freezeTd1 = freezeTd1;
	}
	

}