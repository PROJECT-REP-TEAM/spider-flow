package org.spiderflow.node;

import org.spiderflow.context.SpiderContext;
import org.spiderflow.core.job.SpiderJob;
import org.spiderflow.core.utils.LocalHostUtils;
import org.spiderflow.model.Node;
import org.spiderflow.model.NodeTask;
import org.spiderflow.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class NodeRegisterService {

    @Value("${spider.node.register.url}")
    private String registerUrl;

    @Value("${spider.node.register.token}")
    private String registerToken;

    @Value("${spider.node.work.url}")
    private String takeNodeWorkUrl;

    @Value("${random.uuid}")
    private String nodeId;

    private LocalHostUtils lhu = new LocalHostUtils();

    @Autowired
    private RestService restService;


    //3秒上报一次信息
    @Scheduled(fixedRate = 3000)
    public void registerNodeInfo() {
        Node node = new Node();
        node.setId(nodeId);
        node.setIp(lhu.getIp());
        node.setHostname(lhu.getHostName());
        node.setCreateTime(new Date());
        List<SpiderContext> contextList = SpiderJob.getSpiderContext();
        List<NodeTask> tasks = new ArrayList<>();
        contextList.forEach(sc -> {
            NodeTask nodeTask = new NodeTask();
            nodeTask.setTaskId(sc.getId());
            nodeTask.setFlowId(sc.getFlowId());
            nodeTask.setRunning(sc.isRunning());
            tasks.add(nodeTask);
        });
        node.setTaskCount(contextList.size());
        node.setTaskList(tasks);
        restService.postForJSON(registerUrl, node, registerToken);
        //System.out.println(rs);
    }


    //获取任务信息
    @Scheduled(fixedRate = 3000)
    public void takeNodeWork() {
        //String rs = restService.post(takeNodeWorkUrl, "{}", registerToken);
        //System.out.println(rs);
    }

}
