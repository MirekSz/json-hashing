//$(document).ready(function(){
//	var source = new EventSource('/stream');
//	var last=0;
//		source.onmessage = function(event) {
//			var json = JSON.parse(event.data)
//			console.log(json.puts)
//			console.log(json.members)
//			console.log(json.back)
//			
//			if(json.size!=last){
//				last=json.size;
//			  chart.data.labels.push(new Date().toISOString().slice(0, 16));
//		    chart.data.datasets.forEach((dataset) => {
//		        dataset.data.push(json.size);
//		    });
//			}
//		    chart.update();
//		}
//		var o=0;
//		setInterval(function(){
//			  chart.data.labels.push(new Date().toISOString().slice(0, 16));
//			    chart.data.datasets.forEach((dataset) => {
//			        dataset.data.push(o++);
//			    });
//			    chart.update();	
//		},10000)
//		
//
//
//})

var phonecatApp = angular.module('killers', ['ngAnimate']);


phonecatApp.controller('KillersController', function CartController($scope, $interval, $timeout) {
	var source = new EventSource('/stream');
	source.onmessage = function (event) {
		var json = JSON.parse(event.data)
		$timeout(function () {
			$scope.state= json;
		}, 1);
	}

	$scope.state = { members: 0, backups: 0, local: 0, size: 0,membersView:[] };

});

phonecatApp.component('metric', {
	bindings: {
		icon: '=',
		data: '=',
		title: '=',
	},
	controller: function ($scope, $element) {
		this.$onInit = function () {
			console.log($($($element).find("canvas")[0]))
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
	controller:function($scope){
		$scope.sortByDate = function(item) {
			debugger
		    var date = Date.parse(item.date);
		    return date;
		};
	},
	template: `
		<div class="card">
		<div class="card-header">{{$ctrl.title}}</div>
		<div class="card-body">
		<ul class="list-group list-group-flush">
		<li   ng-repeat="item in $ctrl.data|orderBy:sortByDate track by item.id "  class="list-group-item list-group-item-action flex-column align-items-start repeat-item">
    <div class="d-flex w-100 justify-content-between">
      <h6 class="text-info">{{item.method}}</h6>
      <div>
      <small class="text-danger">{{item.date}} </small>
      </div>
    </div>
    <textarea readonly class="float-left form-control col-7">{{item.params}}</textarea>
    <p class="mb-1"></small>
		<div class="float-right "><i class="fas fa-user"></i> Mirosław Szajowski</div>
  </li>
		</ul>
		</div>
		</div>
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
