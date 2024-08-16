## FIRM2
https://hackmd.io/@jannettasteyn/FIRM2

### General Description
This program starts by checking for a file name `.firm2.properties` in the same
directory as the `.jar` file of the program. If the file
does not exist it is created with default values. If the file does exist the 
program checks that all the required properties are in the file and if not 
the missing properties are added with default values or, if environment 
variables exist, it
will use the value from the environment value. Default values are hard coded in
the `SystemProperties` class. This means that if no `.firm2.properties` file 
exists and no environment variables are set the model will still run, and it 
will also create the `.firm2.properties` file which can then be used as a 
template for future configuration.

The hard coded default properties are the following:

|parameter|default| description                                                                                                                  |
|---|---|------------------------------------------------------------------------------------------------------------------------------|
|TOROIDAL|false| True if the model is toroidal (toroidal not yet implemented)                                                                 |
|TICKS|0| If 0 the model will run indefinitely, otherwise the model will run for the number of ticks this value is set to              |
|VISUALISE|TRUE| This is mostly for development and debugging purposes as this framework is not currently aimed at visualisation of the model |
|CELL_SIZE|5|If visualisation is true, each cell will be visualised as a square of this many pixels|
|CHANCE|50|This is a variable that can be used for anything in the implementation of the framework|
|APPLICATION_TITLE|FIRM2|This is the title of the application that will be displayed if visualisation is used|
|INPUT_DATA|/data/inputs/|This is the directory where input files can be found|
|OUTPUT_DATA|/data/outputs/|This is the directory where outputs, including log files are written to|
|TERRAIN_DATA|terrain.json|Terrain data|
|ROADS_DATA|roads.json|The roads network|
|BUILDINGS_DATA|buildings.json|Buildings data|
|DEFENCES_DATA|defences.json|Defences data|
|MODEL_PARAMETERS|globals.json|This file contains default values - should this go into `.firm2.properties`?|
|VEHICLES_DATA|vehicles.json|Vehicle data|
|SLOWDOWN|0|Mostly for visualisation purposes, this value (in milliseconds) can be used to slow the model down|
|TIME_STAMP|1719874800|The Unix timestamp for when the model will start running. Times in the timelin use this as an epoch|
|TICK_TIME_VALUE|60|The number of seconds in a tick|
|OCEAN_DEPTH|4|The depth of the water for cells that are marked as ocean. This means that the land elevation for the cell will be the negative of this number|
|RUN_ON_STARTUP|TRUE|True if the model should start running as soon as the program is started. Mostly for visualisation purposes as there is a button to start and stop the model.|

### Directory Structure
uk.ac.ncl.nclwater.firm2

- AgentBasedModelFramework
- DAFNITest
- examples
- firm2
  - controller
  - model
  - Firm2.java
  - Txt2Json.java
- Main.java


### Agents

- Terrain
- Water
- Defences
- Buildings
- Vehicles
- 
### Networks
- Roads




