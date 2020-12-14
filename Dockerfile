FROM centos7-mini2:latest

RUN rm -r /app/spider-flow_8088/*

WORKDIR /app/spider-flow_8088

EXPOSE 8088

ADD ./spider-flow-web/target/spider-flow.jar /app/spider-flow_8088

CMD sleep 30;cd /app/spider-flow_8088; java -Djava.security.egd=file:/dev/./urandom -jar spider-flow.jar --spring.profiles.active=local --selenium.driver.chrome=/usr/bin/chromedriver --logging.level.org.spiderflow=INFO --spring.datasource.url=jdbc:mysql://172.168.16.101:8013/spiderflow?useSSL=false