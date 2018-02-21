var phonecatApp = angular.module('killers', ['ngAnimate']);

phonecatApp.controller('KillersController', function CartController($scope, $interval, $timeout) {
	var source = new EventSource('/stream');
	source.onmessage = function (event) {
		var json = JSON.parse(event.data)
		$timeout(function () {
			$scope.state = json;
		}, 1);
	}

	$scope.state = { members: 0, backups: 0, local: 0, size: 0, membersView: [] };

});
phonecatApp.filter('diff', function () {
	return function (item) {
		if (item.stopDate) {
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
	template: `
				<i class="{{$ctrl.icon}} fa-5x"></i>
				<h4>
					{{$ctrl.title}}: <span class="text-primary">{{$ctrl.data}}</span>
				</h4>
				<canvas></canvas>
		  `
});
phonecatApp.component('list', {
	bindings: {
		data: '=',
		title: '=',
	},
	template: `
			<div class="card">
			<div class="card-header">{{$ctrl.title}}</div>
			<div class="card-body">
				<ul class="list-group list-group-flush">
					<li ng-repeat="item in $ctrl.data" class="list-group-item">{{item}}</li>
				</ul>
			</div>
		</div>
		`
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
	},
	template: `
	<ul class="timeline">
	<li class="timeline-inverted" ng-repeat="item in $ctrl.data|orderBy:sortByDate track by item.id ">
		<div class="timeline-badge success" ng-if="item.type=='f'">
			F
		</div>
		<div class="timeline-badge info" ng-if="item.type=='a'">
			A
		</div>
		<div class="timeline-badge warning" ng-if="item.type=='t'">
			T
		</div>
		<div class="timeline-panel">
			<div class="timeline-heading">
				<div class="float-right ">
					<i class="fas fa-user"></i> {{item.operator}}
					<span ng-if="item.startDate">
						<br/>
						<i class="fab fa-font-awesome-flag"></i> {{item.stopDate}}</span>
				</div>
				<h4 class="timeline-title">{{item.startDate}} ({{item | diff}})</h4>
				<small class="text-muted">
					<a ng-click="$ctrl.showParams(item)" href="">
						<i class="glyphicon glyphicon-time"></i>{{item.service}}_{{item.methodName}}</a>
				</small>
			</div>
			<div class="timeline-body">
				<textarea ng-if="item.showParams" rows="2" readonly class="form-control col-8">{{item.arguments}}</textarea>
			</div>
		</div>
	</li>
</ul>
		`
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
