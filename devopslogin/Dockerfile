#FROM FROM openjdk:11
#
#VOLUME /tmp
#
#ADD /target/dal.war /usr/local/tomcat/webapps/dal.war
#
#CMD ["catalina.sh", "run"]

FROM openjdk:11-slim
RUN useradd -ms /bin/bash spring
USER spring
WORKDIR /home/spring
COPY target/*.war dal.war
ENTRYPOINT ["java", "-jar", "dal.war"]
