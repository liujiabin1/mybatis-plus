package com.zrb.constant.common;

/**
 * Author: Hardy
 * Date:   2018/7/30 12:55
 * Description:
 **/
public enum RabbitConst {

    TEST("test", "test-key", "test", false, false);


    public final String ex;
    public final String key;
    public final String queue;
    public final boolean init;           // 是否初始化队列
    public final boolean receive;        // 接受队列消息

    RabbitConst(String exchange, String routingKey, String queue, boolean init, boolean receive) {
        this.ex = exchange;
        this.key = routingKey;
        this.queue = queue;
        this.init = init;
        this.receive = receive;
    }
}
