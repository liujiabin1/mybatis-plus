package com.zrb.component.srv;


import com.zrb.service.TestService;
import com.zrb.tool.LogTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Author: Hardy
 * Date:   2019/7/12
 * Description:
 **/
@Component
public class TaskStarter {

    @Autowired
    private TestService testService;

    @PostConstruct
    public void start() {
        LogTool.alarm("------------------------------ 自启任务 启动 ------------------------------");

        testService.testAsync();

        LogTool.alarm("------------------------------ 自启任务 结束 ------------------------------");
    }
}
