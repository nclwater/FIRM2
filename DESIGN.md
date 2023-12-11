# Layers

The basic framework for this modelling environment consists of several layers of grids. Each grid hosts agents of a specific type.

```mermaid
---
title: FIRM2 Layers
---

classDiagram
    class Agent{
        +int agentId
        +int tickAge
        +Color colour
    }
    class Terrain {
        +float elevation
    }
    class Building {
        +int type
    }
    class Defence
    class Road {
        +int speedLimit
    }
   
    Agent <|--Terrain
    Agent <|--Building
    Agent <|--Defence
    Agent <|--Road
    

    

```

Agents have to implement the `Agent` abstract class which is in the `model` package.
