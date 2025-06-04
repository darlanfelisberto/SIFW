FROM ubuntu:latest
LABEL authors="darlan"

USER root

WORKDIR /opt/sifw

RUN pwd
RUN ls -la

COPY . /tmp/sifw/git/SIFW

RUN apt-get update
RUN cd /tmp/sifw/git/SIFW/docker_conf && \
     ./install.sh -o l && \
     ./build_deploy.sh

#CMD /opt/sifw/wildfly/bin/standalone.sh

EXPOSE 8080
EXPOSE 9990
