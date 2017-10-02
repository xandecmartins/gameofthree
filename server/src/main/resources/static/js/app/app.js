var app = angular.module('g3',['ui.router','ngStorage']);

app.constant('urls', {
    BASE: 'http://localhost:8080/server',
    PLAYER_SERVICE_API : 'http://localhost:8080/server/api/players/'
});

app.config(['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {

        $stateProvider
            .state('home', {
                url: '/',
                templateUrl: 'partials/list',
                controller:'ServerController',
                controllerAs:'ctrl',
                resolve: {
                    players: function ($q, PlayerService) {
                        console.log('Load server');
                        var deferred = $q.defer();
                        PlayerService.loadAllPlayers().then(deferred.resolve, deferred.resolve);
                        return deferred.promise;
                    }
                }
            });
        $urlRouterProvider.otherwise('/');
    }]);

