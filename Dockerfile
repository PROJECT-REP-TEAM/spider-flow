FROM java:8

RUN mkdir -p /spider-flow

WORKDIR /spider-flow

EXPOSE 8188

ADD ./spider-flow-web/target/spider-flow.jar ./

CMD sleep 30;java -Djava.security.egd=file:/dev/./urandom -jar spider-flow.jar --spring.profiles.active=local --selenium.driver.chrome=/usr/bin/chromedriver --logging.level.org.spiderflow=INFO --spring.datasource.url=jdbc:mysql://172.168.16.101:8013/spiderflow?useSSL=false