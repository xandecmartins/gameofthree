# Description
    
    This project is implementation of the chalenge "Game of Three"

# Architecture

    There are two modules

 ## Server
 
    Responsible to receive the registration of players and control the round of the game
 
 ## Player
 
    Unit responsible to choose the values and send the value. This unit could be manual or autonomous.
  
  ## Technologies
  
    Backend: Java, Spring-Boot, REST
    Frontend: Java, AngularJS, REST, FTL (freemarker)
    
# How to run

  ## Build
  
    mvn package
   
  ## Run
  
    java -jar server/target/server.jar
    
    java -jar player/target/player.jar
    java -jar player/target/player.jar
    
    Access your browse on address http://localhost:8080/server
    
# Main Features

    * Backend and Frontend implemented
    * Possibility to easy expand the game for 3 or more players, using the round-robin concept
    * Easy way to change the type of player (manual/autonomous)
    
# Features Not Implemented
    
    * Several strategies of autonomous player
    * Show historic of games
    * Permit a player disconnect and the other be called winner
    
 
  
