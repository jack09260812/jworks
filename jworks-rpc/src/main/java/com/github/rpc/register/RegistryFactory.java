package com.github.rpc.register;

import com.github.rpc.register.support.ZookeeperRegistry;
import org.apache.commons.lang3.StringUtils;

/**
 * 注册中心工厂类
 */
public class RegistryFactory {

    public static ServiceRegistry getRegistry(String address) {

        if (StringUtils.isEmpty(address)) {
            throw new IllegalArgumentException("address can`t be null！");
        }
        if (address.startsWith("zookeeper://")) {
            return new ZookeeperRegistry(address.replace("zookeeper://", ""));
        }
        throw new IllegalArgumentException("illegal address protocol");
    }

}
