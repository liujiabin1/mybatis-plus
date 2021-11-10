package com.riverstar.component.common;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.riverstar.config.LoggingReInitializer;
import com.riverstar.tool.JsonTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.scope.refresh.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Author: Hardy
 * Date:   2019/6/6
 * Description:
 * - 监听配置文件变化
 **/
@Component
public class ApolloChangeListener {

    private static final Logger log = LoggerFactory.getLogger(ApolloChangeListener.class);

    @Autowired
    private RefreshScope refreshScope;
    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    @ApolloConfigChangeListener("application.yml")
    public void onChange(ConfigChangeEvent changeEvent) {
        boolean isReinitializeLog = false;
        StringBuilder sb = new StringBuilder();

        for (String changeKey : changeEvent.changedKeys()) {
            if (changeKey.contains("log.file")) isReinitializeLog = true;

            sb.append(changeKey)
                    .append("=")
                    .append(JsonTool.toJsonExclude(changeEvent.getChange(changeKey), "propertyName", "namespace", "changeType"))
                    .append(";");
        }
        log.info("Apollo change listener change [{}]", sb.toString());

        // 重新吃初始化日志
        if (isReinitializeLog) LoggingReInitializer.reinitialize(configurableApplicationContext.getEnvironment());

        configurableApplicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
        refreshScope.refreshAll();
    }
}
