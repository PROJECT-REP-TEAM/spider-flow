package org.spiderflow.reset.io;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.helper.HttpConnection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * 请求对象包装类
 *
 * @author Administrator
 */
public class HttpResetRequest {

    private Connection connection = null;

    public static HttpResetRequest create() {
        return new HttpResetRequest();
    }

    public HttpResetRequest url(String url) {
        this.connection = HttpConnection.connect(url);
        this.connection.method(Method.GET);
        this.connection.timeout(60000);
        return this;
    }

    public HttpResetRequest headers(Map<String, String> headers) {
        this.connection.headers(headers);
        return this;
    }

    public HttpResetRequest header(String key, String value) {
        this.connection.header(key, value);
        return this;
    }

    public HttpResetRequest header(String key, Object value) {
        if (value != null) {
            this.connection.header(key, value.toString());
        }
        return this;
    }

    public HttpResetRequest cookies(Map<String, String> cookies) {
        this.connection.cookies(cookies);
        return this;
    }

    public HttpResetRequest cookie(String name, String value) {
        if (value != null) {
            this.connection.cookie(name, value);
        }
        return this;
    }

    public HttpResetRequest contentType(String contentType) {
        this.connection.header("Content-Type", contentType);
        return this;
    }

    public HttpResetRequest data(String key, String value) {
        this.connection.data(key, value);
        return this;
    }

    public HttpResetRequest data(String key, Object value) {
        if (value != null) {
            this.connection.data(key, value.toString());
        }
        return this;
    }

    public HttpResetRequest data(String key, String filename, InputStream is) {
        this.connection.data(key, filename, is);
        return this;
    }

    public HttpResetRequest data(Object body) {
        if (body != null) {
            this.connection.requestBody(body.toString());
        }
        return this;
    }

    public HttpResetRequest data(Map<String, String> data) {
        this.connection.data(data);
        return this;
    }

    public HttpResetRequest method(String method) {
        this.connection.method(Method.valueOf(method));
        return this;
    }

    public HttpResetRequest followRedirect(boolean followRedirects) {
        this.connection.followRedirects(followRedirects);
        return this;
    }

    public HttpResetRequest timeout(int timeout) {
        this.connection.timeout(timeout);
        return this;
    }

    public HttpResetRequest proxy(String host, int port) {
        this.connection.proxy(host, port);
        return this;
    }

    @SuppressWarnings("deprecation")
    public HttpResetRequest validateTLSCertificates(boolean value) {
        this.connection.validateTLSCertificates(value);
        return this;
    }

    public HttpResetResponse execute() throws IOException {
        this.connection.ignoreContentType(true);
        this.connection.ignoreHttpErrors(true);
        this.connection.maxBodySize(0);

        Response response = connection.execute();
        return new HttpResetResponse(response);
    }
}
