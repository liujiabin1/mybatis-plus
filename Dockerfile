FROM bj-riverstar-pro-acr-registry-vpc.cn-beijing.cr.aliyuncs.com/riverstar/rs-java8:v1.0
MAINTAINER zhaochuanchuan@yichongyisheng.com
USER root
RUN mkdir -p /home/work/app /home/work/app/log /home/work/app/photo
#进容器后的目录
WORKDIR /home/work/app
ADD silkworm-controller.jar /home/work/app/silkworm-controller.jar
#修改时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
#启动参数
ENV PARAMS=""

EXPOSE 8080
#启动时运行tomcat
ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /home/work/app/silkworm-controller.jar $PARAMS"]
