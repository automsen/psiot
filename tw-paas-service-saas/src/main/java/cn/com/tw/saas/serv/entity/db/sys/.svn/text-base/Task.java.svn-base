package cn.com.tw.saas.serv.entity.db.sys;

import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3634362121683552682L;

	/**
	 * 主键
	 */
    private String id;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 分组
     */
    private String groupName;

    /**
     * 任务描述
     */
    private String taskName;

    /**
     * 指定代码中的类
     */
    private String taskCode;

    /**
     * 计时表达式
     */
    private String cronExpression;

    /**
     * 状态(1，开启  0，关闭)
     */
    private Byte status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 上次执行时间
     */
    private Date lastTime;

    /**
     * 上次执行是否成功(1、成功  2、失败)
     */
    private Byte isLastSuccess;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName == null ? null : groupName.trim();
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode == null ? null : taskCode.trim();
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression == null ? null : cronExpression.trim();
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

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public Byte getIsLastSuccess() {
        return isLastSuccess;
    }

    public void setIsLastSuccess(Byte isLastSuccess) {
        this.isLastSuccess = isLastSuccess;
    }
}