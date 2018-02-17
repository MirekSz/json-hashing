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

var phonecatApp = angular.module('killers', []);


phonecatApp.controller('KillersController', function CartController($scope,$interval,$timeout) {
	var source = new EventSource('/stream');
		source.onmessage = function(event) {
			var json = JSON.parse(event.data)
			 $timeout( function(){
			$scope.state={members:json.members,backups:json.backups,local:json.local,size:json.size,membersView:json.membersView}
			 }, 1 );
		}
		
	$scope.state={members:0,backups:0,local:0,size:0};
	
});

phonecatApp.component('metric', {
	  bindings: {
	    icon: '=',
	    data: '=',
	    title: '=',
	  },
	  controller: function ($scope,$element) {
		  this.$onInit = function () {
			 console.log($($($element).find("canvas")[0]))
			 setTimeout(function(){
			 $scope.$ctrl.chart = createChart($($($element).find("canvas")[0]));
			 },0);
		    };
		    $scope.$watch('$ctrl.data', function() { 
		    	if($scope.$ctrl.chart){
		    	updateChart($scope.$ctrl.chart,$scope.$ctrl.data);
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

function createChart(target){
	return new Chart(target, {
		  type: 'line',
		  data: {
		    labels: [1],
		    datasets: [{ 
		        data: [1],
		        label: "Africa",
		        borderColor: "#3e95cd",
		        fill: false}
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

function updateChart(chart,val){
	  chart.data.labels.push(new Date().toISOString().slice(0, 16));
	    chart.data.datasets.forEach((dataset) => {
	        dataset.data.push(val);
	    });
	    chart.update();
		}
