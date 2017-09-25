'use strict';

angular.module('g3').factory('PlayerService',
    ['$localStorage', '$http', '$q', 'urls','$location',
        function ($localStorage, $http, $q, urls, $location) {

            var factory = {
            	loadPlayer: loadPlayer,
            	getPlayer: getPlayer,
                startGame: startGame,
                manualPlay: manualPlay,
                update: update,
                startNewValue: startNewValue,
            };

            return factory;

            function loadPlayer() {
                var deferred = $q.defer();
                $http.get('http://'+$location.host()+':'+$location.port()+'/player/api/')
                    .then(
                        function (response) {
                            $localStorage.player = response.data;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading player');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
            
            function getPlayer(){
                return $localStorage.player;
            }

            function startGame() {
                var deferred = $q.defer();
                $http.get('http://'+$location.host()+':'+$location.port()+'/player/api/askToStart')
                    .then(
                        function (response) {
                            loadPlayer();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while starting the game');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
            
            function manualPlay(currentNumber) {
                var deferred = $q.defer();
                $http.get('http://'+$location.host()+':'+$location.port()+'/player/api/manualPlay/'+currentNumber)
                    .then(
                        function (response) {
                            loadPlayer();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while starting the game');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
            
            function update(autonomous) {
                var deferred = $q.defer();
                $http.get('http://'+$location.host()+':'+$location.port()+'/player/api/update/'+autonomous)
                    .then(
                        function (response) {
                            loadPlayer();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while starting the game');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
            
            function startNewValue() {
                var deferred = $q.defer();
                $http.get('http://'+$location.host()+':'+$location.port()+'/player/api/startNewValue/')
                    .then(
                        function (response) {
                            loadPlayer();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while starting the game');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
        }
    ]);