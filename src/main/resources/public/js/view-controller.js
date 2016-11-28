angular.module('app.services')
/**
 * View controller
 */
.controller('ViewController', ['$rootScope', '$scope', '$http', '$resource', 'authorizedData', 'lang', 'pluginServices'
                               , function($rootScope, $scope, $http, $resource, authorizedData, lang, pluginServices) {

    $rootScope.currentDate = new Date();

    $rootScope.pluginServices = pluginServices;

    $rootScope.notifications = ['PRINCIPAL','EMAIL','BY_REQUEST'];


    /**
    * Define if ViewController run over Signed page (Avoid signIn mandatory calls).
    *
    * default = true; if not signed use : 'var signed = false ;'  see '/security/login.html'.
    **/
    $scope.signed =(typeof signed === 'undefined' || signed===null)?true:signed;

    $rootScope.logout = function() {
        return $http.post('/logout');
    }

    /**
     * Tabs
     */
    $rootScope.sectionTab = 0;

    $rootScope.getSectionTab = function() {
        return $rootScope.sectionTab;
    }

    $rootScope.setSectionTab = function(value) {
        $rootScope.sectionTab = value;
    }

    $rootScope.isSectionTabSet = function(value,role) {
        if (angular.isDefined(role) && $rootScope.isAuthorized(role)) {
            return $rootScope.sectionTab === value;
        }
        return $rootScope.sectionTab === value;
    }

    /**
     * Bottom tabs
     */
    $rootScope.bottomSectionTab = 0;
    $rootScope.setBottomSectionTab = function(value) {
        $rootScope.bottomSectionTab = value;
    };
    $rootScope.isBottomSectionTabSet = function(value) {
        return $rootScope.bottomSectionTab === value;
    };

    $rootScope.identityResource = $resource('/app/identity/' , null
            , {	save: { method: 'PUT' }, create: { method: 'POST' }});
    $rootScope.userAuthResource = $resource("/api/entity/auth", {userId: "@userId"}, {});

    /**
     * Location Resource.
     */
    $rootScope.locationResource = $resource("/api/location/:path", { stateId : "@stateId"}, {
        save: { method: 'PUT' }
        , create: { method: 'POST' }
    });

    $rootScope.roleList = [];

    $scope.identityTypes = [{enum:"NOT_ADDRESSABLE"}, {enum:"ORGANIZATIONAL_EMAIL"}, {enum:"PERSONAL_EMAIL"} ];

    $rootScope.genders = ["NOT_SUPPLIED", "MALE", "FEMALE" ];

    /**
     * Read the authorized user.
     */
    if($scope.signed){$http.get('/api/entity/user').success(function(data) {$rootScope.authorizedUser = data})};

    /**
     * Profile
     */
    $rootScope.updatePassword = function() {
        $rootScope.identityResource.get({identityId:authorizedData.getAuthorizedUser().identityId}).
        $promise.then(
        function(data) {
            $rootScope.identity = data;
            $rootScope.openForm("/assets/user/form/profile.html");
        });
    };

    /**
     * Profile
     *
     * PUT /app/identity/
     */
    $scope.updateProfile = function(){
        $rootScope.identityResource.save({}, $rootScope.identity).$promise.then(
        function(data) {
            $rootScope.identity = data;
        });
        $("#modalBody").modal('hide');
    };

    $scope.getPersonalProfile = function(id){
        $scope.identityResource.get({identityId:id}).
            $promise.then(
            function(data) {
                $rootScope.identity = data;
                $rootScope.openForm("/assets/user/form/profile.html");
            });
    }

    /**
     * Authorization
     */
    $rootScope.getAuthorizedRoles = function(userIdVal) {
        if($scope.signed){
            $rootScope.userAuthResource.query({userId:userIdVal}).$promise.then(function(data) {
                $rootScope.roleList = data;
                $rootScope.isAdmin=$rootScope.isAuthorized('ADMIN', 'MANAGER');
            });
        };
    }


    $rootScope.getAuthorizedRoles();

    $rootScope.isAuthorized = function(role, ext){
        var result = false;
        if (!angular.isDefined(ext)) {
            var ra = role.split('_');
            if (ra.length>1) {
                role = ra[0];
                ext = ra[1];
            }
            else {
                ext = 'READ';
            }
        }
        if (angular.isDefined($rootScope.roleList)) {
            $rootScope.roleList.forEach(function(entry) {
                if(entry.serviceCode == (role) && entry.serviceExtension.indexOf(ext)>-1){
                    result = true;
                }
            });
        }
        return result;
    };

    $rootScope.openForm = function(url){
        $rootScope.message =[];
        $rootScope.formUrl = url;
        $("#modalBody").modal('show');
    }

    $rootScope.getFormUrl = function() {
        return $rootScope.formUrl;
    }

    //DEFINIÇÔES PARA PLUGINS

    $scope.getQuestionTab = function() { return $scope.questionTab; }

    $scope.setQuestionTab = function(value) { $scope.questionTab = value; }

    $scope.isQuestionTabSet = function(value) { return $scope.questionTab === value; }

    $scope.getResponseTab = function() { return $scope.responseTab; }

    $scope.setResponseTab = function(value) { $scope.responseTab = value; }

    $scope.isResponseTabSet = function(value) { return $scope.responseTab === value; }

    $scope.getTopicTab = function() { return $scope.topicTab; }

    $scope.setTopicTab = function(value) { $scope.topicTab = value; }

    $scope.isTopicTabSet = function(value) { return $scope.topicTab === value; }

    $scope.setTopicTab1 = function(value) { $scope.topicTab1 = value; }

    $scope.isTopicTabSet1 = function(value) { return $scope.topicTab1 === value; }

    $scope.setTopicTab2 = function(value) { $scope.topicTab2 = value; }

    $scope.isTopicTabSet2 = function(value) { return $scope.topicTab2 === value; }

    $scope.setTopicTab3 = function(value) { $scope.topicTab3 = value; }

    $scope.isTopicTabSet3 = function(value) { return $scope.topicTab3 === value; }

    $scope.setCurrentCriterion = function(value) {
        $scope.currentCriterion = value;
    }

    $scope.isCurrentCriterionSet = function(value) { return $scope.currentCriterion === value; }

}]);

