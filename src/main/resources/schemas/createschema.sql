/* Need to switch of FK check for MySQL since there are crosswise FK references */
SET FOREIGN_KEY_CHECKS = 0;;
 -- DROP TABLE IF EXISTS Game;;
 -- DROP TABLE IF EXISTS Player;;

CREATE TABLE IF NOT EXISTS Game (
  gameID int NOT NULL UNIQUE AUTO_INCREMENT,

  name varchar(255),

  phase tinyint,
  step tinyint,
  currentPlayer tinyint NULL,

  PRIMARY KEY (gameID),
  FOREIGN KEY (gameID, currentPlayer) REFERENCES Player(gameID, playerID)
);;

CREATE TABLE IF NOT EXISTS Player (
  gameID int NOT NULL,
  playerID tinyint NOT NULL,

  name varchar(255),
  colour varchar(31),

  positionX int,
  positionY int,
  heading tinyint,



  PRIMARY KEY (gameID, playerID),
  FOREIGN KEY (gameID) REFERENCES Game(gameID)
);;

SET FOREIGN_KEY_CHECKS = 1;;

CREATE TABLE IF NOT EXISTS RegisterField(
 gameID int,
 playerID tinyint NOT NULL,

 card1 varchar(255),
 card2 varchar(255),
 card3 varchar(255),
 card4 varchar(255),
 card5 varchar(255),

PRIMARY KEY (gameID, playerID),
FOREIGN KEY (gameID, playerID) REFERENCES Player(gameID, playerID)
);;

CREATE TABLE IF NOT EXISTS CommandCardField(
 gameID int,
 playerID tinyint NOT NULL,

 card1 varchar(255),
 card2 varchar(255),
 card3 varchar(255),
 card4 varchar(255),
 card5 varchar(255),
 card6 varchar(255),
 card7 varchar(255),
 card8 varchar(255),


PRIMARY KEY (gameID, playerID),
FOREIGN KEY (gameID, playerID) REFERENCES Player(gameID, playerID)
);;
