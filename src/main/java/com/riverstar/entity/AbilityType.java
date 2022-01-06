package com.riverstar.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 能力类型表
 * </p>
 *
 * @author liuailan
 * @since 2022-01-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AbilityType implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自增主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 能力类型名称
     */
    private String name;

    /**
     * 所属科目id
     */
    private Integer subjectId;

    private Integer parentAbilityTypeId;

    /**
     * 排序值
     */
    private Integer orders;

    /**
     * 状态,0:未启用;1:启用
     */
    private Boolean status;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改人
     */
    private String modifiedBy;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer updateTime;


}
