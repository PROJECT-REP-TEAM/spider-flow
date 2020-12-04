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
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * 输出执行器
 *
 * @author Administrator
 */
@Component
public class CollectExecutor implements ShapeExecutor, SpiderListener {


    public static final String OUTPUT_NAME = "output-name";

    public static final String OUTPUT_VALUE = "output-value";


    private static Logger logger = LoggerFactory.getLogger(CollectExecutor.class);

    /**
     * 输出CSVPrinter节点变量
     */
    private Map<String, CSVPrinter> cachePrinter = new HashMap<>();

    @Override
    public void execute(SpiderNode node, SpiderContext context, Map<String, Object> variables) {
        SpiderOutput output = new SpiderOutput();
        output.setNodeName(node.getNodeName());
        output.setNodeId(node.getNodeId());

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
            output.addOutput(outputName, value);
            if (value != null) {
                outputData.put(outputName, value);
            }
        }

        if (outputData.size() > 0) {
            List<Map<String, Object>> collects = context.get("collects");
            if (collects == null) {
                collects = new ArrayList<>();
                context.put("collects", collects);
            }
            collects.add(outputData);

            variables.put("collects",collects);
        }

    }


    @Override
    public String supportShape() {
        return "collects";
    }

    @Override
    public void beforeStart(SpiderContext context) {

    }

    @Override
    public void afterEnd(SpiderContext context) {
        this.releasePrinters();
    }

    private void releasePrinters() {
        for (Iterator<Map.Entry<String, CSVPrinter>> iterator = this.cachePrinter.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry<String, CSVPrinter> entry = iterator.next();
            CSVPrinter printer = entry.getValue();
            if (printer != null) {
                try {
                    printer.flush();
                    printer.close();
                    this.cachePrinter.remove(entry.getKey());
                } catch (IOException e) {
                    logger.error("文件输出错误,异常信息:{}", e.getMessage(), e);
                    ExceptionUtils.wrapAndThrow(e);
                }
            }
        }
    }
}
