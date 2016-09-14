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
	.controller('SecurityController', ['$scope', '$window', '$http', '$resource', 'resources' , 'genericServices'
	                                  , function($scope, $window, $http, $resource, resources, genericServices) {
	
		$scope.baseName = "home";
		
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
		
//		$scope.getStates();


		$scope.cnpjOk=false;
        $scope.valideCNPJ = function(value){
            $scope.cnpjOk=validarCNPJ(value);
        };

        function validarCNPJ(cnpj) {
            if(cnpj == null || cnpj == 'undefined' || cnpj == '') return false;
            cnpj = cnpj.replace(/[^\d]+/g,'');

            if (cnpj.length != 14) return false;

            // Elimina CNPJs invalidos conhecidos
            if (cnpj == "00000000000000" ||
                cnpj == "11111111111111" ||
                cnpj == "22222222222222" ||
                cnpj == "33333333333333" ||
                cnpj == "44444444444444" ||
                cnpj == "55555555555555" ||
                cnpj == "66666666666666" ||
                cnpj == "77777777777777" ||
                cnpj == "88888888888888" ||
                cnpj == "99999999999999")
                return false;

            // Valida DVs
            tamanho = cnpj.length - 2
            numeros = cnpj.substring(0,tamanho);
            digitos = cnpj.substring(tamanho);
            soma = 0;
            pos = tamanho - 7;
            for (i = tamanho; i >= 1; i--) {
              soma += numeros.charAt(tamanho - i) * pos--;
              if (pos < 2)
                    pos = 9;
            }
            resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
            if (resultado != digitos.charAt(0))
                return false;

            tamanho = tamanho + 1;
            numeros = cnpj.substring(0,tamanho);
            soma = 0;
            pos = tamanho - 7;
            for (i = tamanho; i >= 1; i--) {
              soma += numeros.charAt(tamanho - i) * pos--;
              if (pos < 2)
                    pos = 9;
            }
            resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
            if (resultado != digitos.charAt(1))
                  return false;

            return true;

        }


	}]) // SecurityController
	.controller('SignUpController', ['$scope', '$window', '$http', '$resource', 'resources' , 'genericServices'
	                                  , function($scope, $window, $http, $resource, resources, genericServices) {

	}]) // SignUpController
	;
} )();
