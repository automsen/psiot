package cn.com.tw.engine.core.entity;

import java.io.Serializable;

public class Task implements Serializable{

	/**
	 * "collect", "collect", CollectJob.class, "0/5 * * * * ?"
	 * String jobName, String groupName, Class taskClass, String cronExpression
	 */
	private static final long serialVersionUID = 1L;
	
	private final String jobName = "collect";
	
	private final String groupName = "collect";
	
	private String cronExp;
	
	private String status;

	public String getJobName() {
		return jobName;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getCronExp() {
		return cronExp;
	}

	public void setCronExp(String cronExp) {
		this.cronExp = cronExp;
	}

	@Override
	public String toString() {
		return "Task [jobName=" + jobName + ", groupName=" + groupName
				+ ", cronExp=" + cronExp + "]";
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
