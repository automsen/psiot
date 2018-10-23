/**
 * TODO: complete the comment
 */
package cn.com.tw.saas.serv.entity.db.cust;

import java.util.Date;

/**
 * 客户——房间关联
 * 数据库实体
 * 对应t_saas_customer_room
 */
public class CustomerRoom {

    private String id;

    private String orgId;

    private String roomAccountId;

    private String customerId;

    private String roomId;

    /**
     * 0，尚未激活
1，正常使用中
2，取消关联过程中
3，历史数据
     */
    private Byte status;

    private Date createTime;

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
    public String getRoomAccountId() {
        return roomAccountId;
    }
    public void setRoomAccountId(String roomAccountId) {
        this.roomAccountId = roomAccountId == null ? null : roomAccountId.trim();
    }
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId == null ? null : customerId.trim();
    }
    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId == null ? null : roomId.trim();
    }
    public Byte getStatus() {
        return status;
    }
    public void setStatus(Byte status) {
        this.status = status;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}