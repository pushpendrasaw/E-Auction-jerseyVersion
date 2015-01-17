$(document).ready(function() {
	
	var $inventory = $('#buy_item');
	
	getInventory();
	
	$(document.body).on('click', ':button, .viewItem', function(e) {
		//console.log(this);
		var $this = $(this)
			, product_Id = $this.val()
			, $tr = $this.closest('tr')
			, product_Name = $tr.find('.product_Name').text()
			, product_Disc = $tr.find('.product_Disc').text()
			, initial_Bid = $tr.find('.initial_Bid').text();
		
		$('#product_Name').text(product_Name);
		$('#product_Disc').text(product_Disc);
		$('#initial_Bid').text(initial_Bid);
		
		$('#update_response').text("");
		$('#buy_item').html("<input type='hidden' id='product_Id' value='"+ product_Id +"' /><input type='submit' id='submit_it' value='Buy Now' class='btn btn-primary'/>");
	});
	
	$inventory.submit(function(e) {
		e.preventDefault(); //cancel form submit
		
		var obj = $inventory.serializeObject(),
			product_Id = $('#product_Id').val();
		
		buyItem(obj,product_Id);
	});

});
function buyItem(obj,product_Id) {
	
	ajaxObj = {  
			type: "POST",
			url: "http://localhost:8080/E-Auction/api/transaction/new/" + product_Id,
			data: JSON.stringify(obj), 
			contentType:"application/json",
			error: function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.responseText);
				$('#update_response').text( "Error" );
			},
			success: function(data) {
				//console.log(data);
				$('#update_response').text( data[0].MSG );
			},
			complete: function(XMLHttpRequest) {
				//console.log( XMLHttpRequest.getAllResponseHeaders() );
				getInventory();
			}, 
			dataType: "json" //request JSON
		};
		
	return $.ajax(ajaxObj);
}

function getInventory() {
	
	var d = new Date()
		, n = d.getTime();
	
	ajaxObj = {  
			type: "GET",
			url: "http://localhost:8080/E-Auction/api/item/inventory", 
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
				//console.log( XMLHttpRequest.getAllResponseHeaders() );
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

