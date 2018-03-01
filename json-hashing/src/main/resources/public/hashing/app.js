var hashingApp = angular.module('hashing', [ 'ngAnimate' ]);

hashingApp.controller('HashingController', function HashingController($scope,
		$interval, $timeout,$http) {
	$scope.submit = function() {
		var request = prepareJSONRequest($scope.person);
		$scope.request=request
		$http({
			  headers: {
			        "se":"first value",
			    },
		    method: 'POST',
		    url: 'greeting',
		    data: request
		}) .then(function (res) {
			$scope.result=res.data
        });
		console.log($scope.form);
	};
});

hashingApp.directive('username', function($q, $timeout) {
	  return {
	    require: 'ngModel',
	    link: function(scope, elm, attrs, ctrl) {
	      var usernames = ['Jim', 'John', 'Jill', 'Jackie'];

	      ctrl.$asyncValidators.username = function(modelValue, viewValue) {

	        if (ctrl.$isEmpty(modelValue)) {
	          // consider empty model valid
	          return $q.resolve();
	        }

	        var def = $q.defer();

	        $timeout(function() {
	          // Mock a delayed response
	          if (usernames.indexOf(modelValue) === -1) {
	            // The username is available
	            def.resolve();
	          } else {
	            def.reject();
	          }

	        }, 2000);

	        return def.promise;
	      };
	    }
	  };
	});