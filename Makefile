#PWD=/media/jannetta/WORKDRIVE/DATA/GitHub_Repositories/nclwater/FIRM2

PWD = $(shell pwd)
MVN = mvn
JAVA = java
DOCKER = docker
ZIP = gzip

save_target_root = firm2
save_target_tar = $(save_target_root).tar
save_target_zip = $(save_target_tar).gz

image_version = 0.001
image_tag = nclwater/$(save_target_root):$(image_version)
image_name = $(shell echo $(save_target_root) | tr '[a-z]' '[A-Z]')

mvn_target_dir = target

compile:
	$(MVN) package

run:
	$(DOCKER) run -d --rm -v "${PWD}/data:/data" --name $(image_name) $(image_tag)

build: compile
	$(DOCKER) build -t $(image_tag) .

cleanmvn:
	$(MVN) clean

clean: cleanmvn

cleanall: cleanmvn
	$(RM) $(save_target_tar) $(save_target_zip)

save: save_target_zip
save_target: build
	$(DOCKER) save -o $(save_target_tar) $(image_tag)
	$(ZIP) $(save_target_tar)

stop: 
	$(DOCKER) stop $(image_name)

runGUI:
	$(JAVA) -cp $(mvn_target_dir)/$(image_name).jar uk.ac.ncl.nclwater.firm2.firm2.Firm2

DAFNITest:
	$(JAVA) -cp $(mvn_target_dir)/$(image_name).jar uk.ac.ncl.nclwater.firm2.DAFNITest.DAFNITest

Conway:
	$(JAVA) -cp $(mvn_target_dir)/$(image_name).jar uk.ac.ncl.nclwater.firm2.examples.conway.Conway
