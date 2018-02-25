var phonecatApp = angular.module('killers', ['ngAnimate']);

phonecatApp.controller('KillersController', function CartController($scope, $interval, $timeout) {
	var source = new EventSource('/stream');
	source.onmessage = function (event) {
		var json = JSON.parse(event.data)
		json.allKillers = json.killers;
		json.counters = { ok: 0, warnings: 0, dangers: 0, unfinished: 0 }
		json.killers.forEach(calculateDuration);
		countCategories(json.killers, json.counters);

		$timeout(function () {
			$scope.state = filterKillers(json, $scope.filterBy);
		}, 1);
	}

	$scope.state = { members: 0, backups: 0, local: 0, size: 0, membersView: [] };
	$scope.filter = function (type) {
		$scope.filterBy = type;
		filterKillers($scope.state, type);
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
function filterKillers(state, type) {
	if (type === 'd') {
		state.killers = state.allKillers.filter(isDangerTime);
	} else if (type === 'w') {
		state.killers = state.allKillers.filter(isWarnTime);
	} else if (type === 'o') {
		state.killers = state.allKillers.filter(isOkTime);
	} else if (type === 'u') {
		state.killers = state.allKillers.filter(function (e) { return e.stopDate == null; });
	} else if (type === 'c') {
		state.filterBy = null;
		state.killers = state.allKillers;
	}
	return state;
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
	},
	controller: function ($scope, $element) {
		this.$onInit = function () {
			setTimeout(function () {
				$scope.$ctrl.chart = createChart($($($element).find("canvas")[0]));
			}, 0);
		};
		$scope.$watch('$ctrl.data', function () {
			if ($scope.$ctrl.chart) {
				updateChart($scope.$ctrl.chart, $scope.$ctrl.data);
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
		data: '=',
		title: '=',
	},
	controller: function ($scope) {
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
	},
	templateUrl: 'app/killers.html'
});

function createChart(target) {
	return new Chart(target, {
		type: 'line',
		data: {
			labels: [1],
			datasets: [{
				data: [1],
				label: "Africa",
				borderColor: "#3e95cd",
				fill: false
			}
			]
		},
		options: {
			scales: {
				xAxes: [{
					display: false
				}],
				yAxes: [{
					display: true
				}],
			},
			legend: {
				display: false
			},
			title: {
				display: false,
			}
		}
	});
}

function updateChart(chart, val) {
	chart.data.labels.push(new Date().toISOString().slice(0, 16));
	chart.data.datasets.forEach((dataset) => {
		dataset.data.push(val);
	});
	chart.update();
}
