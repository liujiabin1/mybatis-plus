package com.riverstar.component.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Author: Hardy
 * Date:   2018/7/23 15:32
 * Description:
 **/
@Component
public class GlobalEnv {

    public final String env;

    @Autowired
    public GlobalEnv(@Value("${profile-env:dev}") String env) {
        this.env = env.toLowerCase();
    }

    public boolean isProd() {
        return "prod".equals(env);
    }
}
