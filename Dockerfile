FROM ubuntu:22.04

LABEL maintainer="jannetta.steyn@newcastle.ac.uk"

ENV TZ=Europe/London
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apt-get update -y openjdk-20-jre-headless wget

WORKDIR /workdir
COPY model.jar .

copy run.sh

CMD ["/bin/bash", "run.sh"]
