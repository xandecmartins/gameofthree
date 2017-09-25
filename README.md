# Description
    
    This project is an implementation of the chalenge "Game of Three"

# Architecture

    There are two modules. First the serve must be started to receive players for registration. However, there is a limit of two players. It is possible to change this, to do it, you have to update the Application.properties in the server module.

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
    
    * Server  
    java -jar server/target/server.jar
    
    * Player 1
    java -jar player/target/player.jar
    
    * Player 2
    java -jar player/target/player.jar
    
    Access your browse on address http://localhost:8080/server
    
    The links of players will be available on the server page.
    
# API Rest

    ## Server 
        * GET http://localhost:8080/server/api/player 
            + Return the player list
        * GET http://localhost:8080/server/api/player/{id} 
            + Return a player by id
        * DELETE http://localhost:8080/server/api/player/{id}
            + Delete a player
        * GET http://localhost:8080/server/api/start/player/{id}
            + Receive the ask to start a game from player
        * GET http://localhost:8080/server/api/register/{ip}/{port}
            + Receive the request to register a new player
        * GET http://localhost:8080/server/api/start
            + Start a new game, using the fisrt registred player to begin
        * GET http://localhost:8080/server/api/play/{number}/player/{id}
            + Receive a number from a player to send for another player
        
        
    ## Player
    
  
# Main Features

    * Backend and Frontend implemented
    * Possibility to easy expand the game for 3 or more players, using the round-robin concept
    * Easy way to change the type of player (manual/autonomous)
    
# Features Not Implemented
    
    * Several strategies of autonomous player
    * Show historic of games
    * Permit a player disconnect and the other be called winner
    
 
  
