# SlidingDavid
##### Demo Video Link: https://www.youtube.com/watch?v=tmr0Qv3Dm9o

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

## Permutation Logic, and Why Our Puzzle is Solvable.
- Permutation Fact: Using swaps, going from Permutation A to Permutation B either takes an
even number about of swaps, or an odd number of swaps. If a Permutation can be reached with an
even number of swaps, it can ONLY be reached with an even number of swaps. The same is true for a
Permutation requiring an odd number of swaps.
- The generatePuzzle() function within GamePanel.java creates an 2D-array of ints, NxN. Then
it randomly swaps 100 elements within that 2-D array. Therefore, we know our solution will
require an even number of swaps.
- Making a swap within the puzzle requires moving the "blank" space either up, down, left, or right.
Therefore, n = u+d+l+r, where n is the number of moves, and u,d,l,r represent their respective number of moves.
Our blank space starts in the bottom right, and it must end in the bottom right for the solution.
That means that we must make the same number of left moves as right moves, and the same number of
up moves as down moves. Therefore, l=r, and u=d, and we can simplify our equation above to,
    n = 2u + 2l, or n = 2(u+l). That means n is even, and our puzzle can be solved.
- TLDR, Our puzzle is solvable because we swap the original an even amount of times(var nrOfSwaps), and the
blank is in the same place in both the start and solution.
