package com.riverstar;

import com.riverstar.config.ApolloEnvInitializer;
import com.riverstar.config.LoggingReInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Author: Hardy
 * Date:   2019/7/22
 * Description:
 **/
@EnableAsync
//@MapperScan("mapper")
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.addInitializers(new ApolloEnvInitializer());
        application.addInitializers(new LoggingReInitializer());
        application.run(args);
    }

}