'use strict';

angular.module('g3').controller('ServerController',
    ['PlayerService', '$scope','$interval',  function( PlayerService, $scope, $interval) {

        var self = this;
        self.players = [];

        self.getAllPlayers = getAllPlayers;
        self.removePlayer = removePlayer;
        self.startGamePlayer = startGamePlayer;
        self.startGame = startGame;

        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;

        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;

        function removePlayer(id){
        	self.successMessage = '';
            self.errorMessage = '';
        	console.log('About to remove Player with id '+id);
            PlayerService.removePlayer(id)
                .then(
                    function(){
                        console.log('Player '+id + ' removed successfully');
                    },
                    function(errResponse){
                    	if ('data' in errResponse){
                    		console.error('Error while removing player '+id +', Error :'+errResponse.data.errorMessage);
                    		self.errorMessage =  errResponse.data.errorMessage;
                    	} else {
                    		
                    		console.error('Error while removing player '+id);
                    	}
        			}
                );
        }
        
        function startGamePlayer(id){
        	self.successMessage = '';
            self.errorMessage = '';
        	console.log('About to start game to Player with id '+id);
            PlayerService.startGamePlayer(id)
                .then(
                    function(sucResponse){
                        console.log('Player '+id + ' started successfully');
                        self.successMessage = sucResponse.data.successMessage;
                    },
                    function(errResponse){
                    	if ('data' in errResponse){
                    	 	console.error('Error while starting player '+id +', Error :'+errResponse.data.errorMessage);
                    	 	self.errorMessage =  errResponse.data.errorMessage;
	                	} else {
	                		console.error('Error while starting player '+id);
	                	}
                       
                       
                    }
                );
        }


        function getAllPlayers(){
            return PlayerService.getAllPlayers();
        }
        
        function startGame(){
        	reset();
        	return PlayerService.startGame();
        }
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
        }
        
        $scope.reload = function () {
        	PlayerService.loadAllPlayers();
         };
        $scope.reload();
        $interval($scope.reload, 500);
    }

    ]);