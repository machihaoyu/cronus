FROM registry.cn-hangzhou.aliyuncs.com/fjs/centos-jdk:7.3-1.8-1
MAINTAINER fjs
COPY build/libs/*.war  /opt
