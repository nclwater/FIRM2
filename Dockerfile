FROM ubuntu:22.04

LABEL maintainer="jannetta.steyn@newcastle.ac.uk"

ENV APP_PLATFORM="docker"

ENV TZ=Europe/London
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apt-get update -y
RUN apt-get install -y openjdk-17-jre wget

WORKDIR /workdir/target
COPY ./target/FIRM2.jar .
COPY ./.firm2.properties .
COPY ./logging.properties .

WORKDIR /workdir
RUN mkdir -p /data/inputs /data/outputs

COPY run.sh .

CMD ["/bin/bash", "run.sh"]
