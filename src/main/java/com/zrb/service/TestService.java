package com.zrb.service;

import com.riverstar.tool.JsonTool;
import com.riverstar.tool.SleepTool;
import com.zrb.model.message.TestMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Author: Hardy
 * Date:   2019/7/22
 * Description:
 **/
@Service
public class TestService {

    private static final Logger log = LoggerFactory.getLogger(TestService.class);

    public void test(TestMessage message) {
        log.info("handle doing message {}", JsonTool.toJson(message));
    }

    @Async
    public void testAsync() {
        while (true) {
            SleepTool.sleep(60000);
            log.info("handle doing testAsync() ...");
        }
    }
}
