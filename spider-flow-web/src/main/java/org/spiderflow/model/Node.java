package org.spiderflow.model;


import java.util.Date;
import java.util.List;

public class Node {
    private String id;
    private String ip;
    private String hostname;
    private int taskCount;
    private List<NodeTask> taskList;
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public List<NodeTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<NodeTask> taskList) {
        this.taskList = taskList;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
