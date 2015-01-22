$(document).ready(function() {
	
	var $inventory = $('#buy_item');
	getInventory();
});

function getInventory() {
	
	var d = new Date()
		, n = d.getTime();
	
	ajaxObj = {  
			type: "GET",
			url: "http://localhost:8080/E-Auction/api/product/products", 
			data: "ts="+n, 
			contentType:"application/json",
			error: function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.responseText);
			},
			success: function(data) { 
				console.log(data);
				var html_string = "";
				
				$.each(data, function(index1, val1) {
					console.log(val1);
					html_string = html_string + templateGetInventory(val1);
				});
				
				$('#get_inventory').html("<table class='table table-hover'>" +
										"<tbody><tr><th>Product Name</th><th>Product Discription</th><th>Starting Bid Price</th><th> Last Bidding Date </th><th></th></tr>"+ html_string + "</tbody></table>");
			},
			complete: function(XMLHttpRequest) {
			}, 
			dataType: "json" //request JSON
		};
		
	return $.ajax(ajaxObj);
}

function templateGetInventory(param) {
	return '<tr>' +
				'<td class="product_Name">' + param.product_Name + '</td>' +
				'<td class="product_Disc">' + param.product_Disc + '</td>' +
				'<td class="initial_Bid">&#8377;' + param.initial_Bid + '</td>' +
				'<td class="last_Bid_Time">' + param.last_Bid_Time + '</td>' +
			'</tr>';
}

