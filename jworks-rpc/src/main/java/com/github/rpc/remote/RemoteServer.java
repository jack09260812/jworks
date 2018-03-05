package com.github.rpc.remote;

import com.github.rpc.model.RpcFuture;
import com.github.rpc.model.RpcRequest;

/**
 * Created by jinwei.li on 2017/6/27 0027.
 */
public interface RemoteServer {
    /**
     * 启动服务
     * @throws Exception
     */
    void start() throws Exception ;

    /**
     * 发送请求
     * @param request
     * @return
     * @throws Exception
     */
    RpcFuture send(RpcRequest request) throws Exception;
}
