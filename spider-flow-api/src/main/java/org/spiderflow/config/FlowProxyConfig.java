package org.spiderflow.config;

import org.spiderflow.model.FlowProxy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "spring.flow")
public class FlowProxyConfig {

    private Map<String, FlowProxy> flowProxys;



    public Map<String, FlowProxy> getFlowProxys() {
        return flowProxys;
    }

    public void setFlowProxys(Map<String, FlowProxy> flowProxys) {
        this.flowProxys = flowProxys;
    }
}
