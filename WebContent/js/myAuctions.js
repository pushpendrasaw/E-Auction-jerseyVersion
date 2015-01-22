$(document).ready(function() {
	$("#update_form").hide();
	getMyAuctions();

	/**
	 * To Edit the product 
	 */
	$(document.body).on('click', '.edit_Auction', function(e) {
		//console.log(this);
		$("#update_form").show();
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
		$('#form_initial_Bid').val(initial_Bid);
		$('#form_last_Bid_Time').text(last_Bid_Time);
		$('#update_response').text("");
	});
	
	/**
	 * to remove an auction product
	 */
	$(document.body).on('click', '.remove_Auction', function(e) {
		var $this = $(this)
		, $tr = $this.closest('tr')
		, product_Id = $tr.find('.product_Id').text();
		console.log(" Product ID :"+product_Id);
		if(confirm("Are you sure you want to delete ?")) {
			deleteProduct(product_Id);
		}
	});
	
	
	/** 
	 *  To Update form Submit button for My Auction
	 */ 
	$(document.body).on( 'click','#update_Submit', function(e) {
		e.preventDefault(); //cancel form submit
		console.log(" submit : "+e);
		var obj = $("#update_form").serializeObject()
			, id = $('#form_product_Id').val()
			, disc = $('#form_product_Disc').val()
			, ib= $('#form_initial_Bid').val()
			, lbt = $('#form_last_Bid_Time').val();
		
		updateProduct(obj, id, disc, ib,lbt);
	});
});


/**
 * FUNCTION TO DELETE THE PRODUCT FROM AUCTION 
 * @param product_Id
 * @returns
 */
function deleteProduct(product_Id) {
	ajaxObj = {  
			type: "DELETE",
			url: "http://localhost:8080/E-Auction/api/Delete/product" + product_Id,
			contentType:"application/json",
			error: function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.responseText);
			},
			success: function(data) {
				$('#delete_response').text( data[0].MSG );
			},
			complete: function(XMLHttpRequest) {
				getMyAuctions();
			}, 
			dataType: "json" //request JSON
		};
		
	return $.ajax(ajaxObj);
}

/**
 * Function to Retrieve All Auction Product of Specific User
 * @returns
 */
function getMyAuctions() {
	
	var d = new Date()
		, n = d.getTime();
	
	ajaxObj = {  
			type: "GET",
			url: "http://localhost:8080/E-Auction/api/product/myAuctions", 
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
					html_string = html_string + templateGetAuctionList(val1);
				});
				
				$('#get_my_auctions').html("<table id='my_Auction_Table' class='table table-striped'>" +
										"<tbody><tr><th>Product </th><th>Description</th><th>Starting Bid </th><th>Last Bidding Date</th><th>Maximum Bid Price </th><th>Bidder</th><th></th><th></th></tr>"+ html_string + "</tbody></table>");
			},
			complete: function(XMLHttpRequest) {
				//console.log( XMLHttpRequest.getAllResponseHeaders() );
			}, 
			dataType: "json" //request JSON
		};
		
	return $.ajax(ajaxObj);
}

function templateGetAuctionList(param) {
	var tableData = '<tr>' +
				'<td class="product_Id hidden">' + param.product_Id + '</td>' +
				'<td class="product_Name">' + param.product_Name + '</td>' +
				'<td class="product_Disc">' + param.product_Disc + '</td>' +
				'<td class="initial_Bid">&#8377;' + param.initial_Bid + '</td>'+
				'<td class="last_Bid_Time">&#8377;' + param.last_Bid_Time + '</td>';
		if(param.maxBid==null && param.buyer_Id==null)
				tableData =  tableData + '<td class="max_Bid" colspan="2" style="text-align:center;">  Not Yet Bidding </td>';
		else
			tableData = tableData + '<td class="max_Bid">' + param.maxBid + '</td>' +
						'<td class="buyer_Id">' + param.buyer_Id + '</td>';
			tableData = tableData +
						'<td><a href="#" class="edit_Auction"><span class="glyphicon glyphicon-pencil"></span></a></td>' +
						'<td><a href="#" class="remove_Auction" ><span class="glyphicon glyphicon-remove"></span></a></td>' +
						'</tr>';
			return tableData;
}


/*** Function to update the Auction product by seller ***/
function updateProduct(obj,product_Id, product_Disc, initial_Bid,last_Bid_Time) {
	console.log(JSON.stringify(obj));
	ajaxObj = {  
			type: "PUT",
			url: "http://localhost:8080/E-Auction/api/Update/product/" + product_Id + "/" + product_Disc +"/"+ initial_Bid+"/"+ last_Bid_Time,
			data: JSON.stringify(obj), 
			contentType:"application/json",
			error: function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.responseText);
			},
			success: function(data) {
				$('#update_response').text( data[0].MSG );
			},
			complete: function(XMLHttpRequest) {
				getMyAuctions();
			}, 
			dataType: "json" //request JSON
		};
	return $.ajax(ajaxObj);
}