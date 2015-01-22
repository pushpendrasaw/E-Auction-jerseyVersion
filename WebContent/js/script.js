var eAuctionApp = angular.module('eAuctionApp',[]);
$(document).ready(function() {
	ajaxObj = {  
		type: "GET",
		url: "http://localhost:8080/E-Auction/api/Login/confirmLogin", 
		contentType:"application/json",
		error: function(jqXHR, textStatus, errorThrown) {
			console.log("Error " + jqXHR.getAllResponseHeaders() + " " + errorThrown);
		},
		success: function(data) { 
				$('#loginstatus').html(data[0].MSG);
				console.log("loginstatus : "+data[0].MSG);
		},
		dataType: "json" //request JSON
	},
	$.ajax(ajaxObj);	
	
	$(document.body).on('click', '#logout', function(e) {
		window.location.href="http://localhost:8080/E-Auction/api/Login/logout";
		
		setTimeout( function() { $(location).attr('href','http://localhost:8080/E-Auction/index.html'); }, 200);		

	});
});

eAuctionApp.controller('NavController',function($scope,$http) {
	$http.get("http://localhost:8080/E-Auction/api/nav").success( function(response) {
		console.log(response);
	    $scope.navtabs = response;
	 });
});
