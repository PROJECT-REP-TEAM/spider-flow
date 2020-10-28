package org.spiderflow.node;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spiderflow.context.SpiderContext;
import org.spiderflow.controller.SpiderFlowController;
import org.spiderflow.core.Spider;
import org.spiderflow.core.job.SpiderJob;
import org.spiderflow.core.job.SpiderJobContext;
import org.spiderflow.core.model.SpiderFlow;
import org.spiderflow.core.model.Task;
import org.spiderflow.core.service.SpiderFlowService;
import org.spiderflow.core.service.TaskService;
import org.spiderflow.core.utils.LocalHostUtils;
import org.spiderflow.model.*;
import org.spiderflow.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class NodeRegisterService {

    @Value("${spider.node.register.url}")
    private String registerUrl;

    @Value("${spider.node.register.token}")
    private String registerToken;

    @Value("${spider.node.cmd.url}")
    private String takeNodeCmdUrl;

    @Value("${random.uuid}")
    private String nodeId;

    private LocalHostUtils lhu = new LocalHostUtils();

    @Autowired
    private RestService restService;

    @Autowired
    private SpiderFlowService spiderFlowService;

    @Autowired
    private Spider spider;

    @Value("${spider.workspace}")
    private String workspcace;

    @Autowired
    private SpiderJob spiderJob;

    @Autowired
    private TaskService taskService;

    private static Logger logger = LoggerFactory.getLogger(NodeRegisterService.class);

    private Set<String> hideFlow = new HashSet<>();


    private String nodeIdFile = "nodeId.txt";
    private String hideNodeFile = "hideNode.txt";

    @PostConstruct
    public void init() {

        try {
            File file = new File(workspcace, nodeIdFile);
            if (!file.exists()) {
                file.createNewFile();
                FileUtils.write(file, nodeId, "UTF-8");
            } else {
                nodeId = FileUtils.readFileToString(file, "UTF-8");
            }
        } catch (IOException ignored) {
        }

        try {
            File hFile = new File(workspcace, hideNodeFile);
            if (!hFile.exists()) {
                hFile.createNewFile();
            }
            List<String> lines = FileUtils.readLines(hFile, "UTF-8");
            if (lines != null) {
                hideFlow.addAll(lines);
            }
        } catch (IOException ignored) {
        }
    }

    //3秒上报一次信息
    @Scheduled(fixedRate = 3000)
    public void registerNodeInfo() {

        Node node = new Node();
        node.setNodeId(nodeId);
        node.setNodeIp(lhu.getIp());
        node.setHostname(lhu.getHostName());
        node.setCreateTime(new Date());

        List<SpiderFlow> flows = spiderFlowService.selectFlows();

        Map<Integer, SpiderContext> contextList = SpiderJob.getSpiderContext();

        Map<String, Map<Integer, SpiderContext>> contextMap = new HashMap<>();

        contextList.forEach((taskId, sc) -> {
            String flowId = sc.getFlowId();
            Map<Integer, SpiderContext> contexts = contextMap.get(flowId);
            if (contexts == null) {
                contexts = new HashMap<>();
                contextMap.put(flowId, contexts);
            }
            contexts.put(taskId, sc);
        });

        List<NodeFlow> flowList = new ArrayList<>();

        for (SpiderFlow flow : flows) {

            String name = flow.getName();
            String id = flow.getId();
            Integer running = flow.getRunning();

            if (hideFlow.contains(id)) {
                continue;
            }

            NodeFlow nodeFlow = new NodeFlow();
            nodeFlow.setFlowId(id);
            nodeFlow.setName(name);
            nodeFlow.setRunning(running);

            Map<Integer, SpiderContext> scMap = contextMap.get(id);

            List<NodeTask> taskList = new ArrayList<>();

            if (scMap != null && scMap.size() > 0) {
                scMap.forEach((taskId, sc) -> {
                    NodeTask nodeTask = new NodeTask();
                    nodeTask.setTaskId(taskId);
                    nodeTask.setRunning(sc.isRunning());
                    nodeTask.setName(sc.getId());
                    nodeTask.setFlowId(sc.getFlowId());
                    taskList.add(nodeTask);
                });
            }

            nodeFlow.setTaskList(taskList);

            flowList.add(nodeFlow);
        }

        node.setFlowList(flowList);

        restService.postForJSON(registerUrl, node, registerToken);

    }

    //获取任务信息
    @Scheduled(fixedRate = 5000)
    public void takeNodeWork() throws UnsupportedEncodingException {

      /*       "{" +
                "\"cmd\":\"" + cmd + "\"," +
                "\"action\":\"" + action + "\"," +
                "\"taskId\":\"" + taskId + "\"," +
                "\"flowId\":\"" + flowId + "\"" +
                "}"*/
        try {
            String rs = restService.get(takeNodeCmdUrl + "/" + nodeId, registerToken);
            if (StringUtils.hasText(rs)) {
                System.out.println(rs);
                JSONObject jsonObject = JSON.parseObject(rs);
                JSONObject data = jsonObject.getJSONObject("data");
                if (data != null) {
                    String cmd = data.getString("cmd");
                    String flowId = data.getString("flowId");
                    String action = data.getString("action");
                    Integer taskId = data.getInteger("taskId");
                    if ("run".equalsIgnoreCase(cmd)) {
                        runAsync(flowId);
                    } else if ("stop".equalsIgnoreCase(cmd)) {
                        stop(taskId);
                    } else if ("hide".equalsIgnoreCase(cmd)) {
                        hide(flowId);
                    } else if ("showAll".equalsIgnoreCase(cmd)) {
                        showAll();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("takeNodeWork error", e);
        }
    }

    private void showAll() {
        try {
            hideFlow.clear();
            File file = new File(workspcace, hideNodeFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileUtils.write(file, "", "UTF-8");
        } catch (IOException ignored) {
        }
    }

    private void hide(String flowId) {
        hideFlow.add(flowId);
        StringBuffer buffer = new StringBuffer();
        hideFlow.forEach(h -> {
            buffer.append(h);
            buffer.append("\n");
        });

        try {
            File file = new File(workspcace, hideNodeFile);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileUtils.write(file, buffer.toString(), "UTF-8");
        } catch (IOException ignored) {
        }
    }


    /**
     * 异步运行
     *
     * @param id
     * @return
     */
    public JsonBean<Integer> runAsync(String id) {
        SpiderFlow flow = spiderFlowService.getById(id);
        if (flow == null) {
            return new JsonBean<>(0, "找不到此爬虫信息");
        }
        Task task = new Task();
        task.setFlowId(flow.getId());
        task.setBeginTime(new Date());
        taskService.save(task);
        Spider.executorInstance.submit(() -> spiderJob.run(flow, task, null));
        return new JsonBean<>(task.getId());
    }

    /**
     * 停止运行任务
     *
     * @param taskId
     */
    public JsonBean<Void> stop(Integer taskId) {
        SpiderContext context = SpiderJob.getSpiderContext(taskId);
        if (context == null) {
            return new JsonBean<>(0, "任务不存在！");
        }
        context.setRunning(false);
        return new JsonBean<>(1, "停止成功！");

    }

}
