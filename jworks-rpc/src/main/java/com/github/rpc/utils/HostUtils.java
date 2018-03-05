package com.github.rpc.utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HostUtils {

    public static final String IP;
    public static final String HOSTNAME;
    private static final Logger logger = LoggerFactory.getLogger(HostUtils.class);

    static {
        String ip;
        String hostname;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
            hostname = addr.getHostName();
        } catch (UnknownHostException e) {
            logger.error("Can't find out address: " + e.getMessage());
            ip = "UNKNOWN";
            hostname = "UNKNOWN";
        }
        if (ip.equals("127.0.0.1") || ip.equals("::1") || ip.equals("UNKNOWN")) {
            try {
                Process process = Runtime.getRuntime().exec("hostname -i");
                if (process.waitFor() == 0) {
                    ip = new String(IOUtils.toByteArray(process.getInputStream()), "UTF8");
                }
                process = Runtime.getRuntime().exec("hostname");
                if (process.waitFor() == 0) {
                    hostname = (new String(IOUtils.toByteArray(process.getInputStream()), "UTF8")).trim();
                }
            } catch (Exception e) {
                logger.warn("get hostname failed {}", e.getMessage());
            }
        }
        IP = ip;
        HOSTNAME = hostname;
        logger.info("IP {} HOSTNAME {}", IP, HOSTNAME);
    }
}
