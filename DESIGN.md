# Layers

The basic framework for this modelling environment consists of several layers of grids. Each grid hosts agents of a specific type.

```mermaid
---
title: FIRM2 Layers
---

classDiagram
    class Agent
    Agent : +int agent_id
    Agent : +int tick_age
    Agent : +Color colour
    
    class Terrain
    Terrain : +float elevation
    
    class Building
    Building : +int type
    
    class Defence
    
    class Road
    Road : +int speedLimit
    
    Agent --> Terrain
    Agent --> Building
    Agent --> Defence
    Agent --> Road 
```

Agents have to implement the `Agent` abstract class which is in the `model` package.