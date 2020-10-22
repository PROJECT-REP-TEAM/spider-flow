package org.spiderflow.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reset")
public class RestResController {

    @PostMapping("output")
    public String outputData(@RequestBody String json) {
        System.out.println(json);
        return "success";
    }
}
