var phonecatApp = angular.module('killers', ['ngAnimate']);

phonecatApp.controller('KillersController', function CartController($scope, $interval, $timeout) {
	var source = new EventSource('/stream');
	source.onmessage = function (event) {
		var json = JSON.parse(event.data)
		json.counters = { ok: 0, warnings: 0, dangers: 0 }
		json.killers.forEach(calculateDuration);
		countCategories(json.killers, json.counters);
		$timeout(function () {
			$scope.state = json;
		}, 1);
	}

	$scope.state = { members: 0, backups: 0, local: 0, size: 0, membersView: [] };
	this.filter = function (type) {
		if (type === 'd') {

			$scope.state.filteredKillers = $scope.state.killers.filter((el) => el.duration > 20);
		}
	}
});
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
		if (item.duration > 20) {
			counters.dangers++;
			return;
		}
		if (item.duration > 5) {
			counters.warnings++;
			return;
		}
		counters.ok++;
	})
}
phonecatApp.filter('diff', function () {
	return function (item) {
		if (item.stopDate) {
			var a = moment(item.stopDate).diff(moment(item.startDate), 'minutes');
			return moment.duration(moment(item.stopDate).diff(moment(item.startDate))).humanize()
		}
		return moment.duration(moment().diff(moment(item.startDate))).humanize()
	}
});
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
			if (item.duration > 20) return 'text-danger';
			if (item.duration > 5) return 'text-warning';
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
