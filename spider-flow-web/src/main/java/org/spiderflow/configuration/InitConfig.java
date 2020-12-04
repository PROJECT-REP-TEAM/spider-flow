package org.spiderflow.configuration;

import org.spiderflow.core.service.SpiderFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class InitConfig {

    @Autowired
    private SpiderFlowService spiderFlowService;

    @PostConstruct
    public void init(){

    }

}
