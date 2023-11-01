## buildings.txt

**Format:** `%Easting  %Northing  %Building Type`

Basically provides the `[x,y]` coordinates and the building type.  
A building type of “0” `[number zero]` is a residential property.  The others relate to different building types from the national property database classification (see codes.txt).  
Apart from `>=1000` which are evacuation points.  They are given different numbers in case we want to look at their performance separately.

## Codes.txt
This is a table listing the type of non-residential building.  The code number corresponds to the building type number.
The codes are:

| Code | Non residential property type                    |
|---|--------------------------------------------------|
| 211 | SHOP AND PREMISES                              |
|212| STORE AND PREMISES                               |
|213| SUPERSTORE/HYPERSTORE AND PREMISES               |
|214| RETAIL WAREHOUSE AND PREMISES                    |
|215| SHOWROOM AND PREMISES                            |
|216| KIOSK AND PREMISES                               |
|217| MARKET OUTDOOR AND PREMISES                      |
|218| MARKET INDOOR AND PREMISES                       |
|221| GARAGE/VEHICLE REPAIR WORKSHOP AND PREMISES      |
|222| PETROL FILLING STATION AND PREMISES              |
|223| CAR SHOWROOM AND PREMISES                        |
|224| PLANT HIRE                                       |
|231| HAIRDRESSING SALON AND PREMISES                  |
|232| BETTING SHOP AND PREMISES                        |
|233| LAUNDERETTE AND PREMISES                         |
|234| PUBLIC HOUSE AND PREMISES/CLUB/CLUBHOUSE         |
|235| RESTAURANT AND PREMISES                          |
|236| CAFÉ AND PREMISES                                |
|237| POST OFFICE AND PREMISES                         |
|310| OFFICES AND PREMISES                             |
|311| HI TECH OFFICES                                  |
|320| BANK AND PREMISES                                |
|410| WAREHOUSE AND PREMISES                           |
|420| LAND USED FOR STORAGE AND PREMISES               |
|430| ROAD HAULAGE DEPOT AND PREMISES                  |
|511| HOTEL AND PREMISES                               |
|512| BOARDING HOUSE AND PREMISES/HOLIDAY UNIT         |
|513| CARAVAN PARK AND PREMISES                        |
|514| BEACH HUT                                        |
|515| SELF CATERING UNIT                               |
|516| HOSTEL AND PREMISES                              |
|517| BINGO HALL AND PREMISES                          |
|518| THEATRE/CINEMA AND PREMISES                      |
|521| SPORTS GROUND/PLAYING FIELD AND PREMISES         |
|522| GOLF COURSE AND PREMISES                         |
|523| SPORTS & LEISURE CENTRE AND PREMISES             |
|524|AMUSEMENT ARCADE/PARK AND PREMISES|
|525|FOOTBALL GROUND AND PREMISES|
|526|MOORING/WHARF/MARINA AND PREMISES|
|527|SWIMMING POOL AND PREMISES|
|610|SCHOOL/COLLEGE/UNIVERSITY/NURSERY AND PREMISES|
|620|SURGERY AND HEALTH CENTRE AND PREMISES|
|625|RESIDENTIAL HOME00|
|630|COMMUNITY CENTRE/HALLS AND PREMISES|
|640|LIBRARY AND PREMISES|
|650|POLICE STATION AND PREMISES|
|660|HOSPITAL AND PREMISES|
|670|MUSEUM AND PREMISES|
|680|LAW COURT AND PREMISES|
|690|CHURCH|
|810|WORKSHO0P AND PREMISES|
|820|FACTORY/WORKS AND PREMISES|
|830|QUARRY/TIP SITE/CONCRETE BATCHING AND PREMISES|
|840|SEWAGE TREATMENT WORKS AND PREMISES|
|910|CAR PARK/PREMISES|
|920|PUBLIC CONVENIENCE|
|930|CEMETERY AND CREMATORIUM AND PREMISES|
|940|BUS STATION AND PREMISES|
|950|DOCK HEREDITAMENT AND PREMISES|
|960|ELECTRICITY HEREDITAMENT AND PREMISES|

## preprocessed-buildings.txt
**Format:** `%longitude %latitude %building type %Nearest road link ID`
This is a processed file that will only be generated if the code can’t locate a file of this name.  It links each building to the ID of the nearest road link from roads.txt
When new flood evacuation sites are added to buildings.txt (or any other changes) this file needs to be deleted so it is correctly recalculated.

