'use strict';

/**
 * @ngdoc function
 * @name movielineUiApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the movielineUiApp
 */
 
angular.module('movielineUiApp')
  .controller('SearchController', function ($scope, $http) {
   $scope.data=[];	
   $scope.search=function(query){
	   $http.get('http://localhost:8080/search/'+encodeURIComponent(query)).success(function(data) {
	      $scope.data = data;
	    });      	
   }

 });