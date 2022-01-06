package com.riverstar.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Author: Hardy
 * Date:   2019/7/23
 * Description:
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private int age;

    private String email;

    /**
     * 逻辑删除
     */
    @TableLogic(value = "0", delval = "1")
    @TableField(fill = FieldFill.INSERT)
    private int deleteFlag;

    /**
     * 版本号（用于乐观锁， 默认为 1）
     */
    @Version
    @TableField(fill = FieldFill.INSERT)
    private Integer version;
}
