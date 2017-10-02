'use strict';

angular
		.module('g3')
		.controller(
				'PlayerController',
				[
						'PlayerService',
						'$scope',
						'$interval',
						function(PlayerService, $scope, $interval) {

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

							function getPlayer() {
								return PlayerService.getPlayer();
							}

							function startGame() {
								self.successMessage = '';
								self.errorMessage = '';
								console.log('About to start game');
								PlayerService
										.startGame()
										.then(
												function() {
													console
															.log('Player started successfully');
												},
												function(errResponse) {
													console
															.error('Error while starting player, Error :'
																	+ errResponse.data.errorMessage);
													self.errorMessage = errResponse.data.errorMessage;
												});
							}

							function manualPlay() {
								self.successMessage = '';
								self.errorMessage = '';
								console.log('About to start game');
								PlayerService
										.manualPlay(self.player.currentNumber)
										.then(
												function() {
													console
															.log('Player started successfully');
												},
												function(errResponse) {
													console
															.error('Error while starting player, Error :'
																	+ errResponse.data.errorMessage);
													self.errorMessage = errResponse.data.errorMessage;
												});
							}

							function update() {
								console.log('--->' + self.player.autonomous);
								return PlayerService
										.update(self.player.autonomous);
							}

							$scope.reload = function() {
								PlayerService.loadPlayer();
								self.player.status = getPlayer().status;
								if (self.player.autonomous) {
									self.player = getPlayer();
								} else if (getPlayer().haveNewValue) {
									self.player = getPlayer();
									PlayerService.startNewValue();
								}

							};
							$scope.reload();
							$interval($scope.reload, 500);
						}

				]);