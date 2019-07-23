package com.zrb.model.message;

import lombok.Data;

/**
 * Author: Hardy
 * Date:   2018/7/20 16:09
 * Description:
 **/
@Data
public abstract class BaseMessage {

    protected long createTime;

    protected int count;

    BaseMessage() {
        this.createTime = System.currentTimeMillis();
    }

    public void incr() {
        count++;
    }
}
