'use strict';

angular.module('g3').controller('ServerController',
    ['PlayerService', '$scope','$interval',  function( PlayerService, $scope, $interval) {

        var self = this;
        $scope.player = {};
        self.player = {};
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
            console.log('About to remove Player with id '+id);
            PlayerService.removePlayer(id)
                .then(
                    function(){
                        console.log('Player '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing player '+id +', Error :'+errResponse.data);
                    }
                );
        }
        
        function startGamePlayer(id){
            console.log('About to start game to Player with id '+id);
            PlayerService.startGamePlayer(id)
                .then(
                    function(){
                        console.log('Player '+id + ' started successfully');
                    },
                    function(errResponse){
                        console.error('Error while starting player '+id +', Error :'+errResponse.data);
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
            self.player={};
            $scope.myForm.$setPristine(); //reset Form
        }
        
        $scope.reload = function () {
        	console.log('updating with changes');
        	PlayerService.loadAllPlayers();
         };
        $scope.reload();
        $interval($scope.reload, 1000);
    }

    ]);