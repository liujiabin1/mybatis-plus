package com.riverstar.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.riverstar.entity.User;
import com.riverstar.model.base.RestResponse;
import com.riverstar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuailan
 * @since 2022-01-06
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public RestResponse insert() {
        User user = new User();
        user.setAge(1).setEmail("123@qq.com").setName("liuailan");
        userService.save(user);
        return new RestResponse<>().data("success");
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public RestResponse update() {
        User user = new User();
        user.setId(7).setEmail("456@.qq.com");
        userService.updateById(user);
        return new RestResponse<>().data("success");
    }

    //逻辑删除
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public RestResponse delete() {
        userService.removeById(7);
        return new RestResponse<>().data("success");
    }

    /**
     * 分页查询
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public RestResponse page() {
        // Step1：创建一个 Page 对象
       // Page<User> page = new Page<>();
         Page<User> page = new Page<>(1, 1);
        // Step2：调用 mybatis-plus 提供的分页查询方法
        userService.page(page, null);
        // Step3：获取分页数据
        System.out.println(page.getCurrent()); // 获取当前页
        System.out.println(page.getTotal()); // 获取总记录数
        System.out.println(page.getSize()); // 获取每页的条数
        System.out.println(page.getRecords()); // 获取每页数据的集合
        System.out.println(page.getPages()); // 获取总页数
        System.out.println(page.hasNext()); // 是否存在下一页
        System.out.println(page.hasPrevious()); // 是否存在上一页
        return new RestResponse<>().data("success");
    }

    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public RestResponse version(){
        User user = new User();
        user.setName("tom").setAge(20).setEmail("tom@163.com");
        userService.save(user);
        userService.list().forEach(System.out::println);
        user.setName("lal");
        userService.update(user, null);
        userService.list().forEach(System.out::println);
        return new RestResponse<>().data("success");
    }

    /**
     * 条件构造器
     */
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    public RestResponse query() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id","age","name")
                .eq("age",20)
                .like("name","qin");
        userService.list(queryWrapper).forEach(System.out::println);
        return new RestResponse<>().data("success");
    }

    /**
     * 条件构造器
     */
    @RequestMapping(value = "/lambdaquery", method = RequestMethod.GET)
    public RestResponse lambdaquery() {

        userService.lambdaQuery()
                .select(User::getAge,User::getName)
                .eq(User::getAge,20)
                .list().forEach(System.out::println);

        return new RestResponse<>().data("success");
    }


}

