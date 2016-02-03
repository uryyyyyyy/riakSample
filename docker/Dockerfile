FROM centos:7
MAINTAINER uryyyyyyy <koki305@gmail.com>
RUN yum update -y && yum clean all
RUN yum install tcl gcc wget make vim net-tools -y

RUN curl -s https://packagecloud.io/install/repositories/basho/riak/script.rpm.sh | bash
RUN yum install riak-2.1.1-1.el7.centos.x86_64 -y
