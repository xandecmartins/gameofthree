var app = angular.module('g3',['ui.router','ngStorage']);

app.constant('urls', {
    SERVER: 'http://localhost:8080/server/api'
    
});

app.config(['$stateProvider', '$urlRouterProvider',
    function($stateProvider, $urlRouterProvider) {

        $stateProvider
            .state('home', {
                url: '/',
                templateUrl: 'partials/player',
                controller:'PlayerController',
                controllerAs:'ctrl',
                resolve: {
                    player: function ($q, PlayerService) {
                        console.log('Load player');
                        var deferred = $q.defer();
                        PlayerService.loadPlayer().then(deferred.resolve, deferred.resolve);
                        return deferred.promise;
                    }
                }
            });
        $urlRouterProvider.otherwise('/');
    }]);

