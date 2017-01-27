(function () {
    'use strict';

    angular.module('app', ['ui.bootstrap', 'ui.mask'])
    .controller('ViewController', ['$rootScope', '$http', '$log', function($rootScope, $http, $log) {

        var view = this;

        $log.info("View Controller is active.");

        view.selected = 0;

        view._csrf = "";

        $http.get("/api/entity")
            .then(function(response) {
                view.entity = response.data;
            }).catch(function(e) { $log.error("GET entity FAILED"+e); })

        $http.get("/api/me")
            .then(function(response) {
                view.me = response.data;
            }).catch(function(e) { $log.error("GET me FAILED"+e); })

    }])

} ());