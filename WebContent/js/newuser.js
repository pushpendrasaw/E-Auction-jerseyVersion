$(document).ready(function() {
	
	
	//console.log("ready");
	ajaxObj = {  
			type: "GET",
			url: "http://localhost:8080/E-Auction/api/Login/confirmLogin", 
			contentType:"application/json",
			error: function(jqXHR, textStatus, errorThrown) {
				console.log("Error " + jqXHR.getAllResponseHeaders() + " " + errorThrown);
			},
			success: function(data) { 
				//console.log(data);
				if(data[0].CODE == 200) {
					$('#loggedInMsg').html("You've already logged In");
					$('#add_user').hide();
				}
			},
			//complete: function(XMLHttpRequest) {
				//console.log( XMLHttpRequest.getAllResponseHeaders() );
			//}, 
			dataType: "json" //request JSON
	},
	$.ajax(ajaxObj);		

	
	var $post_example = $('#add_user');
		
	$('#submit_it').click(function(e) {
		//console.log("submit button has been clicked");
		e.preventDefault(); //cancel form submit
		
		var jsObj = $post_example.serializeObject()
			, ajaxObj = {};
		
		//console.log(jsObj);
		
		ajaxObj = {  
			type: "POST",
			url: "http://localhost:8080/E-Auction/api/Registration/newUser", 
			data: JSON.stringify(jsObj), 
			contentType:"application/json",
			error: function(jqXHR, textStatus, errorThrown) {
				console.log("Error " + jqXHR.getAllResponseHeaders() + " " + errorThrown);
			},
			success: function(data) { 
				if(data[0].CODE == 200) {
					$('#div_ajaxResponse').text( data[0].MSG );
					window.location.href = "login.html";
				}
			},
			complete: function(XMLHttpRequest) {
				//console.log( XMLHttpRequest.getAllResponseHeaders() );
			}, 
			dataType: "json" //request JSON
		};
		
		$.ajax(ajaxObj);
	});
	$("#username").keydown(function(){
		  $("#username").css("background-color","red");
		  var username = $("#username").text();
		  console.log(" user :"+username);
		  $("#username_Disc").html("now logged in");
		  $('#username_Disc.hidden').css('visibility','visible').hide().fadeIn().removeClass('hidden');
	});
	
});