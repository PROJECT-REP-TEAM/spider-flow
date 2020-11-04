package org.spiderflow.core.executor.function;

import com.alibaba.fastjson.JSON;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.spiderflow.annotation.Comment;
import org.spiderflow.annotation.Example;
import org.spiderflow.core.utils.ExtractUtils;
import org.spiderflow.executor.FunctionExecutor;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Json和String互相转换 工具类 防止NPE
 *
 * @author Administrator
 */
@Component
@Comment("json常用方法")
public class CommonFunctionExecutor implements FunctionExecutor {

    @Override
    public String getFunctionPrefix() {
        return "common";
    }

    @Comment("将对象装为json键值对字符串")
    @Example("${common.toJsonStr(objVar,selectKey,selectValue)}")
    public static String toJsonStr(Object object, String selectKey, String selectValue) {
        List<Map<String, Object>> list = new ArrayList<>();

        if (object instanceof Elements) {
            Elements elements = (Elements) object;
            Iterator<Element> els = elements.iterator();
            while (els.hasNext()) {
                Element element = els.next();
                Map<String, Object> datas = getMap(element, selectKey, selectValue);
                if (datas != null)
                    list.add(datas);
            }
        } else if (object instanceof Element) {
            Element element = (Element) object;
            Map<String, Object> datas = getMap(element, selectKey, selectValue);
            if (datas != null)
                list.add(datas);
        }
        return object != null ? JSON.toJSONString(list) : null;
    }


    @Comment("将对象装为json键值对字符串")
    @Example("${common.toJsonParamStr(objVar,keys,selects,attrs)}")
    public static String toJsonParamStr(Object object, ArrayList<String> keys, ArrayList<String> selects, ArrayList<String> attrs) {
        List<Map<String, Object>> list = new ArrayList<>();

        if (keys == null || selects == null || keys.size() == 0 || selects.size() == 0 || keys.size() != selects.size()) {
            return "{}";
        }

        if (object instanceof Elements) {
            Elements elements = (Elements) object;
            Iterator<Element> els = elements.iterator();
            while (els.hasNext()) {
                Map<String, Object> datas = new HashMap<>();
                Element element = els.next();
                int size = selects.size();
                for (int i = 0; i < size; i++) {
                    String key = keys.get(i);
                    String attr = null;
                    if (attrs != null && attrs.size() >= i + 1) {
                        attr = attrs.get(i);
                    }
                    String value = getMapValue(element, selects.get(i), attr);
                    if (value != null) {
                        datas.put(key, value);
                    }
                }
                if (datas.size() > 0)
                    list.add(datas);

            }
        } else if (object instanceof Element) {
            Element element = (Element) object;
            Map<String, Object> datas = new HashMap<>();
            int size = selects.size();
            for (int i = 0; i < size; i++) {
                String key = keys.get(i);
                String attr = null;
                if (attrs != null && attrs.size() >= i + 1) {
                    attr = attrs.get(i);
                }
                String value = getMapValue(element, selects.get(i), attr);
                if (value != null) {
                    datas.put(key, value);
                }
            }
            if (datas.size() > 0)
                list.add(datas);
        }
        return object != null ? JSON.toJSONString(list) : null;
    }


    @Comment("将对象装为json键值对字符串")
    @Example("${common.toJsonParamStringify(objVar,keys,selects,attrs)}")
    public static String toJsonParamStringify(Object object, ArrayList<String> keys, ArrayList<String> selects, ArrayList<String> attrs) {
        return toJsonParamStr(object, keys, selects, attrs).replaceAll("\"", "\\\"");
    }


    @Comment("将两个数字对象合并为键值对")
    @Example("${common.toJsonParamArrayStr(['startNumber','price'],priceNumItems1,priceNumItems2)}")
    public static String toJsonParamArrayStr(ArrayList<String> keys, Object object1, Object object2) {

        List<Map<String, Object>> list = new ArrayList<>();

        if (keys == null || keys.size() == 0 || object1 == null || object2 == null) {
            return null;
        }

        List<String> oneDatas = new ArrayList<>();
        List<String> twoDatas = new ArrayList<>();

 /*       {
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
*/


        if (object1 instanceof Elements) {
            Elements elements1 = (Elements) object1;
            Iterator<Element> els = elements1.iterator();
            while (els.hasNext()) {
                Element element = els.next();
                String value = element.text();
                oneDatas.add(value);
            }
        } else if (object1 instanceof Element) {
            Element element = (Element) object1;
            String value = element.text();
            oneDatas.add(value);
        }

        if (object2 instanceof Elements) {
            Elements elements1 = (Elements) object2;
            Iterator<Element> els = elements1.iterator();
            while (els.hasNext()) {
                Element element = els.next();
                String value = element.text();
                twoDatas.add(value);
            }
        } else if (object2 instanceof Element) {
            Element element = (Element) object2;
            String value = element.text();
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

    public static String getMapValue(Element element, String selectKey, String attrKey) {
        List<String> key = null;
        if (attrKey != null) {
            key = ExtractUtils.getAttrBySelector(element, selectKey, attrKey);
        } else {
            key = ExtractUtils.getTextBySelector(element, selectKey);
        }
        if (key == null || key.size() == 0)
            return null;
        return key.get(0);
    }


    public static Map<String, Object> getMap(Element element, String selectKey, String selectValue) {
        List<String> key = ExtractUtils.getTextBySelector(element, selectKey);
        List<String> value = ExtractUtils.getTextBySelector(element, selectValue);
        if (key == null || key.size() == 0 || value == null || value.size() == 0)
            return null;

        Map<String, Object> datas = new HashMap<>();
        datas.put("key", key.get(0));
        datas.put("value", value.get(0));
        return datas;
    }
}
