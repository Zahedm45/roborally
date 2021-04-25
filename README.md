#ROBORALLY 

This is Robarally game 

## How to play 

Start main method from `StartRoboRally` class. 

Reach all the checkPoints in numerical order (first 1, then 2, then...) 

Whoever reaches all the checkPoints before anyone else, will be the winner.


## Features (without database connection) 
1. You will able to save a board on your computer and then you can edit the board as you wish and load the board from your computer

2. Walls -  robots/players can't go through a wall.

3. Robot can push other robots.

4. Gear - turns a robot either right or left.

5. PushPanel - pushes robots in PushPanel's direction. Though, only on step 2 and 4.

6. BoardLaser and EnergySpace - BoardLaser sets robots in survival mode, so that the robots have to get to the energySpace/energyBack first. 
Checkpoints will not be counted, when a robot in survival mode. If a robot has an eneryBank in advance then the robot won't be set in survival mode.

7. EnergySpace - can be picked up only once from one particular space in a game by one robot.

8. Pit - draws walls around the robot. To get out of a pit the robot has to use a damageCard.

9. ConveyorBelt - pushes robots in conveyorBelt's direction.

10. Once a game is done you will be asked if you want to play another game, exit or go to the game lobby.


## Extended features with database connection.

1. Games will be saved automatically in the database right after a user interaction occurred. 
So, that the game is repayable if the game stopped for any reasons, and everything will be as it was before it stopped.

2. It is only the running (no winner yet) games you will be able to load from DB. 
The game list is sorted by date and time. Whenever a game is loaded from DB, it gets updated by current date and time.
So that the last game comes first.






 







## Database connection:
This program requires a database in order to save and resume a game.

First thing you need to do is create a database on your own computer, and to create a database you have to install MariaDB. Down below links provided

Mac https://mariadb.com/resources/blog/installing-mariadb-10-1-16-on-mac-os-x-with-homebrew/

Windows
https://downloads.mariadb.org/mariadb/10.5.8/#file_type=msi

Once you installed the software, you can now create a database either through MSQLWorkbench or Command Line.

Flow the instructions if you do it in command line.

#####Start mysql

Mac: `mysql–u root -p

Windows: `MariaDB->MySQL Client`

Enter your root password.

#####Create a database

MariaDB[(none)]> `create database nameOfDatabase;`

Now you go to class `Connection` roborally->dal->Connection and write your database name, username and password. 

Done!

