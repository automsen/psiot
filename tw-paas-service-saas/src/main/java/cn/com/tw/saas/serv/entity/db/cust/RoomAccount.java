/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.entity.db.cust;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 房间账户
 * 数据库实体
 * 对应t_saas_cust_room_account
 */
@Deprecated
public class RoomAccount {

    /**
     * 房间账户Id
     */
    private String accountId;

    private String orgId;

    /**
     * 房间主键
     */
    private String roomId;

    /**
     * 账户余额
     */
    private BigDecimal balance;

    /**
     * 0，尚未激活
1，正常使用中
2，取消关联过程中
3，历史数据
     */
    private Byte accountStatus;

    /**
     * 录入操作者id
     */
    private String operatorId;

    /**
     * 录入操作者姓名
     */
    private String operatorName;

    private Date createTime;

    private Date updateTime;

    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }
    public String getOrgId() {
        return orgId;
    }
    public void setOrgId(String orgId) {
        this.orgId = orgId == null ? null : orgId.trim();
    }
    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId == null ? null : roomId.trim();
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public Byte getAccountStatus() {
        return accountStatus;
    }
    public void setAccountStatus(Byte accountStatus) {
        this.accountStatus = accountStatus;
    }
    public String getCreatorId() {
        return operatorId;
    }
    public void setCreatorId(String operatorId) {
        this.operatorId = operatorId == null ? null : operatorId.trim();
    }
    public String getCreatorName() {
        return operatorName;
    }
    public void setCreatorName(String operatorName) {
        this.operatorName = operatorName == null ? null : operatorName.trim();
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}