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

hashingApp.component('field', {
	bindings: {
		form: '=',
		data: '=',
		name: '@',
	},
	controller: function ($scope) {
		this.$onInit = function () {
			console.log($scope)
		};
		this.change = function () {
			$scope.$ctrl.form[$scope.$ctrl.name].$setDirty();
		};
	},
	template: `
	  <div class="form-group">
    <label>
    Size (integer 0 - 10):</label>
    <input class="form-control" required type="number" ng-change="$ctrl.change()" ng-model="$ctrl.data[$ctrl.name]" name="age" ng-class="{'is-invalid':$ctrl.form[$ctrl.name].$invalid && $ctrl.form[$ctrl.name].$dirty ,'is-valid':$ctrl.form[$ctrl.name].$valid}"
           /><br />
<small class="form-text text-muted">
      </small>
  </div>
	`
});

hashingApp.directive('username', function($q, $timeout) {
	  return {
	    require: 'ngModel',
	    link: function(scope, elm, attrs, ctrl) {
	      var usernames = ['Jim', 'John', 'Jill', 'Jackie'];
	      ctrl.$asyncValidators.username = function(modelValue, viewValue) {
	    	  ctrl.$error={}
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
	        	  ctrl.$error.msg = 'Imie zajete'
	            def.reject();
	          }

	        }, 2000);

	        return def.promise;
	      };
	    }
	  };
	});