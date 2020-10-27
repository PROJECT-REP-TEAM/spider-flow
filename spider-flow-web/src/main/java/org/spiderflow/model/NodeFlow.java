package org.spiderflow.model;

import java.util.List;

public class NodeFlow {

    String name;
    String flowId;
    Integer running;

    List<NodeTask> taskList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public Integer getRunning() {
        return running;
    }

    public void setRunning(Integer running) {
        this.running = running;
    }

    public List<NodeTask> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<NodeTask> taskList) {
        this.taskList = taskList;
    }
}
