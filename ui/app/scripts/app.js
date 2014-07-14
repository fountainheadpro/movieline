'use strict';

/**
 * @ngdoc overview
 * @name movielineUiApp
 * @description
 * # movielineUiApp
 *
 * Main module of the application.
 */
angular
  .module('movielineUiApp', [
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ngReact'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'SearchController'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
