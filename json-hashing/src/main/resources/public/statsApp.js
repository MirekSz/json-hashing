var phonecatApp = angular.module('killers', ['ngAnimate']);

phonecatApp.controller('KillersController', function CartController($scope, $interval, $timeout) {
	var source = new EventSource('/stream');
	source.onmessage = function (event) {
		var json = JSON.parse(event.data)
		json.killers.forEach(calculateDuration);
		countCategories(json);
		$timeout(function () {
			$scope.state = json;
		}, 1);
	}

	$scope.state = { members: 0, backups: 0, local: 0, size: 0, membersView: [] };

});

function calculateDuration(item){
	var duration=0;
	if (item.stopDate) {
		duration= moment(item.stopDate).diff(moment(item.startDate),'minutes');
	}else{
		duration= moment().diff(moment(item.startDate),'minutes');
	}
	item.duration=duration;
}
function countCategories(state){
	state.ok=0,state.warnings=0,state.dangers=0;
	state.killers.forEach(function(item){
		if(item.duration>5){
			state.warnings++;
			return;
		}
		if(item.duration>20){
			state.dangers++;
			return;
		}
		state.ok++;
	})
}
phonecatApp.filter('diff', function () {
	return function (item) {
		if (item.stopDate) {
			var a=moment(item.stopDate).diff(moment(item.startDate),'minutes');
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
				<i class="{{$ctrl.icon}} fa-4x"></i>
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
		this.calculateClassFromDuration = function(item) {
			if (item.duration > 5) return 'text-warning';
			if (item.duration > 20) return 'text-danger';
			return '';
		}
	},
	template: `
	<ul class="timeline">
	<li class="timeline-inverted repeat-item" ng-repeat="item in $ctrl.data|orderBy:sortByDate:true track by item.id ">
		<div class="timeline-badge" ng-class="{ 'success': item.type=='F', 'info': item.type=='A', 'warning': item.type=='T' }">
			{{item.type}}
		</div>
		<div class="timeline-panel">
			<div class="timeline-heading">
				<div class="float-right ">
					<i class="fas fa-user"></i> {{item.operator}}
					<span ng-if="item.startDate">
						<br/>
						<i class="fab fa-font-awesome-flag"></i> {{item.stopDate}}</span>
				</div>
				<h4 class="timeline-title" ng-class="$ctrl.calculateClassFromDuration(item)">{{item.startDate}} ({{item | diff}})</h4>
				<small class="text-muted" >
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
