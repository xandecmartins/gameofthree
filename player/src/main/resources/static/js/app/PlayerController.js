'use strict';

angular.module('g3').controller('PlayerController',
    ['PlayerService', '$scope','$interval',  function( PlayerService, $scope, $interval) {

        var self = this;
        self.player = {};
        $scope.player = {};
       
        self.getPlayer = getPlayer;
        self.startGame = startGame;
        self.manualPlay = manualPlay;
        self.update = update;
        
        self.player = getPlayer();
        
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;

        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;



        function getPlayer(){
            return PlayerService.getPlayer();
        }
        
        function startGame(){
        	return PlayerService.startGame();
        }
        
        function manualPlay(){
        	return PlayerService.manualPlay(self.player.currentNumber);
        }
        
        function update(){
        	console.log('--->' + self.player.autonomous);
        	return PlayerService.update(self.player.autonomous);
        }
        
        $scope.reload = function () {
        	PlayerService.loadPlayer();
        	self.player.status = getPlayer().status;
        	if (self.player.autonomous){
        		self.player = getPlayer();
        	} else if(getPlayer().haveNewValue){
        		self.player = getPlayer();
        		PlayerService.startNewValue();
        	}
        	
         };
        $scope.reload();
        $interval($scope.reload, 1000);
    }

    ]);