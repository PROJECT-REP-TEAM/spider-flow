package org.spiderflow.reset.executor.shape;

import java.io.*;
import java.util.*;

import org.spiderflow.Grammerable;
import org.spiderflow.core.executor.shape.RequestExecutor;
import org.spiderflow.core.utils.ExpressionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spiderflow.context.SpiderContext;
import org.spiderflow.executor.ShapeExecutor;
import org.spiderflow.io.SpiderResponse;
import org.spiderflow.listener.SpiderListener;
import org.spiderflow.model.Grammer;
import org.spiderflow.model.Shape;
import org.spiderflow.model.SpiderNode;
import org.spiderflow.reset.io.HttpResetRequest;
import org.spiderflow.reset.io.HttpResetResponse;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class HttpResetExecutor implements ShapeExecutor, Grammerable, SpiderListener {

    public static final String URL = "url";

    public static final String REQUEST_METHOD = "method";

    public static final String PARAMETER_NAME = "parameter-name";

    public static final String PARAMETER_VALUE = "parameter-value";

    public static final String COOKIE_NAME = "cookie-name";

    public static final String COOKIE_VALUE = "cookie-value";

    public static final String PARAMETER_FORM_NAME = "parameter-form-name";

    public static final String PARAMETER_FORM_VALUE = "parameter-form-value";

    public static final String PARAMETER_FORM_FILENAME = "parameter-form-filename";

    public static final String PARAMETER_FORM_TYPE = "parameter-form-type";

    public static final String BODY_TYPE = "body-type";

    public static final String BODY_CONTENT_TYPE = "body-content-type";

    public static final String REQUEST_BODY = "request-body";

    public static final String HEADER_NAME = "header-name";

    public static final String HEADER_VALUE = "header-value";

    public static final String TIMEOUT = "timeout";

    public static final String RETRY_COUNT = "retryCount";

    public static final String RETRY_INTERVAL = "retryInterval";

    public static final String RESPONSE_CHARSET = "response-charset";

    public static final String FOLLOW_REDIRECT = "follow-redirect";

    public static final String TLS_VALIDATE = "tls-validate";

    public static final String IS_REPEAT = "_is_repeat";

    private static final Logger logger = LoggerFactory.getLogger(RequestExecutor.class);

    @Override
    public Shape shape() {
        Shape shape = new Shape();
        shape.setImage("data:image/png;base64,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");
        shape.setName("httpReset");
        shape.setTitle("httpReset");
        shape.setLabel("httpReset");
        return shape;
    }

    @Override
    public String supportShape() {
        return "httpReset";
    }

    @PostConstruct
    void init() {
        //允许设置被限制的请求头
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
    }

    @Override
    public boolean allowExecuteNext(SpiderNode node, SpiderContext context, Map<String, Object> variables) {
        return variables.remove(IS_REPEAT) == null;
    }

    @Override
    public void execute(SpiderNode node, SpiderContext context, Map<String, Object> variables) {
        //重试次数
        int retryCount = NumberUtils.toInt(node.getStringJsonValue(RETRY_COUNT), 0) + 1;
        //重试间隔时间，单位毫秒
        int retryInterval = NumberUtils.toInt(node.getStringJsonValue(RETRY_INTERVAL), 0);

        boolean successed = false;
        for (int i = 0; i < retryCount && !successed; i++) {
            HttpResetRequest request = HttpResetRequest.create();
            //设置请求url
            String url = null;
            try {
                url = ExpressionUtils.execute(node.getStringJsonValue(URL), variables).toString();
            } catch (Exception e) {
                logger.error("设置请求url出错，异常信息", e);
                ExceptionUtils.wrapAndThrow(e);
            }

            context.pause(node.getNodeId(), "common", URL, url);
            logger.info("设置请求url:{}", url);
            request.url(url);
            //设置请求超时时间
            int timeout = NumberUtils.toInt(node.getStringJsonValue(TIMEOUT), 60000);
            logger.debug("设置请求超时时间:{}", timeout);
            request.timeout(timeout);

            String method = Objects.toString(node.getStringJsonValue(REQUEST_METHOD), "GET");
            //设置请求方法
            request.method(method);
            logger.debug("设置请求方法:{}", method);

            //是否跟随重定向
            boolean followRedirects = !"0".equals(node.getStringJsonValue(FOLLOW_REDIRECT));
            request.followRedirect(followRedirects);
            logger.debug("设置跟随重定向：{}", followRedirects);

            //是否验证TLS证书,默认是验证
            if ("0".equals(node.getStringJsonValue(TLS_VALIDATE))) {
                request.validateTLSCertificates(false);
                logger.debug("设置TLS证书验证：{}", false);
            }
            SpiderNode root = context.getRootNode();
            //设置请求header
            setRequestHeader(root, request, root.getListJsonValue(HEADER_NAME, HEADER_VALUE), context, variables);
            setRequestHeader(node, request, node.getListJsonValue(HEADER_NAME, HEADER_VALUE), context, variables);

            //设置全局Cookie
            Map<String, String> cookies = getRequestCookie(root, root.getListJsonValue(COOKIE_NAME, COOKIE_VALUE), context, variables);
            if (!cookies.isEmpty()) {
                logger.info("设置全局Cookie：{}", cookies);
                request.cookies(cookies);
            }

            //设置本节点Cookie
            cookies = getRequestCookie(node, node.getListJsonValue(COOKIE_NAME, COOKIE_VALUE), context, variables);
            if (!cookies.isEmpty()) {
                request.cookies(cookies);
                logger.debug("设置Cookie：{}", cookies);
            }

            String bodyType = node.getStringJsonValue(BODY_TYPE);
            List<InputStream> streams = null;
            if ("raw".equals(bodyType)) {
                String contentType = node.getStringJsonValue(BODY_CONTENT_TYPE);
                request.contentType(contentType);
                try {
                    Object requestBody = ExpressionUtils.execute(node.getStringJsonValue(REQUEST_BODY), variables);
                    context.pause(node.getNodeId(), "request-body", REQUEST_BODY, requestBody);
                    request.data(requestBody);
                    logger.debug("设置请求Body:{}", requestBody);
                } catch (Exception e) {
                    logger.debug("设置请求Body出错", e);
                }
            } else if ("form-data".equals(bodyType)) {
                List<Map<String, String>> formParameters = node.getListJsonValue(PARAMETER_FORM_NAME, PARAMETER_FORM_VALUE, PARAMETER_FORM_TYPE, PARAMETER_FORM_FILENAME);
                streams = setRequestFormParameter(node, request, formParameters, context, variables);
            } else {
                //设置请求参数
                setRequestParameter(root, request, root.getListJsonValue(PARAMETER_NAME, PARAMETER_VALUE), context, variables);
                setRequestParameter(node, request, node.getListJsonValue(PARAMETER_NAME, PARAMETER_VALUE), context, variables);
            }

            Throwable exception = null;
            try {
                HttpResetResponse response = request.execute();
                successed = response.getStatusCode() == 200;
                if (successed) {

                    String charset = node.getStringJsonValue(RESPONSE_CHARSET);
                    if (StringUtils.isNotBlank(charset)) {
                        response.setCharset(charset);
                        logger.debug("设置response charset:{}", charset);
                    }
                    //结果存入变量
                    variables.put("resp", response);
                }
            } catch (IOException e) {
                successed = false;
                exception = e;
            } catch (Error e) {
                successed = false;
                exception = e;
            } finally {
                if (streams != null) {
                    for (InputStream is : streams) {
                        try {
                            is.close();
                        } catch (Exception e) {
                        }
                    }
                }
                if (!successed) {
                    if (i + 1 < retryCount) {
                        if (retryInterval > 0) {
                            try {
                                Thread.sleep(retryInterval);
                            } catch (InterruptedException ignored) {
                            }
                        }
                        logger.info("第{}次重试:{}", i + 1, url);
                    } else {
                        logger.error("请求{}出错,异常信息:{}", url, exception);
                    }
                }
            }
        }
    }

    private List<InputStream> setRequestFormParameter(SpiderNode node, HttpResetRequest request, List<Map<String, String>> parameters, SpiderContext context, Map<String, Object> variables) {
        List<InputStream> streams = new ArrayList<>();
        if (parameters != null) {
            for (Map<String, String> nameValue : parameters) {
                Object value;
                String parameterName = nameValue.get(PARAMETER_FORM_NAME);
                if (StringUtils.isNotBlank(parameterName)) {
                    String parameterValue = nameValue.get(PARAMETER_FORM_VALUE);
                    String parameterType = nameValue.get(PARAMETER_FORM_TYPE);
                    String parameterFilename = nameValue.get(PARAMETER_FORM_FILENAME);
                    boolean hasFile = "file".equals(parameterType);
                    try {
                        value = ExpressionUtils.execute(parameterValue, variables);
                        if (hasFile) {
                            InputStream stream = null;
                            if (value instanceof byte[]) {
                                stream = new ByteArrayInputStream((byte[]) value);
                            } else if (value instanceof String) {
                                stream = new ByteArrayInputStream(((String) value).getBytes());
                            } else if (value instanceof InputStream) {
                                stream = (InputStream) value;
                            }
                            if (stream != null) {
                                streams.add(stream);
                                request.data(parameterName, parameterFilename, stream);
                                context.pause(node.getNodeId(), "request-body", parameterName, parameterFilename);
                                logger.info("设置请求参数：{}={}", parameterName, parameterFilename);
                            } else {
                                logger.warn("设置请求参数：{}失败，无二进制内容", parameterName);
                            }
                        } else {
                            request.data(parameterName, value);
                            context.pause(node.getNodeId(), "request-body", parameterName, value);
                            logger.info("设置请求参数：{}={}", parameterName, value);
                        }

                    } catch (Exception e) {
                        logger.error("设置请求参数：{}出错,异常信息:{}", parameterName, e);
                    }
                }
            }
        }
        return streams;
    }

    private Map<String, String> getRequestCookie(SpiderNode node, List<Map<String, String>> cookies, SpiderContext context, Map<String, Object> variables) {
        Map<String, String> cookieMap = new HashMap<>();
        if (cookies != null) {
            for (Map<String, String> nameValue : cookies) {
                Object value;
                String cookieName = nameValue.get(COOKIE_NAME);
                if (StringUtils.isNotBlank(cookieName)) {
                    String cookieValue = nameValue.get(COOKIE_VALUE);
                    try {
                        value = ExpressionUtils.execute(cookieValue, variables);
                        if (value != null) {
                            cookieMap.put(cookieName, value.toString());
                            context.pause(node.getNodeId(), "request-cookie", cookieName, value.toString());
                            logger.info("设置请求Cookie：{}={}", cookieName, value);
                        }
                    } catch (Exception e) {
                        logger.error("设置请求Cookie：{}出错,异常信息：{}", cookieName, e);
                    }
                }
            }
        }
        return cookieMap;
    }

    private void setRequestParameter(SpiderNode node, HttpResetRequest request, List<Map<String, String>> parameters, SpiderContext context, Map<String, Object> variables) {
        if (parameters != null) {
            for (Map<String, String> nameValue : parameters) {
                Object value = null;
                String parameterName = nameValue.get(PARAMETER_NAME);
                if (StringUtils.isNotBlank(parameterName)) {
                    String parameterValue = nameValue.get(PARAMETER_VALUE);
                    try {
                        value = ExpressionUtils.execute(parameterValue, variables);
                        context.pause(node.getNodeId(), "request-parameter", parameterName, value);
                        logger.info("设置请求参数：{}={}", parameterName, value);
                    } catch (Exception e) {
                        logger.error("设置请求参数：{}出错,异常信息：{}", parameterName, e);
                    }
                    request.data(parameterName, value);
                }
            }
        }
    }

    private void setRequestHeader(SpiderNode node, HttpResetRequest request, List<Map<String, String>> headers, SpiderContext context, Map<String, Object> variables) {
        if (headers != null) {
            for (Map<String, String> nameValue : headers) {
                Object value = null;
                String headerName = nameValue.get(HEADER_NAME);
                if (StringUtils.isNotBlank(headerName)) {
                    String headerValue = nameValue.get(HEADER_VALUE);
                    try {
                        value = ExpressionUtils.execute(headerValue, variables);
                        context.pause(node.getNodeId(), "request-header", headerName, value);
                        logger.debug("设置请求Header：{}={}", headerName, value);
                    } catch (Exception e) {
                        logger.error("设置请求Header：{}出错,异常信息：{}", headerName, e);
                    }
                    request.header(headerName, value);
                }
            }
        }
    }

    @Override
    public List<Grammer> grammers() {
        List<Grammer> grammers = Grammer.findGrammers(SpiderResponse.class, "resp", "SpiderResponse", false);
        Grammer grammer = new Grammer();
        grammer.setFunction("resp");
        grammer.setComment("请求结果");
        grammer.setOwner("SpiderResponse");
        grammers.add(grammer);
        return grammers;
    }

    @Override
    public void beforeStart(SpiderContext context) {

    }


    @Override
    public void afterEnd(SpiderContext context) {
    }
}