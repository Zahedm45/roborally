#ROBORALLY GAME


### How to play 

Start main method from `StartRoboRally` class. 

Achieve all the checkPoints in order (first 1, then 2, then...) 

Who ever achieves all the checkPoints before anyone else, will be the winner.


### Features (database connection is not needed) :
1. You will able to save a board to your computer.

2. load a board from your computer

3. choose number of player from 1 to 6

4. Walls - a robot/player can't go through a wall.

5. A robot can push other robots.

6. Gear - turns a robot either right or left.

7. PushPanel - pushes the robot in PushPanel's direction. Though, only on step 2 and 4.

8. BoardLaser and EnergySpace - BoardLaser sets a robot in survival mode, so that the robot has to get to the energySpace/energyBack first. 
Checkpoints will not be counted, when a robot in survival mode. If a robot has an eneryBank in advance then the robot won't be set in survival mode.

9. EnergySpace - can be picked up only once from a particular space in a game by one robot.

10. Pit - draws walls around the robot. To get out of a pit the robot has to use a damageCard.

11. ConveyorBelt - pushes the robot in conveyorBelt's direction.



### extended features with a database connection.

12. The game will be saved automatically in the database right after a user interaction occurred. 
So that the game is repayable if the game stopped for any reasons. 







 







### Database connection:
This program requires a database in order to save a game.

First thing you need to do is create a database on your own computer, and to create a database you have to install MariaDB. Down below links provided

Mac https://mariadb.com/resources/blog/installing-mariadb-10-1-16-on-mac-os-x-with-homebrew/

Windows
https://downloads.mariadb.org/mariadb/10.5.8/#file_type=msi

Once you installed the software, you can now create a database either through MSQLWorkbench or Command Line.

Flow the instructions if you do it in command line.

Start mysql

Mac: `mysql–u root -p

Windows: `MariaDB->MySQL Client`

Enter you root password.

Create a database`

MariaDB[(none)]> `create database nameOfDatabase;`

Now you go to class `Connection roborally->dal->Connection and write your database, username and password. 

Done!

