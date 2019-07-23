package com.zrb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Author: Hardy
 * Date:   2019/7/23
 * Description:
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class User extends BaseEntity {
    private String name;

    private Integer age;
}
