$(document).ready(function() {
	
	getMyAuctions();
	
});
function getMyAuctions() {
	
	var d = new Date()
		, n = d.getTime();
	
	ajaxObj = {  
			type: "GET",
			url: "http://localhost:8080/E-Auction/api/Bidding/myAuctions", 
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
				
				$('#get_my_auctions').html("<table class='table table-striped'>" +
										"<tbody><tr><th>Product </th><th>Description</th><th>Starting Bid </th><th>Maximum Bid Price </th><th>Bidder</th></tr>"+ html_string + "</tbody></table>");
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
				'<td class="product_Name">' + param.product_Name + '</td>' +
				'<td class="product_Disc">' + param.product_Disc + '</td>' +
				'<td class="initial_Bid">&#8377;' + param.initial_Bid + '</td>';
					var restD = getMaxPriceAndBuyerId(param.product_Id);
					console.log(" vadf :"+restD);
			tableData = tableData +	getMaxPriceAndBuyerId(param.product_Id);
			tableData = tableData + '</tr>';
			return tableData;
}

function getMaxPriceAndBuyerId(product_Id)
{
	var d = new Date()
	, n = d.getTime();
	var restData;
	ajaxObj = {
			type : "GET",
			url: "http://localhost:8080/E-Auction/api/Bidding/maxBid"+"/"+product_Id,
			contentType:"application/json",
			error: function(jqXHR,textStatus,errorThrown){},
			success: function(data){
				  $.each(data, function(i, object) {
					    restData = [ object.maxBidd, object.buyer_Id ];
				  });   
			},
			complete : function(XMLHttpRequest){},
			dataType : "json"
		}
		$.ajax(ajaxObj);
	return restData;
}
