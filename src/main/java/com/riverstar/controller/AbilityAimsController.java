package com.riverstar.controller;


import com.riverstar.entity.AbilityAims;
import com.riverstar.model.base.RestResponse;
import com.riverstar.service.AbilityAimsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 能力目标表 前端控制器
 * </p>
 *
 * @author liuailan
 * @since 2022-01-06
 */
@RestController
@RequestMapping("/ability-aims")
public class AbilityAimsController {

    @Autowired
    private AbilityAimsService abilityAimsService;

    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public RestResponse insert() {
        AbilityAims abilityAims = new AbilityAims();
        abilityAims.setAbilityTypeId(1).setCreator("liuailan").setModifiedBy("liuailan")
                .setName("test").setRemark("test")
                .setStatus(true).setSubjectId(1);
        abilityAimsService.save(abilityAims);
        abilityAimsService.list().forEach(System.out::println);
        return new RestResponse<>().data("success");
    }

}

