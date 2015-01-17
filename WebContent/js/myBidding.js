$(document).ready(function() {
	
	getMyBidding();
	
	
});
function getMyBidding() {
	
	var d = new Date()
		, n = d.getTime();
	
	ajaxObj = {  
			type: "GET",
			url: "http://localhost:8080/E-Auction/api/Bidding/bidded", 
			data: "ts="+n, 
			contentType:"application/json",
			error: function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.responseText);
			},
			success: function(data) { 
				var html_string = "";
				$.each(data, function(index1, val1) {
					html_string = html_string + templateGetMyBidding(val1);
				});
				$('#get_My_Bidding').html("<table class='table table-hower '>" +
										"<tbody><tr><th>Auction</th><th>Auction Discription</th><th>Seller Name</th><th>Stating Bid</th><th> Bidding Price</th><th>Status</th></tr>"+ html_string + "</tbody></table>");
			},
			complete: function(XMLHttpRequest) {
			}, 
			dataType: "json" //request JSON
		};
		
	return $.ajax(ajaxObj);
}

function templateGetMyBidding(param) {
	var tableData= '<tr>' +
				'<td class="product_Name">' + param.product_Name + '</td>' +
				'<td class="product_Disc">' + param.product_Disc + '</td>' +
				'<td class="sellername">' + param.sellername + '</td>' +
				'<td class="initial_Bid">&#8377;' + param.initial_Bid + '</td>' +
				'<td class="buyer_Price">&#8377;' + param.buyer_Price + '</td>';
				if(isBidLow(param.product_Id,param.buyer_Price))
					tableData = tableData +
					'<td class="bid_Status"><span class="glyphicon glyphicon-thumbs-down" value="'+param.product_Id+'"></span></td>';
				else if(isBidHigh(param.product_Id,param.buyer_Price))
					tableData = tableData +
					'<td class="bid_Status"><span class="glyphicon glyphicon-thumbs-up" value="'+param.product_Id+'"></span></td>';
				else
					tableData = tableData +
					'<td class="bid_Status"><span class="glyphicon glyphicon-hand-left" value="'+param.product_Id+'"></span></td>';
			
				tableDate = tableData + '</tr>';
	return tableData;
}

function isBidLow(product_Id,buyer_Price){
	console.log("bidlow :"+product_Id);
	var d = new Date()
	, n = d.getTime();

	ajaxObj = {  
			type: "GET",
			url: "http://localhost:8080/E-Auction/api/Bidding/maxBid/"+product_Id, 
			data: "ts="+n, 
			contentType:"application/json",
			error: function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.responseText);
			},
			success: function(data) {
				$.each(data, function(index1, val1) {
							//	console.log("asd :"+val1[0].maxBidd);
					});
				console.log("success Bid");
			},
			complete: function(XMLHttpRequest) {
			}, 
			dataType: "json" //request JSON
		}
	$.ajax(ajaxObj);
	return false;
	}
function isBidHigh(product_Id,buyer_Price){
	
	var d = new Date()
	, n = d.getTime();

	ajaxObj = {  
			type: "GET",
			url: "http://localhost:8080/E-Auction/api/Bidding/maxBid/"+product_Id, 
			data: "ts="+n, 
			contentType:"application/json",
			error: function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.responseText);
			},
			success: function(data) {
				$.each(data, function(val1) {
								console.log(val1.buyer_Id);
					});
			},
			complete: function(XMLHttpRequest) {
			}, 
			dataType: "json" //request JSON
		}
	$.ajax(ajaxObj);
	return false;
	}
