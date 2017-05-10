storeModule.controller('itemsCtrl', ['$scope', 'itemService', 'list', function($scope, itemService, list ) {
        $scope.list = list;
    }]);

storeModule.controller('loginCtrlWithResource', ['$scope', 'loginService', '$http', '$location', function ($scope, loginService, $http, $location)
{
    $scope.token = null;
        $scope.login = function(){
            var service = new loginService({userName:$scope.userName});
            service.$save(function(response){
                $scope.token = response.token;
                $http.defaults.headers.common.Authorization = 'Bearer ' + $scope.token;
                console.log("token: " + $scope.token);

                if ( $scope.token !== null)
                {
                    $location.path('/items');
                }
            });
        }



    $scope.loggedIn = function() {
        return $scope.token !== null;
    }
}]);

storeModule.controller('itemCtrlWithResource', ['$scope', '$window', 'itemServiceWithResource', 'items', function($scope, $window, itemServiceWithResource, items){

    $scope.items = items;

        $scope.fetchAll = function(){
        	  $scope.items = itemServiceWithResource.query();
          };


    $scope.deleteItem = function(identity)
    {
        var service = new itemServiceWithResource({id:identity});
        service.$delete(function(){
            $scope.fetchAll();
        });
    }

    $scope.submit = function()
    {
        var item = {
            'name'      : $scope.name,
            'category'  : $scope.category,
            'uri'       : $scope.uri
        };
        var service = new itemServiceWithResource(item);
        service.$save(function(response){
                $window.location.href="/";
        });
    }


}]);

