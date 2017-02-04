(function() {
	app = angular.module('app')
	.controller('LoginController', ['$scope', '$window', '$http', function($scope, $window, $http) {
    }])
	.controller('SignupController', ['$scope', function($scope)
	{
    	var self = this;

        // TODO validate PIN
        self.invalidPin=false;

	    console.log("SignupController is active");
	    self.adminValue = false;
    }])
	.controller('RegisterController', ['$scope', '$http', '$log', function($scope, $http, $log)
	{
    	var self = this;
	    console.log("RegisterController is active");
        /**
         * Get states.
         */
        $scope.getStates = function(initState){
	        $log.info("Getting states...");
	        $scope.registration.stateCode=initState;
            $http.get('/api/context/state')
            .then(function(data) {
                $scope.states = data;
                $log.info("Found "+data.length);
                $scope.getCities(initState);
            })
        }
        /**
         * Get cities.
         */
        $scope.getCities = function(stateCode){
            $http.get('/api/context/city/'+stateCode)
            .then(function(data){
                $scope.cities = data;
            })
        }
        /**
         * Confirm password
         */
        $scope.passwordConfirmed = true;
        $scope.passwordStrong = true;
        $scope.$watchGroup(['password', 'cpassword'], function () {
        $log.info("!")
            if(angular.isUndefined($scope.password) || $scope.password==null) {
                $log.info("-")
                $scope.passwordStrong    = false;
                $scope.passwordConfirmed = false;
            }
            else {
                $log.info("+")
                $scope.passwordStrong    = ($scope.password.length > 5);
                $scope.passwordConfirmed = ($scope.password === $scope.cpassword);
            }
        }, true);

        $scope.validForm = function() {

        }

    }])
	.controller('SecurityController', ['$scope', '$window', '$http', function($scope, $window, $http) {

		$scope.baseName = "home";

        // TODO Será necessário aqui? agora ja tem no Security Controller
		$scope.validPin=true;

		$scope.validPun=false;

		/**
		 * State init
		 */
		$scope.firstSubmission = true;
		$scope.principalExisting = false;
		$scope.canRecover = false;
		$scope.showAlerts = false;

	}]) // SecurityController
	;
} )();
