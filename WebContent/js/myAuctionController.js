var app = angular.module('eAuctionApp', []);
app.controller('myAuctionsTableController',function($scope,$http) {
	$http.get("http://localhost:8080/E-Auction/api/product/myAuctions").success( function(data) {
		if(data[0].CODE==500){
			$('#status').html(data[0].MSG);
			alert(data[0].MSG);
		}
		else 
			$scope.auctions = response;
	 });
});