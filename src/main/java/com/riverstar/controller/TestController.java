package com.riverstar.controller;



import com.riverstar.mapper.UserMapper;
import com.riverstar.model.base.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * SpringBootTest
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
    private UserMapper userMapper;

    @Autowired
    private TestAutoGenerate testAutoGenerate;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public RestResponse test() {
        return new RestResponse<>().data(userMapper.selectById(1));
    }


    /**
     * ToDo 1.代码生成器
     * @return
     */
    @RequestMapping(value = "/generate", method = RequestMethod.GET)
    public RestResponse generate() {
        testAutoGenerate.autoGenerate();
        return new RestResponse<>().data("success");
    }

}
