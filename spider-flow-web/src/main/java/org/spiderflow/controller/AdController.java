package org.spiderflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;


@Controller
public class AdController {

    @RequestMapping("/ddm/adj/{adCode}.js")
    public String dd(@PathVariable("adCode") String adCode, ModelMap map, HttpServletResponse response) {
        response.setContentType("text/html; charset=UTF-8");
        //广告编码
        //System.out.println("广告编码:" + adCode);

        map.put("adCode", adCode);
        map.put("imgWidth", "585");
        map.put("imgHeight", "90");
        map.put("imgUrl", "https://res-js.hqewimg.com/res/global/img/logo.png");
        map.put("clickUrl", "http://172.168.30.62:8088/pcs/click?c="+adCode+"&adurl=http%3a%2f%2fbaidu.com");
        map.put("viewUrl", "http://172.168.30.62:8088/pcs/view?c="+adCode);


        map.put("dataJcVersion", "r2020116");
        map.put("dataJc", "86");




        return "ad";
    }

    @RequestMapping("/pcs/view")
    @ResponseBody
    public void pcsView(@RequestParam("c") String adCode) {
        System.out.println("接受到展示请求:" + adCode);
    }

    @RequestMapping("/pagead/gen_204")
    @ResponseBody
    public void error(@RequestParam("c") String adCode) {
        System.out.println("接受到展示请求:" + adCode);
    }

    @RequestMapping("/pcs/click")
    public String pcsView(@RequestParam("c") String adCode, @RequestParam("adurl") String adUrl) {
        System.out.println("接受点击:" + adCode);
        return "redirect:" + adUrl;
    }

    @RequestMapping("/demo.html")
    public String demo() {
        return "demo";
    }

}
