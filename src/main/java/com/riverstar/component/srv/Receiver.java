package com.riverstar.component.srv;

import com.riverstar.tool.SleepTool;
import com.riverstar.component.common.GlobalEnv;
import com.riverstar.constant.common.RabbitConst;
import com.riverstar.model.message.TestMessage;
import com.riverstar.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author: Hardy
 * Date:   2018/12/3
 * Description:
 **/
@Component
public class Receiver {

    private static final Logger log = LoggerFactory.getLogger(Receiver.class);

    @Autowired
    private GlobalEnv env;
    @Autowired
    private RabbitSender sender;

    @Autowired
    private TestService testService;

    // 触发打包
    public void message(TestMessage message) {
        RabbitConst rabbit = RabbitConst.TEST;

        // 业务方法需要捕获异常
        try {
            testService.test(message);
        } catch (Exception e) {
            // 根据实际情况进行 重试还是打日志丢弃
            SleepTool.sleep(1000);
            sender.sendAndRetry(rabbit, message);
        }
    }
}
