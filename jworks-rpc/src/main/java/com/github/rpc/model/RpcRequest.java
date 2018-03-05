package com.github.rpc.model;

import lombok.Data;

@Data
public class RpcRequest {

    private String host;
    private int port;
    private String requestId;
    private String serverName;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

}