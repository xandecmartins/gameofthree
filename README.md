# Description
    
    This project is an implementation of the chalenge "Game of Three"

# Architecture

    The Architecture is based on the client-server model and there are two modules available. First the serve must be started to receive players for registration. However, there is a limit of two players. It is possible to change this, to do it, you have to update the Application.properties in the server module.

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
        
         * GET http://localhost:8080/player/api/
            + Return the player values
         * GET http://localhost:8080/player/api/newStatus/{status}
            + Receive new status from server
         * GET http://localhost:8080/player/api/newStatus/{status}/currentNumber/{currentNumber}
            + Receive a new status from server and a new value
         * GET http://localhost:8080/player/api/receive/{number}
            + Receive a number from server to make a play
         * GET http://localhost:8080/player/api/manualPlay/{number}
            + Receive a value typed for a user (not autonomous)
         * GET http://localhost:8080/player/api/askToStart
            + Receive from screen a requesto to start
         * GET http://localhost:8080/player/api/update/{autonomous}
            + Receive a new value to the autonomous property
         * GET http://localhost:8080/player/api/startNewValue
            + Receive a new value from server after marked as manual
         * GET http://localhost:8080/player/api/begin/{bound}
            + Receive the command to random a number from server and start the game
         * DELETE http://localhost:8080/player/api/disconnect
            + Receive the ask to disconnect
  
# Main Features

    * Backend and Frontend implemented
    * Easy Possibility to expand the game for 3 or more players, using the round-robin concept
    * Easy way to change the type of player (manual/autonomous)
    * Flexible configuration using application.properties file
    
# Features Not Implemented
    
    * Show historic of games
    * Permit a player disconnect and the other be called winner
