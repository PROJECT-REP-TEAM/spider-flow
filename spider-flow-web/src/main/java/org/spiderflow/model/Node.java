package org.spiderflow.model;


import java.util.Date;
import java.util.List;

public class Node {
    private String nodeId;
    private String nodeIp;
    private String nodePort;
    private String hostname;
    private List<NodeFlow> flowList;
    private Date createTime;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public List<NodeFlow> getFlowList() {
        return flowList;
    }

    public void setFlowList(List<NodeFlow> flowList) {
        this.flowList = flowList;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getNodePort() {
        return nodePort;
    }

    public void setNodePort(String nodePort) {
        this.nodePort = nodePort;
    }
}
