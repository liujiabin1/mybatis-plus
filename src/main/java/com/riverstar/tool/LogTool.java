package com.riverstar.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: Hardy
 * Date:   2019/4/28
 * Description:
 * - 特殊日志统一打印
 **/
public class LogTool {
    private static final Logger log = LoggerFactory.getLogger("alarm");

    public static void alarm(String msg) {
        log.info(msg);
    }

    public static void alarm(String template, Object... objs) {
        log.info(template, objs);
    }
}
