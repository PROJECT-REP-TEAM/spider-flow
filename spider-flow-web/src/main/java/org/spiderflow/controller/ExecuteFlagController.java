package org.spiderflow.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spiderflow.model.JsonBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/executeFlag")
public class ExecuteFlagController {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteFlagController.class);


    @RequestMapping("/takeFlag")
    public JsonBean<String> loadFlagData(@RequestParam("flowId") String flowId) {
        logger.info("flowId:" + flowId);
        //return new JsonBean<>(executeFlagService.nextData(flowId));
        return null;
    }


}
