package com.riverstar.model.client;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Author: Hardy
 * Date:   2019/7/23
 * Description:
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class TestServerRequest extends BaseClientRequest {
    private int age;

    private String name;
}
