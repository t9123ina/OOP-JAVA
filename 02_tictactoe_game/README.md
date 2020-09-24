## Overview of Assignment

The game allow grids of any size (not just 3x3), and any number of players (not just 2).
For bigger grids, the game can allow to set different number of cells in a row required to win.

# Gameplay
Players will take it in turns to enter a two-character position into the OXOView GUI window.
This position consists of the row letter and the column number of the cell they wish to claim.
For example, if a user wished to claim the centre cell, they would type: b2 This will be automatically passed to the OXOController (via the handleIncomingCommand method).
On receiving a cell position, the code will:
* Check to make sure the specified cell is a valid one (it actually exists and is still available)
* Notify the system that an invalid cell was requested (if an **invalid** cell was requested)
* Claim the specified cell on behalf of the current player (if a **valid** cell was requested)
* Check if the game has been won by the new move (reporting the winner to the model if so)
* Check if the game is a draw (reporting the draw to the model if so)
* Switch to the next player, ready for the next move (the board view will show who's turn it is)

# Exceptions
Three Exception classes are provided to enable your code to notify the system of invalid moves:
* Invalid Cell Identifier: The row and/or column characters are not valid (e.g. punctuation marks)
* Cell Does Not Exist: The identifiers are valid characters, but they too big (out of range)
* Cell Already Taken: The specified cell exists, but it has already been claimed by a player
The OXOController would instantiate and throw instances of these exceptions as appropriate.

# Win Detection
The controller would check for wins in all directions (Horizontal, Vertical and Diagonal).
