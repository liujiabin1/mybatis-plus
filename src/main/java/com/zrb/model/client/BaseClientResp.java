package com.zrb.model.client;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Author: Hardy
 * Date:   2018/7/20 14:44
 * Description:
 **/
@Data
public class BaseClientResp<T> {

    @JSONField(name = "error_no")
    private int errorNo;

    @JSONField(name = "error_message")
    private String errorMessage;

    private T data;
}
