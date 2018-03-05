package com.github.rpc.listener;

import com.github.rpc.context.RpcClientContext;

import java.util.List;

/**
 * Created by jinwei.li on 2017/7/5 0005.
 */
public class RemoteServerChangeListener implements NotifyChangeListener<String, List> {

    private RpcClientContext context;

    public RemoteServerChangeListener(RpcClientContext context) {
        this.context = context;
    }

    @Override
    public void notifyChange(String s, List list) {
        context.addRemoteAddress(s, list);
    }
}
