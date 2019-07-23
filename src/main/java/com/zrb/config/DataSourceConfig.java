package com.zrb.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zrb.component.database.DataSourceDynamicRouter;
import com.zrb.component.database.DataSourceType;
import lombok.Data;
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
@Data
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
}
