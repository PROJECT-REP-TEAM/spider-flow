package org.spiderflow.model;

public class FlowProxy {
    private String proxyHost;
    private Integer proxyPort;
    private String proxyUser;
    private String proxyPass;
    private String switchIpHeaderKey;
    private String switchIpHeaderVal;
    private String scheme;


    @Override
    public String toString() {
        return "FlowProxy{" +
                "proxyHost='" + proxyHost + '\'' +
                ", proxyPort=" + proxyPort +
                ", proxyUser='" + proxyUser + '\'' +
                ", proxyPass='" + proxyPass + '\'' +
                ", switchIpHeaderKey='" + switchIpHeaderKey + '\'' +
                ", switchIpHeaderVal='" + switchIpHeaderVal + '\'' +
                ", scheme='" + scheme + '\'' +
                '}';
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getProxyPass() {
        return proxyPass;
    }

    public void setProxyPass(String proxyPass) {
        this.proxyPass = proxyPass;
    }

    public String getSwitchIpHeaderKey() {
        return switchIpHeaderKey;
    }

    public void setSwitchIpHeaderKey(String switchIpHeaderKey) {
        this.switchIpHeaderKey = switchIpHeaderKey;
    }

    public String getSwitchIpHeaderVal() {
        return switchIpHeaderVal;
    }

    public void setSwitchIpHeaderVal(String switchIpHeaderVal) {
        this.switchIpHeaderVal = switchIpHeaderVal;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }
}
