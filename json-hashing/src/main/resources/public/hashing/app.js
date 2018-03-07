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

var model=[{"name":"find","paramsTypes":["pl.com.stream.verto.cmm.customer.server.pub.main.CustomerId"],"resultType":"java.lang.Long"},{"name":"findFirstCustomerByStringAttribureValueForAttributeDefinition","paramsTypes":["java.lang.Long","java.lang.String"],"resultType":"java.lang.Long"},{"name":"getIdCustomerOriginKind","paramsTypes":["java.lang.Long"],"resultType":"java.lang.Long"},{"name":"getIdCustomerByIdPayer","paramsTypes":["java.lang.Long"],"resultType":"java.util.List"},{"name":"findCustomerByShortcutName","paramsTypes":["java.lang.String"],"resultType":"java.lang.Long"},{"name":"findCustomerByTinAndTinSuffix","paramsTypes":["java.lang.String","java.lang.String"],"resultType":"java.lang.Long"},{"name":"findCustomerByNumber","paramsTypes":["java.lang.Integer"],"resultType":"java.lang.Long"},{"name":"findCustomerByCustomerSearchKey","paramsTypes":["pl.com.stream.verto.cmm.customer.server.pub.main.CustomerSearchKey"],"resultType":"java.lang.Long"},{"name":"findCustomerByGln","paramsTypes":["java.lang.String"],"resultType":"java.lang.Long"},{"name":"findCustomerByOutsideCustNumber","paramsTypes":["java.lang.String"],"resultType":"java.lang.Long"},{"name":"getIdCustomerByIdBusDicItem","paramsTypes":["java.lang.Long"],"resultType":"java.lang.Long"},{"name":"getDefaultFaxNumber","paramsTypes":["java.lang.Long"],"resultType":"java.lang.String"},{"name":"getFirstBankAccountId","paramsTypes":["java.lang.Long"],"resultType":"java.lang.Long"},{"name":"findCustomerSearchKeyOutputData","paramsTypes":["pl.com.stream.verto.cmm.customer.server.pub.main.CustomerSearchKey"],"resultType":"pl.com.stream.verto.cmm.customer.server.pub.main.CustomerSearchKeyOutputData"},{"name":"checkCustomerTinInVIES","paramsTypes":["java.lang.Long"],"resultType":"boolean"},{"name":"setAttributeValue","paramsTypes":["java.lang.Long","java.lang.Long","java.lang.Object"],"resultType":"void"},{"name":"getCustomerContactSourceObject","paramsTypes":["java.lang.Long"],"resultType":"pl.com.stream.verto.cmm.contact.server.pub.address.filter.ContactSourceObject"},{"name":"getNextUniqueTinSuffix","paramsTypes":["java.lang.String"],"resultType":"java.lang.String"},{"name":"getDefaultEmailAddress","paramsTypes":["java.lang.Long"],"resultType":"java.lang.String"},{"name":"getDefaultPhoneNumber","paramsTypes":["java.lang.Long"],"resultType":"java.lang.String"},{"name":"getIdSettlementSubjectByIdCustomer","paramsTypes":["java.lang.Long"],"resultType":"java.lang.Long"},{"name":"findCustomerByTinAndTinSuffixAndCountryCode","paramsTypes":["java.lang.String","java.lang.String","java.lang.String"],"resultType":"java.lang.Long"},{"name":"getCustomerContactSourceCollection","paramsTypes":["java.lang.Long"],"resultType":"pl.com.stream.lib.commons.collection.LongCollection"},{"name":"getIdCustomerByIdSettlementSubject","paramsTypes":["java.lang.Long"],"resultType":"java.lang.Long"},{"name":"getCustomerId","paramsTypes":["pl.com.stream.verto.cmm.customer.server.pub.main.CustomerId"],"resultType":"java.lang.Long"},{"name":"setPhoneNumber","paramsTypes":["java.lang.Long","java.lang.Long","java.lang.String"],"resultType":"java.lang.Long"},{"name":"isFactor","paramsTypes":["java.lang.Long"],"resultType":"java.lang.Boolean"},{"name":"isTrade","paramsTypes":["java.lang.Long"],"resultType":"java.lang.Boolean"},{"name":"update","paramsTypes":["pl.com.stream.verto.cmm.customer.server.pub.main.CustomerDto"],"resultType":"void"},{"name":"apply","paramsTypes":["pl.com.stream.next.asen.common.dto.FieldsValues"],"resultType":"java.lang.Long"},{"name":"find","paramsTypes":["java.lang.Long"],"resultType":"pl.com.stream.verto.cmm.customer.server.pub.main.CustomerDto"},{"name":"init","paramsTypes":["pl.com.stream.verto.cmm.customer.server.pub.main.CustomerDto"],"resultType":"pl.com.stream.verto.cmm.customer.server.pub.main.CustomerDto"},{"name":"delete","paramsTypes":["java.lang.Long"],"resultType":"void"},{"name":"delete","paramsTypes":["pl.com.stream.verto.cmm.customer.server.pub.main.CustomerDto"],"resultType":"void"},{"name":"insert","paramsTypes":["pl.com.stream.verto.cmm.customer.server.pub.main.CustomerDto"],"resultType":"java.lang.Long"},{"name":"validate","paramsTypes":["pl.com.stream.verto.cmm.customer.server.pub.main.CustomerDto"],"resultType":"void"},{"name":"calculateDto","paramsTypes":["pl.com.stream.verto.cmm.customer.server.pub.main.CustomerDto","java.lang.String"],"resultType":"pl.com.stream.verto.cmm.customer.server.pub.main.CustomerDto"},{"name":"calculateFlags","paramsTypes":["pl.com.stream.verto.cmm.customer.server.pub.main.CustomerDto"],"resultType":"pl.com.stream.verto.cmm.customer.server.pub.main.CustomerFlags"},{"name":"getDataSetRights","paramsTypes":["pl.com.stream.verto.cmm.customer.server.pub.main.CustomerDto"],"resultType":"pl.com.stream.next.asen.common.service.DataSetRights"},{"name":"validateField","paramsTypes":["pl.com.stream.verto.cmm.customer.server.pub.main.CustomerDto","java.lang.String"],"resultType":"pl.com.stream.lib.commons.datasource.dataset.validation.ValidationResult"},{"name":"refreshDef","paramsTypes":[],"resultType":"void"},{"name":"getDef","paramsTypes":[],"resultType":"pl.com.stream.next.asen.common.datasetdef.ServerDataSetDefForClient"},{"name":"handleClientException","paramsTypes":["pl.com.stream.next.asen.common.exceptions.client.AbstractServerException"],"resultType":"pl.com.stream.next.asen.common.exceptions.client.AbstractServerException"},{"name":"prevalidate","paramsTypes":["pl.com.stream.verto.cmm.customer.server.pub.main.CustomerDto"],"resultType":"pl.com.stream.lib.commons.datasource.dataset.validation.ValidationResult"},{"name":"afterEditComplete","paramsTypes":["pl.com.stream.next.asen.common.service.EditCompleteContext"],"resultType":"pl.com.stream.next.asen.common.service.EditCompleteResult"},{"name":"beforeEditComplete","paramsTypes":["pl.com.stream.next.asen.common.service.EditCompleteContext"],"resultType":"void"},{"name":"getLabel","paramsTypes":["java.lang.Long"],"resultType":"java.lang.String"}];
var service = {};
for(let m of model){
	service[m.name]=function(){
		var params = m.paramsTypes;
		var args = [];
		for(var i=0;i<params.length;i++){
args.push([params[i],arguments[i]])
		}
		console.log('Sending ',m.name,args)
		a.apply(a,args)
	}
}

function a(a1,a2,a3){
	console.log(a1,a2,a3)
}