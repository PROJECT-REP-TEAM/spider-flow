package org.spiderflow.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class RestService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * post 请求
     *
     * @param url   请求路径
     * @param data  body数据
     * @param token JWT所需的Token，不需要的可去掉
     * @return
     */
    public String post(String url, String data, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Encoding", "UTF-8");
        headers.add("Content-Type", "application/json; charset=UTF-8");
        if (token != null) {
            headers.add("Authorization", "Bearer " + token);
        }
        HttpEntity<String> requestEntity = new HttpEntity<>(data, headers);
        return restTemplate.postForObject(url, requestEntity, String.class);
    }

    /**
     * get 请求
     *
     * @param url   请求路径
     * @param token JWT所需的Token，不需要的可去掉
     * @return
     */
    public String get(String url, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Encoding", "UTF-8");
        headers.add("Content-Type", "application/json; charset=UTF-8");
        if (token != null) {
            headers.add("Authorization", token);
        }
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        String responseBody = response.getBody();
        return responseBody;
    }


    /**
     * 发送文件请求
     *
     * @param url
     * @param token
     * @return
     */
    public String file(String url, MultipartFile file, String token) {
        // 生成临时文件
        String tempFilePath = System.getProperty("java.io.tmpdir") + file.getOriginalFilename();
        File tmpFile = new File(tempFilePath);
        // 结果，抛异常就是 null
        String result = null;
        try {
            // 保存为文件
            file.transferTo(tmpFile);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            headers.setContentType(MediaType.parseMediaType("multipart/form-data;charset=UTF-8"));
            if (token != null) {
                headers.add("Authorization", token);
            }
            MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
            // 把临时文件变成 FileSystemResource
            FileSystemResource resource = new FileSystemResource(tempFilePath);
            param.add("file", resource);
            HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<>(param, headers);
            result = restTemplate.postForObject(url, formEntity, String.class);
            //删除临时文件文件
            tmpFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public String get(String url) {
        return restTemplate.getForObject(url, String.class);
    }

    public String request(String url, HttpMethod method, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Encoding", "UTF-8");
        headers.add("Content-Type", "application/json; charset=UTF-8");
        if (token != null) {
            headers.add("Authorization", "Bearer " + token);
        }
        return restTemplate.exchange(url,
                method,
                new HttpEntity<String>(headers),
                String.class).getBody();
    }

    /**
     * header为：json
     *
     * @param url
     * @param requestParams
     * @return
     * @throws Exception
     */
    public String postForJSON(String url, Object requestParams, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Encoding", "UTF-8");
        headers.add("Content-Type", "application/json; charset=UTF-8");
        if (token != null) {
            headers.add("Authorization", "Bearer " + token);
        }
        HttpEntity<Object> formEntity =
                new HttpEntity<>(JSONObject.toJSONString(requestParams), headers);
        String responseJson = restTemplate.postForObject(url, formEntity, String.class);
        return responseJson;
    }

    /**
     * post表单请求
     *
     * @param url
     * @param map
     * @return
     */
    public String postFormData(String url, Map<String, String> map, String token) {
        MultiValueMap<String, String> reqMap = new LinkedMultiValueMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            reqMap.add(entry.getKey(), entry.getValue());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Encoding", "UTF-8");
        if (token != null) {
            headers.add("Authorization", "Bearer " + token);
        }
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String res;
        try {
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(reqMap, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            res = response.getBody();
        } catch (Exception e) {
            throw e;
        }
        return res;
    }

    /**
     * header为：form-data
     *
     * @param url
     * @param requestParams
     * @return
     * @throws Exception
     */
    public JSONObject postForForm(String url, Map<String, ? extends Object> requestParams, String token) throws Exception {
        LinkedMultiValueMap body = new LinkedMultiValueMap();
        for (String key : requestParams.keySet()) {
            body.add(key, requestParams.get(key));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Encoding", "UTF-8");
        headers.add("Content-Type", "application/json; charset=UTF-8");
        if (token != null) {
            headers.add("Authorization", "Bearer " + token);
        }

        HttpEntity<String> entity = new HttpEntity(body, headers);
        JSONObject responseJson;
        try {
            responseJson = restTemplate.postForObject(url, entity, JSONObject.class);
        } catch (Exception e) {
            throw e;
        }
        return responseJson;
    }

}