## Defences.txt
This file describes the coordinates of the flood defences.  Each defence has a name, and a series of coordinates that join up to form a polyline.  These defences fail as a unit (i.e. all points on the line are removed – see timeline.txt)
**Format:**
```
[x1, y1, “defence1”]
[x2, y2]
…
[xn, yn]
[x1, y1, “defence2”]
```
Etc.

## vehicles.txt
This describes the agent rules as a finite state machine
This agent produces vehicles that flow along one of the main roads that bypasses the town at the South.  Vehicles are produced at the point (taken from the preprocessed-buildings.txt file) and drive to another point on the file before leaving the domain (“exit”)

```
["transit eastbound" "A55 west" [
["A55 west" "0s 0s" "A55 east" 1]
["A55 east" "0s 0s" "exit" 1]
]]
```

This agent produces someone who ahs kids and a job.  They start at home (which is randomly assigned from the list of residential buildings) and then at 7:45 with a standard deviation (SD) of 5 minutes vehicles appear at their home and drive to a school.  They spend 5minutes, with SD=1minute at the school before 90% of them (0.9) head to a non-residential building that is not a shop, the remaining 10% (0.1) head to a randomly selected shop and then after 2hours (SD=1hour) head to work.  Work finishes at 5pm, SD=15m and people head home/shops/recreation non-residential building.  
The second part shows that when they achieve the order to evacuate (see timeline.txt).  In this case 90% respond within 5m (SD=1m) to head to one of the evacuation points, the others do not.  The final point is a return home after 4hours which is not normally used.
Hopefully that gives a sense of how it works?

```
["kids & work" "home 1" [
["home 1" "daily 7:45 5m" "school" 1]
["school" "5m 1m" "work" 0.9]
["school" "5m 1m" "some shop" 0.1]
["some shop" "2h 1h" "work" 1]
["work" "daily 17h 15m" "home 1" 0.75]
["work" "daily 17h 15m" "supermarket" 0.15]
["work" "daily 17h 15m" "home 2" 0.1]
["supermarket" "45m 10m" "home 1" 1]
["home 2" "daily 20h 2h" "some recreation" 0.1]
["some recreation" "3h 1h" "home 1" 1]

	["evacuate" "5m 1m" "some evacuation point" 0.9]
	["evacuate" "0s 0s" "resume" 0.1]
	["some evacuation point" "daily 4h 1h" "home 1" 1]
]]
```

## roads.txt
This is a series of polylines describing all the roads in the network taken from the Ordnance Survey Integrated Transport Network (ITN).  This data was processed in advance from the ITN.  It would be useful to develop a utility model that processes this data into an appropriate format for buildings, road network etc.
The first three numbers are the unique ID of the road link (should correspond to the preprocessed-buildings.txt ID), the ID of the staring node and the ID of the end node of the road link.  
I think the next number is the length, then the road type (which dictates agent speed – this is hard coded in the model at the moment I think).
The final set of pairs of numbers give the [x y] coordinates of the polyline.

```[“4000000013081056” “4000000012843503” “4000000012843508” 17810 “Single Carriageway” [[301510000 374871000] [301512000 374868000] [301515000 374859000] [301517000 374856000] [301517803 374855219] ]]```

## terrain.txt
This file describes the elevation of the domain.  It is a ASCII Raster format – a grid where each value is the elevation of the land (in metres) for that grid point.   ncols x nrows is the size of the matrix, cellsize is the dimension of the grid square (in meters), xllcorner and yllcorner are the lower left coordinates, NODATA_value is the value for areas of the matrix that are then converted to sea level in the model simulation.

```
[%elevation %elevation …  ncols
…  nrows   ….
…%elevation %elevation]
```

## timeline.txt
This provides the timeline of the scenario being simulated.  Probably easiest explained with line by line explanation.

```
[["normal", "08:00", "15m"], 500,  % produces 500 cars with 80% going eastbound, 20% westbound (see vehicles.txt) centred around 8am normally distributed before and after this time with SD=15minutes
["vehicle", "transit eastbound"], 0.8,
["vehicle", "transit westbound"], 0.2],

["0s", 1000, ["vehicle", "kids & work"]],   %sets up 1000 vehicles following the kids & work routine in the vehicles.txt

["07:54", ["sealevel", 6]],    %sets the sealevel loading to be 6m elevation at 7:54.  The sea is those areas in the terrain.txt file that has nodata_value

["07:55", ["breach", defence1]],  %removes the flood defence named defence 1 at 7:55 allowing water to flow through

[8:00, ["evacuate"]]]]  %sends an evacuation order out following the rules in vehicles.txt to go out at 8am
```