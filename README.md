# FIRM2
A reimplementation of the agent based flood incident management (https://github.com/nclwater/FIRM)
model in Java.

## About

This project is a reimplementation of https://github.com/nclwater/FIRM
in Java in preparation of running the model insde a docker on the DAFNI
environment

### Project Team
**PI:** Richard Dawson  
**RSEs:**

- Jannetta Steyn (jannetta.steyn@newcastle.ac.uk)
- Robin Wardle (robin.wardle@newcastle.ac.uk)

### RSE Contact
- Jannetta Steyn (jannetta.steyn@newcastle.ac.uk)
- Robin Wardle (robin.wardle@newcastle.ac.uk)


## Built With

[Java JDK 17](https://www.oracle.com/uk/java/technologies/downloads/)  
[Apache Maven](https://maven.apache.org/)  
[Docker](https://www.docker.com/)  
[Unix `make`](https://man7.org/linux/man-pages/man1/make.1.html)


## Getting Started

### Prerequisites

- Any platform with a Java Runtime Engine (JRE) suitable to run JDK 17 compiled Java code.
- An up-to-date installation of [Docker](https://www.docker.com/).
- [Apache Maven](https://maven.apache.org/) for building the Docker image. Apache Maven installation instructions can be found at https://maven.apache.org/install.html

### Installation

**Building the docker image**  
There is a Makefile with commands for building, running, saving and stopping the container:

- `make build`: The build command compiles the code using maven and creates a jar file. It then builds a docker image called `nclwater/firm2:x.xxx`. Where x.xxx is the version. There is a `run.sh` file which will be copied into the image and will be executed when the container runs.
- `make run`: The run command will create and run a container called `FIRM2`.
- `make save`: The save command will save the container to a tar file and then gzip it. A file called firm2.tar.gz should be created that can be uploaded to **DAFNI**.
- `make stop`: The stop command will stop a docker container with the name `FIRM2`.

The program does not need installation as such. It is packaged as a .jar file which can be copied to
a directory and executed from there. The program needs configuration for where to find data files.

### Running Locally

Important: the following VM option have to be added to the command line:
`--add-opens java.desktop/java.awt=ALL-UNNAMED --add-opens java.desktop/java.awt.color=ALL-UNNAMED`

The program is packaged in a `.jar` file and can be run either from the command line or by double-clicking on the `.jar` file in a file system GUI, as long as the JRE is installed on the system. If a JRE is required, find the download and installation instructions online.

On Linux, assuming a suitable runtime environment is installed, one can run the program from the command line using the `run.sh` script. For testing the program `DAFNITest` is executed which creates a file in `/data/inputs` and another in `/data/outputs`. To run the actual Firm2 model line 2, in the `run.sh` file should be uncommented and line 3 should be commented.  

To run the model inside a docker container, use `make run` as explained above.
### Running Tests

How to run tests on your local system.

## Deployment

### Local

The application is intended for dockerisation to run on **DAFNI**. However, it doesn't have to be. 

### Production

`FIRM2.jar` is a java executable that is packaged in a docker container for upload into **DAFNI**. Any updates to the main branch of the repository should  (eventually) lead to a **GitHub** action being executed that will:

1. use **Maven** to compile and create the `FIRM2.jar` file
2. create, save and zip a docker image
3. upload the image to **DAFNI**

## Usage

Any links to production environment, video demos and screenshots.

## Roadmap

- [x] Select development environment
- [x] Develop basic agent modelling framework
- [x] Create docker environment for model to run on **DAFNI**
- [ ] Implement FIRM model

## Contributing

### Main Branch
Protected and can only be pushed to via pull requests. Should be considered stable and a representation of production code.

### Dev Branch
Should be considered fragile, code should compile and run but features may be prone to errors.

### Feature Branches
A branch per feature being worked on.



## License

## Citation

Please cite the associated papers for this work if you use this code:

```
@article{xxx2023paper,
  title={Title},
  author={Author},
  journal={arXiv},
  year={2023}
}
```


## Acknowledgements

