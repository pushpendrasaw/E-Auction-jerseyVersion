$(document).ready(function() {
	ajaxObj = {  
			type: "GET",
			url: "http://localhost:8080/E-Auction/api/Login/confirmLogin", 
			contentType:"application/json",
			error: function(jqXHR, textStatus, errorThrown) {
				console.log("Error " + jqXHR.getAllResponseHeaders() + " " + errorThrown);
			},
			success: function(data) { 
				console.log("Data : in new item : CL :"+ data[0].CODE);
				if(data[0].CODE == 500) {
					$('#loggedInMsg').html("You are not logged in, Please login");
					$('#add_item').hide();
				}
			}, 
			dataType: "json" // request JSON
	},
	$.ajax(ajaxObj);		
	var $post_example = $('#add_item');
	$('#submit_it').click(function(e) {
		e.preventDefault(); // cancel form submit
		var jsObj = $post_example.serializeObject()
			, ajaxObj = {};
		ajaxObj = {  
			type: "POST",
			url: "http://localhost:8080/E-Auction/api/Registration/newAuction", 
			data: JSON.stringify(jsObj), 
			contentType:"application/json",
			error: function(jqXHR, textStatus, errorThrown) {
				console.log("Error " + jqXHR.getAllResponseHeaders() + " " + errorThrown);
				$('#div_ajaxResponse').text( "Error" );
			},
			success: function(data) { 
				if(data[0].CODE == 200) {
					$('#div_ajaxResponse').text( data[0].MSG );
					$(this).load("index.html");
				}
			},
			complete: function(XMLHttpRequest) {
			}, 
			dataType: "json" // request JSON
		};
		$.ajax(ajaxObj);
	});
});