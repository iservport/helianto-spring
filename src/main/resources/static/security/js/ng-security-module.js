(function() {
	app = angular.module('security', ['ui.bootstrap', 'app.services', 'ui.mask'])
	/**
	 * Resources
	 */
	.factory('resources', ['$resource', function($resource) {
		var service = {};
		service.loginResource = $resource("/login/"
			, { username:"@username", password: "@password", rememberme:"@rememberme"}
			, { login : { method:'POST',  headers : {'Content-Type': 'application/x-www-form-urlencoded'}}});
		service.locationResource = $resource("/api/location/:path"
		    , { stateId : "@stateId"}
		    , { save: { method: 'PUT' }, create: { method: 'POST' }});
		service.signUpResource = $resource("/signup/" , {principal:"@principal", tempEmail:"@tempEmail"});
		service.leadResource = $resource("/lead/" , {principal:"@principal", tempEmail:"@tempEmail"});

		return service;
	}])
	.controller('LoginController', ['$scope', '$window', '$http', '$resource', 'resources' , 'genericServices'
    	                                  , function($scope, $window, $http, $resource, resources, genericServices) {
    }])
	.controller('SignupController', ['$scope', function($scope)
	{
    	var self = this;
	    console.log("SignupController is active");
	    self.adminValue = false;
    }])
	.controller('RegisterController', ['$scope', '$http', function($scope, $http)
	{
    	var self = this;
	    console.log("RegisterController is active");
        /**
         * Get states.
         */
        $scope.getStates = function(initState){
	        console.log("Getting states...");
	        $scope.registration.stateCode=initState;
            $http.get('/api/context/state')
            .success(function(data) {
                $scope.states = data;
                console.log("Found "+data.length);
                $scope.getCities(initState);
            })
        }
        /**
         * Get cities.
         */
        $scope.getCities = function(stateCode){
            $http.get('/api/context/city/'+stateCode)
            .success(function(data){
                $scope.cities = data;
            })
        }
        /**
         * Confirm password
         */
        $scope.passwordConfirmed = true;
        $scope.passwordStrong = true;
        $scope.$watch('[password,cpassword]', function () {
            if(angular.isUndefined($scope.password) || $scope.password==null) {
                $scope.passwordStrong    = false;
                $scope.passwordConfirmed = false;
            }
            else {
                $scope.passwordStrong    = ($scope.password.length > 5);
                $scope.passwordConfirmed = ($scope.password === $scope.cpassword);
            }
        }, true);

    }])
	.controller('SecurityController', ['$scope', '$window', '$http', '$resource', 'resources' , 'genericServices'
	                                  , function($scope, $window, $http, $resource, resources, genericServices) {

		$scope.baseName = "home";

		$scope.validPin=false;

		$scope.validPun=false;

		$scope.cannotChangePassword = true;
		$scope.$watch('[password,cpassword]', function () { 
			$scope.cannotChangePassword = !genericServices.verifyPassword($scope.password, $scope.cpassword);
		}, true);
		
		$scope.registerResource = $resource("/register");
		$scope.passwordRecoveryResource = $resource("/recovery/", { email:"@email"}, {
			update: { method: 'PUT' },
			create: { method: 'POST' }
		});
		
		
	    /**
	     * Login submission
	     */
		$scope.login = function(usernameVal,passwordVal){
			resources.loginResource.login($.param({username :usernameVal, password :passwordVal })).$promise.then(
			function(data, getReponseHeaders) {
				$scope.logged = data;
				$window.location.href = "/";
			});
		}
		
		//form initializer
		// TODO check this
		$scope.create = function(){
			$scope.form =  resources.signUpResource.get({create:true});
			$scope.form.$promise.then(function(data) {
				data.email = $scope.email;
				$scope.form = data;
			});
		} 
		$scope.create();

		$scope.signUp = function(){
			$scope.form.password = 'save';
			$scope.returnCode = resources.signUpResource.save($scope.form);
			$scope.returnCode.$promise.then(function(data) {
				console.log(data);
			});
		};
	
		$scope.updateUser = function(){
			$scope.form.email = $scope.email;
			$scope.registerResource.save($scope.form);
		};
		
		$scope.passwordEmail = function(val){
			$scope.passwordRecoveryResource.save({email:val});
		}
		
		$scope.updatePassword = function(){
			$scope.form.email = $scope.email;
			$scope.passwordRecoveryResource.update($scope.form);
		}
		
		$scope.passwordMatches = function(){
			return $scope.cpassword === $scope.form.password;
		}

		/**
		 * State init
		 */
		$scope.firstSubmission = true;
		$scope.principalExisting = false;
		$scope.canRecover = false;
		$scope.showAlerts = false;

//		/**
//		 * Test if e-mail was previously used by identity or user
//		 */
//		$scope.emailTester=function(){
//		    $scope.previousSubmission = $scope.principal;
//		    $scope.principalExisting = false;
//			resources.leadResource.get({principal:$scope.principal}).$promise.then(function(data) {
//				if(data.principalExists){
//					$scope.principal = '';
//					$scope.principalExisting = true;
//				}
//			});
//			$scope.showAlerts = true;
//		}
	    
		$scope.saveEmail = function(emailVal){
		    $scope.previousSubmission = $scope.principal;
		    $scope.principalExisting = false;
		    $scope.firstSubmission = false;
		    $scope.showAlerts = false;
			resources.leadResource.save({principal:emailVal}).$promise.then(function(data) {
			console.log("1")
                if(data.principalExists){
                console.log("2")
                    $scope.principal = '';
                    $scope.principalExisting = true;
                    $scope.showAlerts = true;
                } else {
                console.log("3")
                }
            });
		}

		/**
		 * States.
		 */
		$scope.getStates = function(){
			resources.locationResource.query({path: 'state'}).$promise.then(
			function(data){
				$scope.states = data;
				if(data.length>0){
					$scope.stateId = data[0].id;
					$scope.getCities($scope.stateId);
				}
			})
		};
		
		/**
		 * Cities.
		 */
		$scope.getCities = function(val){
			resources.locationResource.query({path: 'city', stateId:val}).$promise.then(
			function(data){
				$scope.cities = data;
			})
		};
	}]) // SecurityController
	.controller('SignUpController', ['$scope', '$window', '$http', '$resource', 'resources' , 'genericServices'
	                                  , function($scope, $window, $http, $resource, resources, genericServices) {

	}]) // SignUpController
	;
} )();
