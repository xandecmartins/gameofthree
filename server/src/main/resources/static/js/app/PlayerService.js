'use strict';

angular.module('g3').factory('PlayerService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllPlayers: loadAllPlayers,
                getAllPlayers: getAllPlayers,
                getPlayers: getPlayer,
                removePlayer: removePlayer,
                startGamePlayer: startGamePlayer,
                startGame: startGame
            };

            return factory;

            function loadAllPlayers() {
                console.log('Fetching all players');
                var deferred = $q.defer();
                $http.get(urls.PLAYER_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all players');
                            $localStorage.players = response.data;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading players');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllPlayers(){
                return $localStorage.players;
            }

            function getPlayer(id) {
                console.log('Fetching Player with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.PLAYER_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully player with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading player with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }


            function removePlayer(id) {
                console.log('Removing Player with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.PLAYER_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllPlayers();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing Player with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
            
            function startGamePlayer(id) {
                console.log('Starting game with Player with id '+id);
                var deferred = $q.defer();
                $http.post(urls.PLAYER_SERVICE_API + id+"/start")
                    .then(
                        function (response) {
                            loadAllPlayers();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while starting game with Player with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
            
            function startGame() {
                console.log('Starting game...');
                var deferred = $q.defer();
                $http.post(urls.PLAYER_SERVICE_API+"/start")
                    .then(
                        function (response) {
                            loadAllPlayers();
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