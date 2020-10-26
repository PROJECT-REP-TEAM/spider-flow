package org.spiderflow.core.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class LocalHostUtils {
    private static volatile String cachedIpAddress;

    public LocalHostUtils() {
    }

    public String getIp() {
        if (null != cachedIpAddress) {
            return cachedIpAddress;
        } else {
            Enumeration netInterfaces = null;

            try {
                netInterfaces = NetworkInterface.getNetworkInterfaces();
            } catch (SocketException var7) {
                var7.printStackTrace();
            }

            if (netInterfaces == null) {
                return null;
            } else {
                String localIpAddress = null;

                while (netInterfaces.hasMoreElements()) {
                    NetworkInterface netInterface = (NetworkInterface) netInterfaces.nextElement();
                    Enumeration ipAddresses = netInterface.getInetAddresses();

                    while (ipAddresses.hasMoreElements()) {
                        InetAddress ipAddress = (InetAddress) ipAddresses.nextElement();
                        if (this.isPublicIpAddress(ipAddress)) {
                            String publicIpAddress = ipAddress.getHostAddress();
                            cachedIpAddress = publicIpAddress;
                            return publicIpAddress;
                        }

                        if (this.isLocalIpAddress(ipAddress)) {
                            localIpAddress = ipAddress.getHostAddress();
                        }
                    }
                }

                cachedIpAddress = localIpAddress;
                return localIpAddress;
            }
        }
    }

    private boolean isPublicIpAddress(InetAddress ipAddress) {
        return !ipAddress.isSiteLocalAddress() && !ipAddress.isLoopbackAddress() && !this.isV6IpAddress(ipAddress);
    }

    private boolean isLocalIpAddress(InetAddress ipAddress) {
        return ipAddress.isSiteLocalAddress() && !ipAddress.isLoopbackAddress() && !this.isV6IpAddress(ipAddress);
    }

    private boolean isV6IpAddress(InetAddress ipAddress) {
        return ipAddress.getHostAddress().contains(":");
    }

    public String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException var2) {
            var2.printStackTrace();
            return null;
        }
    }
}