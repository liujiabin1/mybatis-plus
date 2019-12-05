package com.zrb.controller;


import com.github.pagehelper.PageHelper;
import com.zrb.client.TestClient;
import com.zrb.component.database.DataSourceRouter;
import com.zrb.component.database.DataSourceType;
import com.zrb.entity.User;
import com.zrb.mapper.UserMapper;
import com.zrb.model.client.TestServerRequest;
import com.zrb.model.client.TestServerResp;
import com.zrb.tool.JsonTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.zrb.model.base.RestResponse;

import java.util.HashMap;
import java.util.List;

/**
 * Author: Hardy
 * Date:   2019/4/19
 * Description:
 **/
@ConditionalOnExpression("'${profile-env}'=='dev' || '${profile-env}'=='test'")
@RestController
public class TestController extends BaseController {

    public static final Logger log = LoggerFactory.getLogger(TestController.class);

    @Value("${profile-env}")
    private String env;

    @Autowired
    private TestClient testClient;
    @Autowired
    private UserMapper userMapper;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public RestResponse test() {
        return new RestResponse<>().data(userMapper.findOne(1));
    }

    @Transactional
    @DataSourceRouter(DataSourceType.SLAVE)
    @RequestMapping(value = "/test/t", method = RequestMethod.GET)
    public RestResponse testT() {
        User user = userMapper.findOne(1);
        user.setAge(user.getAge() + 1);
        userMapper.update(user);
        return new RestResponse<>();
    }

    @Transactional
    @DataSourceRouter(DataSourceType.SLAVE)
    @RequestMapping(value = "/test/page", method = RequestMethod.GET)
    public RestResponse testPage() {
        PageHelper.startPage(1, 1);
        List<User> users = userMapper.findAll();
        return new RestResponse<>().data(users);
    }

    @RequestMapping(value = "/test/sql", method = RequestMethod.GET)
    public RestResponse testSql() {
        log.info("---------------------------------------------------");

        User user = userMapper.findOne(1);
        log.info("[base::findOne] result: {}", JsonTool.toJson(user));

        // 写sql 下换线字段 (太灵活 不推荐使用)
        user = userMapper.findOneBySql("id=1");
        log.info("[base::findOneBySql] result: {}", JsonTool.toJson(user));

        // 写java 实体类驼峰字段 (太灵活 不推荐使用)
        user = userMapper.findOneByCond(new HashMap<String, Object>() {{
            put("id", 1);
        }});
        log.info("[base::findOneByCond] result: {}", JsonTool.toJson(user));

        // (太灵活 不推荐使用)
        List<User> users = userMapper.findListBySql("id in (1,2)");
        log.info("[base::findListBySql] result: {}", JsonTool.toJson(users));

        // (太灵活 不推荐使用)
        users = userMapper.findListByCond(new HashMap<String, Object>() {{
            put("age", 1);
        }});
        log.info("[base::findListByCond] result: {}", JsonTool.toJson(users));

        // 推荐使用 方便定制
        user = userMapper.findByAgeGte(10);
        log.info("[自定义::findByAgeGte] result: {}", JsonTool.toJson(user));

        log.info("---------------------------------------------------");
        return new RestResponse<>().data(env);
    }

    @RequestMapping(value = "/test/client", method = RequestMethod.GET)
    public RestResponse testClient() {
        TestServerRequest param = new TestServerRequest();
        param.setName("小明");
        param.setAge(100);
        TestServerResp resp = testClient.test(param);
        return new RestResponse<>().data(resp);
    }

    @RequestMapping(value = "/test/server", method = RequestMethod.POST)
    public RestResponse testServer(@RequestBody TestServerRequest param) {
        param.setName(param.getName() + "-1");
        param.setAge(999);
        return new RestResponse<>().data(param);
    }
}
