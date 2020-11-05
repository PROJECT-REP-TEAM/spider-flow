package org.spiderflow.core.executor.function;

import com.alibaba.fastjson.JSON;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.spiderflow.annotation.Comment;
import org.spiderflow.annotation.Example;
import org.spiderflow.core.utils.ExtractUtils;
import org.spiderflow.executor.FunctionExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
@Comment("taoic常用方法")
public class TaoICFunctionExecutor implements FunctionExecutor {

    @Override
    public String getFunctionPrefix() {
        return "taoic";
    }


    @Comment("将对象装为taoic键值对字符串")
    @Example("${taoic.companyInfo(str)}")
    public static List<Map<String, String>> companyInfo(String str) {
        List<Map<String, String>> datas = new ArrayList<>();

        Pattern pattern = Pattern.compile("(?<=CompanyInfo:\\{)(.+?)(?=\\})");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            String json = ("{" + matcher.group() + "}");
            String companyName = null;
            String mobilePhone = null;
            String phone = null;
            String address = null;
            try {
                companyName = (json.substring(json.indexOf("CompanyName:\"") + "CompanyName:\"".length(), json.indexOf("\",CompanyType:")) + " ");
            } catch (Exception e) {
            }
            try {
                mobilePhone = (json.substring(json.indexOf("MobilePhone:\"") + "MobilePhone:\"".length(), json.indexOf("\",QQ:")) + " ");
            } catch (Exception e) {
            }
            try {
                phone = (json.substring(json.indexOf(",Phone:\"") + ",Phone:\"".length(), json.indexOf("\",Fax:")) + " ");
            } catch (Exception e) {
            }
            try {
                address = (json.substring(json.indexOf(",Address:\"") + ",Address:\"".length(), json.indexOf("\",Linkman:")) + " ");
            } catch (Exception e) {
            }
            if (mobilePhone != null && phone != null) {
                Map<String, String> d = new HashMap<>();
                d.put("companyName", companyName);
                d.put("mobilePhone", mobilePhone);
                d.put("phone", phone);
                d.put("address", address);
                //System.out.println(json);
                datas.add(d);
            }
        }
        return datas;
    }
}
