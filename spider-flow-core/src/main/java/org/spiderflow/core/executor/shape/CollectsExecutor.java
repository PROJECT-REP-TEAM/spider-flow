package org.spiderflow.core.executor.shape;

import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spiderflow.context.SpiderContext;
import org.spiderflow.core.utils.ExpressionUtils;
import org.spiderflow.executor.ShapeExecutor;
import org.spiderflow.listener.SpiderListener;
import org.spiderflow.model.SpiderNode;
import org.spiderflow.model.SpiderOutput;
import org.spiderflow.utils.SpiderResponseHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * 输出执行器
 *
 * @author Administrator
 */
@Component
public class CollectsExecutor implements ShapeExecutor, SpiderListener {


    public static final String OUTPUT_NAME = "output-name";

    public static final String OUTPUT_VALUE = "output-value";
    public static final String COLLECTS = "collects";

    private static Logger logger = LoggerFactory.getLogger(CollectsExecutor.class);


    @Override
    public void execute(SpiderNode node, SpiderContext context, Map<String, Object> variables) {

        List<Map<String, String>> outputs = node.getListJsonValue(OUTPUT_NAME, OUTPUT_VALUE);
        Map<String, Object> outputData = new HashMap<>(outputs.size());
        for (Map<String, String> item : outputs) {
            Object value = null;
            String outputValue = item.get(OUTPUT_VALUE);
            String outputName = item.get(OUTPUT_NAME);
            try {
                value = ExpressionUtils.execute(outputValue, variables);
                context.pause(node.getNodeId(), "common", outputName, value);
                logger.debug("输出{}={}", outputName, value);
            } catch (Exception e) {
                logger.error("输出{}出错，异常信息：{}", outputName, e);
            }

            if (value != null) {
                outputData.put(outputName, value);
            }

        }

        if (outputData.size() > 0) {
//            List<Map<String, Object>> collects = context.get(COLLECTS);
//            if (collects == null) {
//                collects = new ArrayList<>();
//                context.put(COLLECTS, collects);
//            }
//            collects.add(outputData);
//
//            variables.put(COLLECTS, collects);
            context.collect(outputData);

          //  System.out.println("collect outputData:" + context.collectSize());
        }

    }


    @Override
    public String supportShape() {
        return "collects";
    }

    @Override
    public void beforeStart(SpiderContext context) {
        System.out.println("collects beforeStart... collectSize:" + context.collectSize());
    }

    @Override
    public void afterEnd(SpiderContext context) {
        System.out.println("collects afterEnd... collectSize:" + context.collectSize());
    }


    @Override
    public boolean allowExecuteNext(SpiderNode node, SpiderContext context, Map<String, Object> variables) {
        synchronized (node) {
            boolean isDone = node.isDone();
            if (isDone) {
                variables.put(COLLECTS, context.collects());
                context.clearCollects();
                logger.info("收集完成。。。" + context.collectSize());
            }
            return isDone;
        }
    }

}
