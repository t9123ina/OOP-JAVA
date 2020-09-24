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
1. Location: A room or place within the game
1. Artefact: A physical "thing" within the game (that can be collected by the player)
1. Furniture: A physical "thing", part of a location (that can NOT be collected by the player)
1. Character: A creature/person involved in game
1. Player: A special kind of character and the user
