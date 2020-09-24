# Overview of Assignment

## Communication Protocol 
The main class should be a server that uses a ServerSocket to listen on port 8888 for incoming socket connections.
After handling a transaction with the user, the server will close the connection and listen for the next connection on port 8888.

## Configurable Gameplay
Gameplay should be configurable by providing two "game description" files to the game engine:
* Entities: structural layout & relationships
* Actions: dynamic behaviours of the entities
The game engine will parse this two files based on their document formats, and store the data into game engine.

## Entities
It is essential to use a hierarchy of "Entity" classes to represent key elements of the game:
1. Location: A room or place within the game. It recorded the paths to other Locations, and characters, artefacts, and furniture that are in the location.
2. Artefact: A physical "thing" within the game (that can be collected by the player)
3. Furniture: A physical "thing", part of a location (that can NOT be collected by the player)
4. Character: A creature/person involved in game
5. Player: A special kind of character and the user

## Actions
Dynamic behaviour within game is represented by "Actions", each of which has following elements:
1. A set of possible "trigger" words
2. A set of "subjects" entities that are acted on
3. A set of "consumed" entities that are removed
4. A set of "produced" entities that are created

## Standard Gameplay Commands
1. inventory: lists all of the artefacts currently being carried by the player
2. get: picks up a specified artefact from current location and puts it into player's inventory
3. drop: puts down an artefact from player's inventory and places it into the current location
4. goto: moves from one location to another
5. look: reports entities in the current location and paths to other locations
6. Commands from "Actions" 

## Player
The game engine accepted multiple players to play.
Each player should start with a health level of 3 Poisons.
When player's health runs out, they return to start point.
