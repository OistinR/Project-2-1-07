
# Crazy Putting - Group 04

We have created a program which is able to simulate a 3D golf game with 2D physics. This program uses its own custom made physics engine for computing the trajectory of the ball. 
This physics engine can use six different ODE solvers for computing the physics. This program also contains five different bots which are able to solve courses. Finally we also implemented a demo for a multiplayer mode.



![](https://media.discordapp.net/attachments/945742228108742668/976104158572728381/Screenshot_2022-05-17_at_14.49.20.png?width=1185&height=670)


## Run Locally

Clone the project

```bash
  git clone https://github.com/UM-KEN1300/Group-04
```

Go to the project directory and open the entire group-04 folder in your preferred IDE.

Next, go to the following folder.
```bash
   group-04\3dcrazyputting\desktop\src\com\crazyputting3d
```
Finally, run the main of the class Main.java to run the program.

## Run experiments
To run the experiments go to the following folder.
```bash
   group-04\3dcrazyputting\desktop\src\com\crazyputting3d\Experiment
```
Then, go to the experimentmain.java file and run the main method. The resulting data will be showed in the Experiment.csv file which is placed in the same folder.

## Features

- Settings menu to change X and Y  positions of the hole and ball
- Drop down menu to change which bot will be used
- Drop down menu to change which solver will be utilized
- Three launch modes (Play, Play bot, Play maze, Multiplayer)

The multiplayer mode as of writing this README is in its demo stages. The functionality of the multiplayer mode is that everyone who joined the server using the Multiplayer mode will see the all the scores of the other players which are also simultaneously on the same server.

## Modes
- Mode "Play": when the program is launched as "Play" the user will be able to play the game as a normal golf game.

- Mode "Play bot": when the program is launched as "Play bot" the user will be presented with the five different levels. When the user chooses a level, the 3D game will be launched using the bot which was chosen in the main menu. 

- Mode "Play maze": in this mode, the user will be presented with the five different maze levels. The game will be played using the bot which was again chosen from the main menu. Because the levels are mazes, the bot called "A* maze".

- Mode "Multiplayer": in this mode the user will be launched in multiplayer mode and will acces the server. If the server is offline, the player will be presented with a seperate screen that states that the player was unable to connect to the server.

## Solvers and bots
In this program you can choose from the following different solvers.
- Eulers method
- Runge Kutta 2
- Runge Kutta 4
- Verlets method
- Dormand Prince
- Predictor corrector (Adams Moulton & Adams Bashforth)

In this program you can also choose from the following bots.
- Hill climbing bot
- Newton raphson bot
- Rule based bot
- Random based bot
- Brute force bot
- A* maze bot
- Simulated Annealing


## Change input

It is also possible to change the input of the program. To do this, go to the following folder.
```bash
   group-04\3dcrazyputting\assets\levels
```
Then, go to the files inputFile(insert level integer).txt. In here it is possible to change the inputs. For example, go to inputFile1.txt and change. Now the input for level 1 is changed. 

Note: some extra work is needed to change the heightprofile. Go to the following folder. 
```bash
   group-04\3dcrazyputting\desktop\src\com\crazyputting3d\InputReader
```
Next, open the file called Function.java. Then, change the heightprofile inside of the switch cases.

## Authors
- [@Casper Bröcheler](https://github.com/casperbroch)
- [@Oistín Rutledge](https://github.com/DataDem0n)
- [@Laurent Bijman](https://github.com/Akita67)
- [@Miel Geraats](https://github.com/MielGeraats)
- [@Fred Schmitz](https://github.com/JuiceeBoxx)
- [@Aurelien Giuglaris – Michael](https://github.com/auregiuglarism)
- [@Vince Kotsis]()

