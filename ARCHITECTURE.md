## FIRM2
https://hackmd.io/@jannettasteyn/FIRM2

### General Description
This program starts by checking for a file name `.firm2.properties`. If the file
does not exist it is created with default values. If the file does exist the 
program checks that all the required properties are in the file and if not those
properties are added with default values or, if environment variables exist, it
will use the value from the environment value. Default values are hard coded in
the `SystemProperties` class. This means that if no `.firm2.properties` file 
exists and no environment variables are set the model will still run, and it 
will also create the `.firm2.properties` file which can then be used as a 
template for future configuration.


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

Each agent "lives" on a grid with a height and width specified in the 
.firm2.properties files which (for the moment) lives in the user's home 
directory.


