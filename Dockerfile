FROM ubuntu:latest
LABEL authors="darlan"

USER root

WORKDIR /opt/sifw

COPY docker_conf/ /tmp/docker_conf

RUN apt-get update
RUN cd /tmp/docker_conf/ && \
     ./install.sh -o l

