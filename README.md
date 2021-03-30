This is the readme file for the project.

The assignment 1 is UNO game that players could play against each other. Try start the game by running game.java with number of players, by default, game will assume that there are 4 players. Then each player could connect by running player.java.

The cards in the game are represented by strings, for most cards, the first letter R,Y,G,B indicates the color of the card, then the after the color is either a number from 0 to 9 or skip,rev (reverse),d2 (draw 2). If the first letter is not R,Y,G,B, then they are wild cards. A single W represents wild card and W4 represents the wildDraw card

When play the game, try type the position (starting at 0) of card you want to play; then when try to draw cards, type "draw","Draw" or "d"; after playing wild cards, type "red,Red,r", "yellow,Yellow,y","blue,Blue,b" and "green,Green,g" for color you want to set. Then when you want to end your turn, type "skip,Skip,s".

There are 5 classes in Cardgame package, in Cards.java, it defines the cards used in the game; in Game.java, it handles the game logic and also start the game server that players should connect to; in Player.java, it created a platform for user to type their command and receive information about the game; in PlayerHandler, it handles all the requests from player; then the PlayConnection helps players receive informations.

There are 1 classes in gameTest class, it tests basic game logics and basic player connections.
