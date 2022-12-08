
# Omega: Java editon - Group 07

This project is a re-creation of the board game 'Omega'. The aim of this project is to create different AI bots which will be able to play the game of 'Omega'. This project is part of a study on which AI is the most suitable for this board game. The game is also available to play with friends (single screen only). The creators of this project are listed at the end of this README. The client for this research is Maastricht University.

## Main menu screen
![](https://cdn.discordapp.com/attachments/1019614026038247436/1030129285811417188/unknown.png)


## Run with Git

Clone the project

```bash
  git clone https://github.com/DataDem0n/Project-2-1-07
```

Go to the project directory and open the Project-2-1-07 folder in your preferred IDE.

## Run via zip file

Extract the "Project-2-1-07-main" folder and open this open as a project in an IDE that supports Gradle.
You may need to build the project. Run the following command while in the main project directory:
```
    gradle build 
```
We recommend intelliJ IDEA for this as it automatically detects gradle build scripts.

## Run the program

After building the project, go to the following folder.
```bash
   Project-2-1-07\desktop\src\com\mygdx\game
```
Finally, run the launcher class DesktopLauncher.java by running its main method.

### Troubleshooting: Gradle not running? ("07-CodeFirstPhase" **SHOULD NOT BE THE ROOT**)

Make sure you extracted "Project-2-1-07-main" and opened that as the root directory. 


This is what your directory should look like:

![](assets/img.png)



## Features
The program's current features are:
- Player versus player mode
- Play against AI mode
- Two different AI bots
- Game instructions page
- Different map selection

## Game instructions
### Start-up and scoring
In Omega, players try to create groups of their color by placing hexagonal stones on a field in order to score points. The final score is calculated by multiplying the sizes of all of the different groups of a specific color. When the game is started each player gets assigned a color: player 1 gets pink and player 2 gets blue. 

### Playing the game
Each turn, the current player must place one stone of each color on any of the free spaces on the board, which are colored gray. You can always see which player's turn it currently is at the top of the screen. At the bottom of the screen you can see which color hexagon you have to place next. After placing your 2 stones, you can choose to either confirm your move and pass the turn to the next player, or you can choose to undo your moves by clicking on the 'Undo' button, which will reset your moves and allow you to start your turn over. Only after the first turn, player 2 can choose to use the switch? button to swap the color positions. 

### End condition
Each player's total score is always shown in the top corners of the screen, next to player number. The game ends when it is impossible for both players to complete a full turn, meaning there are not enough free spaces on the board left. The player who has the highest score then wins!


## Modes
### Mode 1
The first mode is player versus player. To play the game in this mode, go to the main menu select the map you like, and press the "Play: 2 Player" button. This mode of the game is simple. The game of Omega is able to be played as if it was a normal board game, except its on your computer. Two players looking at the same screen each take a turn to play their hexes untill the game ends. 

### Mode 2
The second mode is player versus AI. To play the game in this mode, go to the main menu select the map you like and click the "Play: AI" button. This is a single player mode. You the player will be placing your hexes, once your turn is over, the bot will choose its locations and play their turn. This will continue untill the game is over. 


## Bots
The current available bots are:
- Random bot
- One Look Ahead (OLA) bot
- MaxN Paranoid bot
- Tree Bot
- Group Fitness bot
 
 Note: changing against which bot you play is currently not available in the main menu. The default bot the player plays against is the OLA bot.


## In-game screen
![](https://cdn.discordapp.com/attachments/1019614026038247436/1030129184967766036/unknown.png)


## Authors
- [@Casper Bröcheler](https://github.com/casperbroch)
- [@Oistín Rutledge](https://github.com/DataDem0n)
- [@Laurent Bijman](https://github.com/Akita67)
- [@Miel Geraats](https://github.com/MielGeraats)
- [@Fred Schmitz](https://github.com/JuiceeBoxx)
- [@Aurelien Giuglaris – Michael](https://github.com/auregiuglarism)
- [@Vince Kotsis]()

