FROM registry.cn-shanghai.aliyuncs.com/bianxj/centos-jdk:7.3-1.8-1
MAINTAINER BIANXJ
COPY build/libs/*.war  /opt
