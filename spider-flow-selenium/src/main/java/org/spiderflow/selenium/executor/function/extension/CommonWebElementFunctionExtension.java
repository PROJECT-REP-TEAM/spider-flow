package org.spiderflow.selenium.executor.function.extension;

import com.alibaba.fastjson.JSON;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebElement;
import org.spiderflow.annotation.Comment;
import org.spiderflow.annotation.Example;
import org.spiderflow.executor.FunctionExecutor;
import org.spiderflow.selenium.model.WebElements;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Json和String互相转换 工具类 防止NPE
 *
 * @author Administrator
 */
@Component
@Comment("json常用方法")
public class CommonWebElementFunctionExtension implements FunctionExecutor {

    @Override
    public String getFunctionPrefix() {
        return "commonWebElement";
    }


    @Comment("将两个数字对象合并为键值对")
    @Example("${commonWebElement.toJsonParamArrayStr(['startNumber','price'],priceNumItems1,priceNumItems2)}")
    public static String toJsonParamArrayStr(ArrayList<String> keys, WebElements object1, WebElements object2) {

        List<Map<String, Object>> list = new ArrayList<>();

        if (keys == null || keys.size() == 0 || object1 == null || object2 == null) {
            return null;
        }

        List<String> oneDatas = new ArrayList<>();
        List<String> twoDatas = new ArrayList<>();

        {
            Iterator<WebElement> els = object1.iterator();
            while (els.hasNext()) {
                WebElement element = els.next();
                String value = element.getText();
                oneDatas.add(value);
            }
        }

        {
            Iterator<WebElement> els = object2.iterator();
            while (els.hasNext()) {
                WebElement element = els.next();
                String value = element.getText();
                twoDatas.add(value);
            }
        }



        /*if (object1 instanceof WebElements) {
            WebElements elements1 = (WebElements) object1;
            Iterator<WebElement> els = elements1.iterator();
            while (els.hasNext()) {
                WebElement element = els.next();
                String value = element.getText();
                oneDatas.add(value);
            }
        } else if (object1 instanceof WebElement) {
            WebElement element = (WebElement) object1;
            String value = element.getText();
            oneDatas.add(value);
        }*/

       /* if (object2 instanceof WebElements) {
            WebElements elements1 = (WebElements) object2;
            Iterator<WebElement> els = elements1.iterator();
            while (els.hasNext()) {
                WebElement element = els.next();
                String value = element.getText();
                twoDatas.add(value);
            }
        } else if (object2 instanceof WebElement) {
            WebElement element = (WebElement) object2;
            String value = element.getText();
            twoDatas.add(value);
        }*/


        int size = oneDatas.size();
        for (int i = 0; i < size; i++) {
            try {
                String data1 = oneDatas.get(i);
                String data2 = twoDatas.get(i);
                Map<String, Object> maps = new HashMap<>();
                maps.put(keys.get(0), data1);
                maps.put(keys.get(1), data2);
                list.add(maps);
            } catch (Exception e) {
            }
        }

        return JSON.toJSONString(list);
    }


    @Comment("将两个数字对象合并为键值对")
    @Example("${commonWebElement.toJsonParamArrayStr(['startNumber','price'],priceNumItems1,priceNumItems2)}")
    public static String toJsonParamArrayStr(ArrayList<String> keys, List<org.spiderflow.selenium.executor.function.extension.WebElementWrapper> object1, List<org.spiderflow.selenium.executor.function.extension.WebElementWrapper> object2) {

        List<Map<String, Object>> list = new ArrayList<>();

        if (keys == null || keys.size() == 0 || object1 == null || object2 == null) {
            return null;
        }

        List<String> oneDatas = new ArrayList<>();
        List<String> twoDatas = new ArrayList<>();


        for (WebElementWrapper element : object1) {
            String value = element.getText();
            oneDatas.add(value);
        }


        for (WebElementWrapper element : object2) {
            String value = element.getText();
            twoDatas.add(value);
        }

        int size = oneDatas.size();
        for (int i = 0; i < size; i++) {
            try {
                String data1 = oneDatas.get(i);
                String data2 = twoDatas.get(i);
                Map<String, Object> maps = new HashMap<>();
                maps.put(keys.get(0), data1);
                maps.put(keys.get(1), data2);
                list.add(maps);
            } catch (Exception e) {
            }
        }

        return JSON.toJSONString(list);
    }

}