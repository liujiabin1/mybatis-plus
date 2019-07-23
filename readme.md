# zrb-demo 模板框架项目

### 目录介绍
```
- com.zrb
    - client 调用其他服务
        - BaseClient    所有client的基类, 提供通用的解析、判断
        - TestClient    基于okhttp做的封装, 学习了解下方资料
    - component 组件包
        - common 公共组件
            - ApolloChangeListener  监听applo实时推送的配置变化, 刷新Spring Bean  
            - GlobalEnv             注入的启动环境标识
            - WebLogAspect          统一记录Http进出日志
        - database 多数据源组件
            - DataSourceDynamicRouter   数据源路由器
            - DataSourceRouter          数据源切换注解, 支持方法注解、类注解
            - DataSourceRouterAspect    数据源切换切面
            - DataSourceType            数据源映射的枚举
        - srv 业务组件
            - RabbitSender  同步MQ发送
            - Receiver      MQ接收和处理 (注: 方法名为 message, 原因请看RabbitConfig)
            - TaskStarter   自启任务启动类, 配合@Async使用, 进行常驻任务启动
    - config
        - ApolloEvnInitializer  启动时设置apollo的环境变量
        - DataSourceConfig      启动注入多数据源, 自动注入spring.database.configs的数据源配置, 了解Hikari请看资料
        - LoggingReInitializer  日志重新重加载, 日志初始在拉取apollo配置之前, 导致日志配置无效, 所以需要拉取配置后重载
        - RabbitConfig          RabbitMQ配置类, 自动创建队列、配置接收者
        - SwaggerConfig         自动生成Controller API生成, 主要用于本地调试, 正式请写Yapi
        - WebMvcConfig          对JsonMessageConverter做了增强, 特殊除了BigDecimal
    - constant 常量包, 主要包括静态常量 枚举
        - common
            - RabbitConst   Rabbit枚举配置 
    - controller 接口
        - BaseController    所有业务接口必须继承的基类, 主要用于异常时，统一拦截处理
        - AliveController   探活接口, 和运维大大约定的 项目启动探活 http://domain/alive
        - TestController    用户测试接口
    - entity DB映射的实体
        - BaseEntity    所有实体的基类, 强制要求继承 (注: 新表必须有:time、upTime, 所以BaseEntity可以仅保留id, 同时抽取time、upTime为一个二级父类, 以便兼容历史表)
        - User          测试表，多数据源请分2级目录, 便于理解
        例:
        - trade         
            xxx
        - puser         
            xxx
    - exception
        - ErrCode       异常码枚举
        - SrvException  具体业务具体重命名 
    - mapper 数据库持久层
        - BaseMapper    所有Mapper的基类, 定义了一些基础方法
        - UserMapper    测试表，多数据源请分2级目录, 同entity一一对应
    - model 业务中的除了实体、常量的其他类定义, 最好根据业务、功能等作出不同力度划分，防止堆积
        - base
            - PageRequest   接收分页的信息的基类
            - RestPage      返回分析数据的包装
            - RestResponse  统一返回数据的格式
        - client
            - BaseClientRequest client的公用基类, 主要提供通用的方法
            - BaseClientResp    公用返回值模板, 便于统一解析和判断
    - service 业务层偏向于含义功能, 如逻辑过于复杂, 可以抽一个service同级目录, 为service提供底层支持, service组装调用
        - ...
    - tool 静态工具包
        - LogTool 提供关键日志打印alarm.log
```
注: mybatis 注解使用 
```
    #{}: 自动类型转换, 不需要使用@Param [ep: string 转换'xx']
    ${}: 不转换直接赋值, 必须需要使用@Param [ep: string 则不会 xx] 
```

### 环境
- `dev` (本地) ：本地文件，开发调试
- `test`(测试) ：apollo fat环境
- `prev`(稳定) ：apollo uat环境
- `prod`(线上) ：apollo pro环境
    
注: apollo支持配置热更新 <br/>
注: 如需了解具体如何根据目录进行环境区分，请了解`pom.xml` `<profiles>`标签


### 日志
```
resouces
    - common
        logback-spring.xml      主日志配置 info、error、alarm
        ch-logback-debug.xml    子日志模块, 用于调试, 支持试试线上实时开关 log.file.debug-enabled=true/false
        ch-logback-json.xml     子日志模块, 用于运维系统, 统一日志采集
```

### 启动
本地启动:<br/>
`sh run.sh`<br/>
默认端口号: 8000, 并开启jvm远程调试模式 (支持本地debug测试环境哦, 前提网通)

测试apollo启动:<br/>
`mvn spring-boot:run -Ptest`

### 资料:
- [ 连接池`Hikari`详细介绍和配置 ](https://github.com/brettwooldridge/HikariCP#essentials)
- [ Apollo配置中心 ](http://apollo-portal.zhenrongbao.com/config.html?#/appid=zrb-demo)
- [ idea配置jvm远程调试配置(一) ](https://stackoverflow.com/questions/21114066/attach-intellij-idea-debugger-to-a-running-java-process)
- [ idea配置jvm远程调试配置(二) ](https://juejin.im/entry/5c1c8cf45188257c30460369)
- [ okhttp熟悉了解 ](https://github.com/square/okhttp)


### 常用插件
- `Lombok plugin` idea解析lombok注解插件 (必需)
- `Maven Helper` maven依赖分析插件
- `Translation`   在线翻译插件
- `Java Bean To Json Tool` java bean转json或json schema
- `BashSupport` shell高亮

[ 官方插件仓库 ](https://plugins.jetbrains.com/idea)


有疑问请联系（Hardy、Luzhiyuan）, 欢迎提供更有优质模块替换方案 genghaizhou@zhenrongbao.com、luzhiyuan@zhenrongbao.com