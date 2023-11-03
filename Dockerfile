FROM ubuntu:22.04

LABEL maintainer="jannetta.steyn@newcastle.ac.uk"

ENV TZ=Europe/London
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apt-get update -y
RUN apt-get install -y openjdk-17-jre wget

WORKDIR /workdir
COPY ./target/FIRM2.jar .
RUN mkdir -p /data/inputs /data/outputs

copy run.sh .

CMD ["/bin/bash", "run.sh"]
