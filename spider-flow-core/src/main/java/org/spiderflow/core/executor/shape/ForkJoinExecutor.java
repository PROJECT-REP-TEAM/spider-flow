package org.spiderflow.core.executor.shape;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spiderflow.context.SpiderContext;
import org.spiderflow.executor.ShapeExecutor;
import org.spiderflow.model.SpiderNode;
import org.spiderflow.utils.SpiderResponseHolder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 等待执行结束执行器
 */
@Component
public class ForkJoinExecutor implements ShapeExecutor {

    private static Logger logger = LoggerFactory.getLogger(ForkJoinExecutor.class);


    /**
     * 缓存已完成节点的变量
     */
    private Map<String, Map<String, Object>> cachedVariables = new HashMap<>();

    @Override
    public void execute(SpiderNode node, SpiderContext context, Map<String, Object> variables) {
    }

    @Override
    public String supportShape() {
        return "forkJoin";
    }

    @Override
    public boolean allowExecuteNext(SpiderNode node, SpiderContext context, Map<String, Object> variables) {
        String key = context.getId() + "-" + node.getNodeId();
        synchronized (node) {
            boolean isDone = node.isDone();
            if (isDone) {
                try {
                    context.clearCollects();
                    SpiderResponseHolder.clear(context);
                    System.gc();
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
            Map<String, Object> cached = cachedVariables.get(key);
            if (!isDone) {
                if (cached == null) {
                    cached = new HashMap<>();
                    cachedVariables.put(key, cached);
                }
                cached.putAll(variables);
                System.gc();
            } else if (cached != null) {
                variables.putAll(cached);
                cachedVariables.remove(key);
            }
            return isDone;
        }
    }
}
