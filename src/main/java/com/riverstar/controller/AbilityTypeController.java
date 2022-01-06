package com.riverstar.controller;


import com.riverstar.entity.AbilityType;
import com.riverstar.model.base.RestResponse;
import com.riverstar.service.AbilityTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 能力类型表 前端控制器
 * </p>
 *
 * @author liuailan
 * @since 2022-01-06
 */
@RestController
@RequestMapping("/ability-type")
public class AbilityTypeController {

    @Autowired
    private AbilityTypeService abilityTypeService;

    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public RestResponse insert() {
        AbilityType abilityType = new AbilityType();
        abilityType.setOrders(1).setCreator("liuailan").setModifiedBy("liuailan")
                .setName("test").setParentAbilityTypeId(1)
                .setStatus(true).setSubjectId(1);
        abilityTypeService.save(abilityType);
        abilityTypeService.list().forEach(System.out::println);
        return new RestResponse<>().data("success");
    }


}

