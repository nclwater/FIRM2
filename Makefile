#PWD=/media/jannetta/WORKDRIVE/DATA/GitHub_Repositories/nclwater/FIRM2

PWD=$(shell pwd)

run:
	docker run -d --rm -v "${PWD}/data:/data/" --name FIRM2 nclwater/firm2:0.001

build:
	mvn package
	docker build -t nclwater/firm2:0.001 .

save:
	docker save -o firm2.tar nclwater/firm2:0.001
	gzip firm2.tar

stop: 
	docker stop FIRM2
