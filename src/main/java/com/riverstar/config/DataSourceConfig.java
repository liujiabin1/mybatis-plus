package com.riverstar.config;

import com.riverstar.component.database.DataSourceAdvisor;
import com.riverstar.component.database.DataSourceDynamicRouter;
import com.riverstar.component.database.DataSourceInterceptor;
import com.riverstar.component.database.DataSourceType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Hardy
 * Date:   2018/8/20 19:37
 * Description:
 **/
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceConfig {

    private Map<String, HikariConfig> configs = new HashMap<String, HikariConfig>();

    /**
     * 为了注解的可控性, 需要配置枚举DataSourceType <-> configs.keys
     *
     * @return DataSourceDynamicRouter 动态数据源
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        HashMap<Object, Object> sources = new HashMap<>();
        for (String name : configs.keySet()) {
            DataSourceType type = DataSourceType.valueOf(name.toUpperCase());
            HikariConfig hikariConfig = configs.get(name);
            hikariConfig.setPoolName("HikariPool[" + name + "]");
            sources.put(type, new HikariDataSource(hikariConfig));
        }

        DataSourceDynamicRouter sourceRouter = new DataSourceDynamicRouter();
        sourceRouter.setTargetDataSources(sources);
        sourceRouter.setDefaultTargetDataSource(sources.get(DataSourceType.MASTER));
        return sourceRouter;
    }

    @Bean
    public DataSourceAdvisor dataSourceAdvisor() {
        DataSourceInterceptor interceptor = new DataSourceInterceptor();
        return new DataSourceAdvisor(interceptor);
    }
}
