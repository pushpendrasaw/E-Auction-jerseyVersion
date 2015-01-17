$(document).ready(function() {
	
	var $update_form = $('#update_form');
	$update_form.hide();
	
	getItems();

	
	$(document.body).on('click', '.edit', function(e) {
		//console.log(this);
		$update_form.show();
		var $this = $(this)
			, $tr = $this.closest('tr')
			, product_Id = $tr.find('.product_Id').text()
			, product_Name = $tr.find('.product_Name').text()
			, product_Disc = $tr.find('.product_Disc').text()
			, initial_Bid = $tr.find('.initial_Bid').text()
			, last_Bid_Time = $tr.find('.last_Bid_Time').text();
		
		$('#form_product_Id').val(product_Id);
		$('#form_product_Name').text(product_Name);
		$('#form_product_Disc').text(product_Disc);
		$('#form_initial_Bid').text(initial_Bid);
		$('#form_last_Bid_Time').text(last_Bid_Time);
		
		$('#update_response').text("");
	});
	
	$update_form.submit(function(e) {
		e.preventDefault(); //cancel form submit
		
		var obj = $update_form.serializeObject()
			, id = $('#form_product_Id').val()
			, desc = $('#form_product_Disc').val()
			, price = $('#form_initial_Bid').val()
			, lastBidTime = $('#form_last_Bid_Time').val();
		
		updateItem(obj, id, desc, price,lastBid);
	});
	
	$(document.body).on('click', '.remove', function(e) {
		var $this = $(this)
		, $tr = $this.closest('tr')
		, product_Id = $tr.find('.product_Id').text();
		if(confirm("Are you sure you want to delete ?")) {
			deleteItem(product_Id);
		}
	});
});

function updateItem(obj, id, desc, price,lastBid) {
	console.log(JSON.stringify(obj));
	ajaxObj = {  
			type: "PUT",
			url: "http://localhost:8080/E-Auction/api/item/" + id + "/" + desc + "/" + price + "/" + lastBid,
			data: JSON.stringify(obj), 
			contentType:"application/json",
			error: function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.responseText);
			},
			success: function(data) {
				//console.log(data);
				$('#update_response').text( data[0].MSG );
			},
			complete: function(XMLHttpRequest) {
				//console.log( XMLHttpRequest.getAllResponseHeaders() );
				getItems();
			}, 
			dataType: "json" //request JSON
		};
		
	return $.ajax(ajaxObj);
}

function deleteItem(id) {
	ajaxObj = {  
			type: "DELETE",
			url: "http://localhost:8080/E-Auction/api/item/" + id,
			contentType:"application/json",
			error: function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.responseText);
			},
			success: function(data) {
				//console.log(data);
				$('#delete_response').text( data[0].MSG );
			},
			complete: function(XMLHttpRequest) {
				//console.log( XMLHttpRequest.getAllResponseHeaders() );
				getItems();
			}, 
			dataType: "json" //request JSON
		};
		
	return $.ajax(ajaxObj);
}




function getItems() {
	
	var d = new Date()
		, n = d.getTime();
	
	ajaxObj = {  
			type: "GET",
			url: "http://localhost:8080/E-Auction/api/item/all", 
			data: "ts="+n, 
			contentType:"application/json",
			error: function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.responseText);
			},
			success: function(data) { 
				//console.log(data);
				var html_string = "";
				
				$.each(data, function(index1, val1) {
					//console.log(val1);
					html_string = html_string + templateGetInventory(val1);
				});
				
				$('#get_items').html("<table class='table table-striped table-bordered'>" +
										"<tbody><tr><th>Item</th><th>Desc</th><th>Price</th><th>Sold</th><th></th><th></th></tr>"+ html_string + "</tbody></table>");
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
				'<td class="product_Id hidden">' + param.product_Id + '</td>' +
				'<td class="product_Name">' + param.product_Name + '</td>' +
				'<td class="product_Disc">' + param.product_Disc + '</td>' +
				'<td class="initial_Bid">' + param.initial_Bid + '</td>' +
				'<td class="product_count">' + param.count + ' times</td>' +
//				'<td><a href="#" class="edit"><span class="glyphicon glyphicon-pencil"></span></a></td>' +
//				'<td><a href="#" class="remove"><span class="glyphicon glyphicon-remove"></span></a></td>' +
			'</tr>';
}

