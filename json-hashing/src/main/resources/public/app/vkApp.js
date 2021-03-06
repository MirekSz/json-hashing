var phonecatApp = angular.module('killers', ['ngAnimate']);
phonecatApp.run(function($rootScope, $templateCache) {
	   $rootScope.$on('$viewContentLoaded', function() {
		   debugger
	      $templateCache.removeAll();
	   });
	});
function loadData($scope){
	$.get( "state", function( json ) {
		json.counters = { ok: 0, warnings: 0, dangers: 0, unfinished: 0 }
		json.killers.forEach(calculateDuration);
		countCategories(json.killers, json.counters);
		$scope.state=json;
	});
}
phonecatApp.controller('KillersController', function CartController($scope, $interval, $timeout) {
	loadData($scope);
	$interval(function(){
		loadData($scope);
	},10000)
	$scope.state = { members: 0, backups: 0, local: 0, size: 0, membersView: [] };
	$scope.filter = function (type) {
		$scope.filterBy = type;
	}
});
phonecatApp.filter('filterByType', function() {
	  return function(killers,type) {
		  if (type === 'd') {
				return killers.filter(isDangerTime);
			} else if (type === 'w') {
				return killers.filter(isWarnTime);
			} else if (type === 'o') {
				return killers.filter(isOkTime);
			} else if (type === 'u') {
				return killers.filter(function (e) { return e.stopDate == null; });
			}
			return killers;
	  }
});
phonecatApp.filter('humanize', function () {
	return function (item) {
		return moment.duration(item.duration, "minutes").humanize();
	}
});
function isOkTime(item) {
	return item.duration < 5;
}
function isWarnTime(item) {
	return item.duration >= 5 && item.duration < 20;
}
function isDangerTime(item) {
	return item.duration >= 20;
}

function calculateDuration(item) {
	var duration = 0;
	if (item.stopDate) {
		duration = moment(item.stopDate).diff(moment(item.startDate), 'minutes');
	} else {
		duration = moment(item.startDate).diff(moment(), 'minutes');
	}
	item.duration = Math.abs(duration);
}
function countCategories(killers, counters) {
	killers.forEach(function (item) {
		if (!item.stopDate) {
			counters.unfinished++;
		}
		if (isDangerTime(item)) {
			counters.dangers++;
			return;
		}
		if (isWarnTime(item)) {
			counters.warnings++;
			return;
		}
		counters.ok++;
	})
}
phonecatApp.component('metric', {
	bindings: {
		icon: '=',
		data: '=',
		title: '=',
		differ: '=',
		height: '=',
		initialValues:'='
	},
	controller: function ($scope, $element) {
		this.$onInit = function () {
			if($scope.$ctrl.height){
				$scope.style = {height: $scope.$ctrl.height}
			}
			if($scope.$ctrl.differ){
				$scope.style = {height: "200px"}
				$scope.lastData = 0;
			}
			setTimeout(function () {
				$scope.$ctrl.chart = createChart($($($element).find("canvas")[0]), $scope.style);
				var groups = _.groupBy($scope.$ctrl.initialValues, function (el) {
					  return moment(el.startDate).startOf('minutes').format();
				});
				_.forOwn(groups, function(value, key) {
					updateChart($scope.$ctrl.chart, value.length, key);
				});
			}, 0);
		};
		$scope.$watch('$ctrl.data', function (newValue, oldValue) {
			if (!$scope.$ctrl.chart) {
				return
			}
			if($scope.$ctrl.differ){
				if(oldValue != null){
					updateChart($scope.$ctrl.chart, newValue - oldValue);
				}
			}else {
				updateChart($scope.$ctrl.chart, newValue);
			}
		}, true);
	},
	templateUrl: 'app/metric.html?v=2'
});
phonecatApp.component('list', {
	bindings: {
		data: '=',
		title: '=',
	},
	templateUrl: 'app/list.html?v=2'
});
phonecatApp.component('killers', {
	bindings: {
		data: '<',
		title: '=',
		showType: '=',
	},
	controller: function ($scope) {
		this.$onInit = function () {
			$scope.limit=100;
		};
		$scope.sortByDate = function (item) {
			var date = Date.parse(item.startDate);
			return date;
		};
		this.showParams = function (item) {
			item.showParams = !item.showParams;
		}
		this.calculateClassFromDuration = function (item) {
			if (isDangerTime(item)) return 'text-danger';
			if (isWarnTime(item)) return 'text-warning';
			return '';
		}
		this.loadAll = function(){
			$scope.limit = $scope.$ctrl.data.length;
		}
	},
	templateUrl: 'app/killers.html?v=2'
});

function createChart(target,displayX) {
	var options= {
		scales: {
			xAxes: [{
				display: displayX,
			}],
			yAxes: [{
				display: true,
				ticks: {
				    min: 0,
				    beginAtZero: true,
				    callback: function(value, index, values) {
				        if (Math.floor(value) === value) {
				            return value;
				        }
				    }
				}
			}],
		},
		legend: {
			display: false
		},
		title: {
			display: false,
		}
	}
	if(displayX){
		options.responsive= true;
		options.maintainAspectRatio= false;
	}
	return new Chart(target, {
		type: 'line',
		data: {
			labels: [' '],
			datasets: [{
				data: [0],
				label: "Africa",
				borderColor: "#3e95cd",
				fill: false
			}
			]
		},
		options:options
	});
}

function updateChart(chart, val, date) {
	var tzoffset = (new Date()).getTimezoneOffset() * 60000; //offset in milliseconds
	var event = date!=null?new Date(date):Date.now();
	var localISOTime = (new Date(event - tzoffset)).toISOString().slice(0, 16);
	var needRemove = chart.data.labels.length > 100;

	chart.data.labels.push(localISOTime);
	chart.data.datasets.forEach((dataset) => {
		if(needRemove){
			dataset.data.shift()
		}
		dataset.data.push(val);
	});
	if(needRemove){
		chart.data.labels.shift()
	}
	chart.update();
}
phonecatApp.component('vkApp', {
	templateUrl: 'app/main.html?v=2'
});
$(document).ready(function(){
	$("#vkApp").html('<div><vk-app/></div>')
	angular.element(function() {
      angular.bootstrap(document, ['killers']);
    });
})
