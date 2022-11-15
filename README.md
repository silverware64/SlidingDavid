# SlidingDavid
A sliding tile puzzle game is a game that consists of a grid of multiple flat tiles with one intended
end result (a coherent picture or sequence of numbers which can only be achieved by one
specific ordering of tiles), and one space left open. The game is played by showing the player
what end result is intended, then shuffling the tiles so that the player must slide the tiles around
(using the open space) in order to organize them properly into the intended configuration.

## Requirements
- The shuffle result presented to the player to solve must be solvable. There are a few
options for doing this, but ideally this would not mean hard-coding specific shuffled
configurations - that only leads to a few possible game patterns, and as soon as the
player figures them out, the game loses all interest. Consider that many digital
implementations of this game show the final result, then show the player the shuffling
process too quickly for the player to memorize. They don’t usually immediately jump right
to a randomly generated end configuration - this should give you a hint on
implementation.
- The tiles must be unique - this could mean your puzzle looks like the numbered one in
the picture on the right, though that’s maybe a bit boring-looking - consider picture
options or themes for your wow factor!
- There must be a minimum of nine tile spaces - consider adding multiple difficulty options
with varying grid sizes!
- Game win/replay/restart logic
