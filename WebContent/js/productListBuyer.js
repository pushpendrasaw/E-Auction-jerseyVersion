$(document).ready(function() {
	
	getUnBidList();
	
	$(document.body).on('click', '.bidItem', function(e) {
		var product_Id = this.value;
		if(checkBidLastTime(product_Id)){
			var bidAmount = prompt(" Enter the Bid Amount : ");
		if(bidAmount != null)
			updateBidStatus(bidAmount,product_Id);
		}
		else
			alert(" Out of Auction");	
	});
});

function updateBidStatus(bidAmount,product_Id)
{
	var d = new Date()
	, n = d.getTime();
	
		ajaxObj = {  
			type: "GET",
			url: "http://localhost:8080/E-Auction/api/Auctions/searchMaxBid/"+product_Id+"/"+bidAmount, 
			data: "ts="+n, 
			contentType:"application/json",
			error: function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.responseText);
			},
			success: function(data) { 
				console.log(data[0].MSG);
				alert(data[0].MSG);
			},
			complete: function(XMLHttpRequest) {
			}, 
			dataType: "json" //request JSON
		};
		return $.ajax(ajaxObj);
}

function checkBidLastTime(product_Id){
	var d = new Date()
	, n = d.getTime();

		ajaxObj = {  
				type: "GET",
				url: "http://localhost:8080/E-Auction/api/Auctions/lastAuctionTime/"+product_Id, 
				data: "ts="+n, 
				contentType:"application/json",
				error: function(jqXHR, textStatus, errorThrown) {
					console.log(jqXHR.responseText);
				},
				success: function(data) { 
					if(data[0].CODE==200)
						return true;
				},
				complete: function(XMLHttpRequest) {
					console.log( XMLHttpRequest.getAllResponseHeaders() );
				}, 
				dataType: "json" //request JSON
			};
	return false;
}

function getUnBidList() {
	
	var d = new Date()
		, n = d.getTime();
	
	ajaxObj = {  
			type: "GET",
			url: "http://localhost:8080/E-Auction/api/Auctions/unBidList", 
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
					html_string = html_string + templateGetProductList(val1);
				});
				
				$('#get_productList').html("<table class='table table-hover'>" +
										"<tbody><tr><th>Product Name </th><th>Description</th><th>Starting Bid </th><th>Last Bidding Date</th><th></th></tr>"+ html_string + "</tbody></table>");
			},
			complete: function(XMLHttpRequest) {
				//console.log( XMLHttpRequest.getAllResponseHeaders() );
			}, 
			dataType: "json" //request JSON
		};
		
	return $.ajax(ajaxObj);
}

function templateGetProductList(param) {
	return  '<tr>' +
				'<td class="product_Name">' + param.product_Name + '</td>' +
				'<td class="product_Disc">' + param.product_Disc + '</td>' +
				'<td class="initial_Bid">&#8377;' + param.initial_Bid + '</td>' +
				'<td class="last_Bid_Time">' + param.last_Bid_Time + '</td>' +
				'<td><button class="bidItem btn btn-default" value="' + param.product_Id + '" type="button">Bid</button></td>'+
			'</tr>';
}