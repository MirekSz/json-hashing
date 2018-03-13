var phonecatApp = angular.module('killers', ['ngAnimate']);
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
		filterKillers($scope.state, type);
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
		return item.duration + ' minutes'
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
		duration = moment().diff(moment(item.startDate), 'minutes');
	}
	item.duration = duration;
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
		differ: '='
	},
	controller: function ($scope, $element) {
		this.$onInit = function () {
			if($scope.$ctrl.differ){
				$scope.style = {height: "200px"}
				$scope.lastData = 0;
			}
			setTimeout(function () {
				$scope.$ctrl.chart = createChart($($($element).find("canvas")[0]),$scope.$ctrl.differ);
			}, 0);
		};
		$scope.$watch('$ctrl.data', function (newValue, oldValue) {
			if (!$scope.$ctrl.chart) {
				return
			}
			if($scope.$ctrl.differ){
				if(oldValue==null){
					updateChart($scope.$ctrl.chart, newValue);
				}else{
					updateChart($scope.$ctrl.chart, newValue - oldValue);
				}
			}else {
				updateChart($scope.$ctrl.chart, newValue);
			}
		}, true);
	},
	templateUrl: 'app/metric.html'
});
phonecatApp.component('list', {
	bindings: {
		data: '=',
		title: '=',
	},
	templateUrl: 'app/list.html'
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
	templateUrl: 'app/killers.html'
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
			labels: [],
			datasets: [{
				data: [],
				label: "Africa",
				borderColor: "#3e95cd",
				fill: false
			}
			]
		},
		options:options
	});
}

function updateChart(chart, val) {
	var tzoffset = (new Date()).getTimezoneOffset() * 60000; //offset in milliseconds
	var localISOTime = (new Date(Date.now() - tzoffset)).toISOString().slice(0, 16);

	chart.data.labels.push(localISOTime);
	chart.data.datasets.forEach((dataset) => {
		dataset.data.push(val);
	});
	chart.update();
}
phonecatApp.component('vkApp', {
	templateUrl: 'app/main.html'
});
$(document).ready(function(){
	$("#vkApp").html('<div><vk-app/></div>')
	angular.element(function() {
      angular.bootstrap(document, ['killers']);
    });
})
