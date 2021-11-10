package com.zrb.controller;

import com.zrb.component.common.GlobalEnv;
import com.riverstar.tool.MailTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Hardy
 * Date:   2018/8/20 14:39
 * Description:
 **/
@RestController
public class AliveController {

    @Autowired
    private GlobalEnv globalEnv;

    @RequestMapping(value = "/alive", method = RequestMethod.GET)
    public Object alive() {
        return "Server is alive " + globalEnv.env;
    }

    @RequestMapping(value = "/mail", method = RequestMethod.GET)
    public Object testMail() {
        return MailTool.send("xxx@zhenrongbao.com", "", "zrb-demo测试邮件", "不用紧张仅仅是个测试邮件 (￣.￣)");
    }
}
