# Flood Infrastructure Resilience Model v2 (FIRM2)
A reimplementation of the agent-based flood incident management (https://github.com/nclwater/FIRM) model in Java for DAFNI (https://www.dafni.ac.uk/).

## About
The FIRM2 project re-implements the NetLogo-based [FIRM](https://github.com/nclwater/FIRM) agent-based flood simulation model in Java, and embeds it in a Docker image for running on [DAFNI](https://www.dafni.ac.uk/).

The project is funded by the UKRI ["Building a secure and Resilient World strategic theme](https://www.ukri.org/who-we-are/our-vision-and-strategy/ukri-strategic-themes/building-a-secure-and-resilient-world/). Links:
- [DAFNI BSRW projects](https://www.dafni.ac.uk/bsrw-projects/)
- [DAFNI FIRM 2](https://www.dafni.ac.uk/firm-coe/)
- [FIRM insights](https://www.dafni.ac.uk/insights/firm-flood-resilience-simulation-model-on-dafni/)

DAFNI resources:
- [Towyn example data set](https://facility.secure.dafni.rl.ac.uk/data/details?dataset_id=cd7519cf-b244-4c81-9cd4-999518b76853&version_id=67861bde-b188-4fc4-b0a1-364480ca5dff&metadata_id=ec4bf3d4-8504-4649-a8ae-0825cf83e04f)
- [FIRM model](https://facility.secure.dafni.rl.ac.uk/models/details?version_id=27d95afa-f655-46db-8b18-4d0c4aa7aff0)
- [FIRM example workflow](https://facility.secure.dafni.rl.ac.uk/workflows/details?id=d06bf825-f83f-4781-8627-0fecea09d597)

Contacts:
- Project: Richard Dawson (PI) <[richard.dawson@newcastle.ac.uk](mailto:richard.dawson%40newcastle.ac.uk)>
- RSE: Jannetta Steyn <[jannetta.steyn@newcastle.ac.uk](mailto:jannetta.steyn%40newcastle.ac.uk)>

### Project Team
#### School of Engineering
- Richard Dawson <[richard.dawson@newcastle.ac.uk](mailto:richard.dawson%40newcastle.ac.uk)>
- Olivia Butters <[olivia.butters@newcastle.ac.uk](mailto:olivia.butters@newcastle.ac.uk)>
#### Research Software Engineering
- Jannetta Steyn <[jannetta.steyn@newcastle.ac.uk](mailto:jannetta.steyn%40newcastle.ac.uk)>
- Robin Wardle <[robin.wardle@newcastle.ac.uk](mailto:robin.wardle%40newcastle.ac.uk)>

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

For running on DAFNi you will need a [DAFNI account[(https://www.dafni.ac.uk/dafnilogin/).

### Installation
#### Building the docker image
There is a `Makefile` with directives for building, running, saving and stopping the container:

- `make build`: The `build` command compiles the code using Maven and creates a `.jar` file. It then builds a Docker image called `nclwater/firm2:x.xxx`, where x.xxx is the version. There is a `run.sh` file which will be copied into the image and will be executed when the container runs.
- `make run`: The `run` command will create and run a container called `FIRM2`.
- `make save`: The `save` command will save the container to a `.tar` file and then `gzip` it. A file called `firm2.tar.gz` should be created that can be uploaded to DAFNI.
- `make stop`: The `stop` command will stop a docker container with the name `FIRM2`.

The program does not need installation as such. It is packaged as a `.jar` file with all dependencies included, which can be copied to a directory and executed from there.

The program's configuration  files need to be modified to establish paths to relevant data files. For both running locally and within a Docker container, the two configurations files `.firm2.properties` and `logger.properties` have to be in the user directory. That is the same directory from which the program is being run. Edit these two files with the appropriate values before running or Dockerising the application.

### Running Locally
Important: the following VM option must be added to the command line:
```
--add-opens java.desktop/java.awt=ALL-UNNAMED --add-opens java.desktop/java.awt.color=ALL-UNNAMED
```

The program is packaged in a `.jar` file and can be run either from the command line or by double-clicking on the `.jar` file in a file system GUI, as long as the JRE is installed on the system. If a JRE is required, the download and installation instructions can be found [on Oracle's website](https://www.oracle.com/uk/java/technologies/downloads/)  .

On Linux, assuming a suitable runtime environment is installed, one can run the program from the command line using the `run.sh` script. For testing the program `DAFNITest` is executed which creates a file in `/data/inputs` and another in `/data/outputs`. To run the actual Firm2 model line 2, in the `run.sh` file should be uncommented and line 3 should be commented.  

To run the model inside a Docker container, use `make run` as explained above.

### Running Tests
TBC

## Deployment
### Local
The application is intended for Dockerisation to run on DAFNI. However, it can be run locally using a JRE of the correct version.

### Production
`FIRM2.jar` is a java executable that is packaged in a docker container for upload into DAFNI. The creation in the GitHub repository of a new release will execute a GitHub Action that will:

1. use Maven to compile and create the `FIRM2.jar` file
2. create, save and zip a docker image
3. upload the image along with the DAFNI model definition file to DAFNI using the [DAFNI model uploader](https://github.com/dafnifacility/dafni-model-uploader)

## Usage
The model can be configured with a number of parameters. Local builds read the file `.firm2.properties`, which DAFNI models read parameters from a parameter set defined in `model-definition.yml`. The configurable parameters are:

| Parameter Name | Display Name | Type | Description |
| --- | --- | --- | --- |
| TOROIDAL | Toroidal wrap | `boolean` | Defines the map boundary conditions. If `True`, connects the upper map edge to the lower map edge, and the left map edge to the right map edge. |
| TICKS | Ticks | `integer` | The number of simulation steps (ticks) to run. If this is zero, the simulation will run forever. |
| VISUALISE | Visualisation | `boolean` | Specifies whether the simulation should be run through the visualisation display. Not available on DAFNI. |
| CELL_SIZE | Cell size in metres | `integer` | The size of the simulation cell size. Smaller cell sizes result in a higher resolution map but take longer to simulate. |
| CHANCE | Chance | `float` | TBC |
| APPLICATION_TITLE | Application title | `string` | The name of the application to display in windows or logs. |
| INPUT_DATA | Input data | `string` | Full path to the simulation input data in the FIRM2 model. |
| OUTPUT_DATA | Output data | `string` | Full path to the simulation output data in the FIRM2 model. |
| TERRAIN_DATA | Terrain data filename | `string` | Name of the JSON file containing FIRM2 terrain data. On DAFNI this will be in a dataset referenced by the model. |
| ROADS_DATA | Road data filename | `string` | Name of the JSON file containing FIRM2 road data. On DAFNI this will be in a dataset referenced by the model. |
| ROAD_TYPES | Road types filename | `string` | Name of the JSON file containing FIRM2 road types data. On DAFNI this will be in a dataset referenced by the model. |
| BUILDINGS_DATA | Building data filename | `string` | Name of the JSON file containing FIRM2 building data. On DAFNI this will be in a dataset referenced by the model. |
| DEFENCES_DATA | Sea defences data filename | `string` | Name of the JSON file containing FIRM2 defences data. On DAFNI this will be in a dataset referenced by the model. |
| MODEL_PARAMETERS | Global parameters data filename | `string` | Name of the JSON file containing FIRM2 global parameters data. On DAFNI this will be in a dataset referenced by the model. |
| VEHICLES_DATA | Vehicle data filename | `string` | Name of the JSON file containing FIRM2 vehicle data. On DAFNI this will be in a dataset referenced by the model. |
| TIMELINE | Timeline data filename | `string` | Name of the JSON file containing a FIRM2 events timeline. On DAFNI this will be in a dataset referenced by the model. |
| VEHICLE_FLOOD_DEPTH | Vehicle flood threshold in metres | `number` | The depth of water at which inundated vehicles will be immobilised. |
| SLOWDONW | Slowdown | `float` | TBC |
| TIME_STAMP | Time stamp | `integer` | TBC |
| TICK_TIME_VALUE | Tick time value | `integer` | Duration of a simulation tick (step) time in seconds. |
| OCEAN_DEPTH | Ocean depth in metres | `integer` | Depth of the ocean in metres. |
| RUN_ON_STARTUP | Run on startup | `boolean` | Indicate whether the simulation should launch immediately on run, or whether user intervention is required. Not available on DAFNI. |


## Roadmap
- [x] Select development environment
- [x] Develop basic agent modelling framework
- [x] Create docker environment for model to run on DAFNI
- [x] Implement FIRM model

## Contributing
The main branch should be considered stable and a representation of production code. The main branch is protected and requires a reviewer. Please complete the submission form with details of the pull request including linked Issues (see below) and tests that have been performed.

Contributions should be linked to an Issue in the repository [issues list](https://github.com/NewcastleRSE/FIRM2/issues).

This repository does not use a "dev branch". Contributions must be produced in *feature branches* and you should follow the "branch early and often" maxim. Branches should generally contain a small enough contribution to the project that a branch's lifespan is measured in days, rather than weeks or longer. Branches should be named after the feature being added, preferably with the contributor's initials and ideally containing the Issue ID that is being addressed.

An exception to the branch-pull request workflow is minor changes to `README.md` files and other documentation to correct spelling and formatting errors and suchlike - these changes can be performed in the GitHub editor.

Further reading on [branches](https://nvie.com/posts/a-successful-git-branching-model/), and [versioning](https://semver.org/).

## License
Towyn dataset on DAFNI is CC BY-NC-SA.

This code is licenced under the terms of the GNU Affero General Public License as published by the Free Software Foundation. See `LICENCE.txt` and `COPYING.md` for further details.

## Citation
Please cite the associated papers for this work if you use this model.
- Dawson, R.J., Peppe, R. & Wang, M. An agent-based model for risk-based flood incident management. Nat Hazards 59, 167–189 (2011). https://doi.org/10.1007/s11069-011-9745-4

## Acknowledgements
This work was funded under the UKRI / STFC "Building a Secure and Resilient World" strategic theme, project reference [ST/Y003799/1](https://gtr.ukri.org/projects?ref=ST%2FY003799%2F1).
