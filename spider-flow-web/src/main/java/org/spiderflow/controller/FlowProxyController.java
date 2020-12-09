package org.spiderflow.controller;

import org.spiderflow.config.FlowProxyConfig;
import org.spiderflow.model.FlowProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/flowProxy")
public class FlowProxyController {

    @Autowired
    private FlowProxyConfig flowProxyConfig;


    @RequestMapping("/all")
    public List<FlowProxy> all() {
        List<FlowProxy> flowProxyList = new ArrayList<>();
        FlowProxy flowProxy = new FlowProxy();
        flowProxy.setName("--");
        flowProxyList.add(flowProxy);
        Map<String, FlowProxy> flowProxys = flowProxyConfig.getFlowProxys();
        if (flowProxys != null) {
            flowProxys.forEach((k, v) -> {
                v.setName(k);
                flowProxyList.add(v);
            });
        }

        return flowProxyList;
    }

}
