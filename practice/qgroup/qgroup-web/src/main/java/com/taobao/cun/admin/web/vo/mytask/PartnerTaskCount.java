package com.taobao.cun.admin.web.vo.mytask;

import java.io.Serializable;
import java.util.List;

public class PartnerTaskCount implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5223298285534733423L;
	
	/*����״̬*/
	private String status;
	/*��״̬��������*/
	private String count;
	/*�����б����*/
	private List<PartnerTask>tasks;
	/*�Ƿ������������*/
    boolean hasNewTask;
    
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public List<PartnerTask> getTasks() {
		return tasks;
	}
	public void setTasks(List<PartnerTask> tasks) {
		this.tasks = tasks;
	}
	public boolean isHasNewTask() {
		return hasNewTask;
	}
	public void setHasNewTask(boolean hasNewTask) {
		this.hasNewTask = hasNewTask;
	}
    
}
