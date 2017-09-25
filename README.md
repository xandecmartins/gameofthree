# Description
    
    This project is implementation of the chalenge "Game of Three"

# Architecture

    There are two modules

 ## Server
 
    Responsible to receive the registration of players and control the round of the game
 
 ## Player
 
    Unit responsible to choose the values and send the value. This unit could be manual or autonomous.
  
  ## Technologies
  
    => Backend: Java, Spring-Boot, REST
    => Frontend: Java, AngularJS, REST, FTL (freemarker)
    
# How to run

  ## Build
  
    mvn player/pom.xml
    mvn server/pom.xml
    
  ## Run
  
    java -jar target/server.jar
    java -jar target/player.jar
    java -jar target/player.jar
    
    Access your browse on address http://localhost:8080/server
 
  
