$(document).ready(function() {
	
	getMyBidding();
	/**** to remove an auction product ****/
	$(document.body).on('click', '.remove_Bidding', function(e) {
		var $this = $(this)
		, $tr = $this.closest('tr')
		, bid_Id = $tr.find('.bid_Id').text();
		console.log(" bid ID :"+bid_Id);
		if(confirm("Are you sure you want to delete ?")) {
			deleteBid(bid_Id);
		}
	});
	
	
});


/**
 * FUNCTION TO DELETE THE PRODUCT FROM AUCTION 
 * @param bid_Id
 * @returns
 */

function deleteBid(bid_Id) {
	ajaxObj = {  
			type: "DELETE",
			url: "http://localhost:8080/E-Auction/api/Delete/Bid/" + bid_Id,
			contentType:"application/json",
			error: function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.responseText);
			},
			success: function(data) {
				if(data[0].CODE==200)
					alert(data[0].MSG);
				$('#delete_response').text( data[0].MSG );
			},
			complete: function(XMLHttpRequest) {
				getMyBidding();
			}, 
			dataType: "json" //request JSON
		};
		
	return $.ajax(ajaxObj);
}


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
										"<tbody><tr><th>Bid No</th><th>Auction</th><th>Auction Discription</th><th>Seller Name</th><th>Stating Bid</th><th> Bidding Price</th><th>Status</th><th>Remove</th></tr>"+ html_string + "</tbody></table>");
			},
			complete: function(XMLHttpRequest) {
			}, 
			dataType: "json" //request JSON
		};
		
	return $.ajax(ajaxObj);
}

function templateGetMyBidding(param) {
	console.log(" get bid : "+ param.bid_Id);
	var tableData= '<tr>' +
				'<td class="bid_Id" >' + param.bid_Id + '</td>' +
				'<td class="product_Name">' + param.product_Name + '</td>' +
				'<td class="product_Disc">' + param.product_Disc + '</td>' +
				'<td class="sellername">' + param.sellername + '</td>' +
				'<td class="initial_Bid">&#8377;' + param.initial_Bid + '</td>' +
				'<td class="buyer_Price">&#8377;' + param.buyer_Price + '</td>';
				
				var bidstatus = BidStatus(param.product_Id,param.buyer_Price);
				if(bidstatus==0)
					tableData = tableData + '<td class="bid_Status"><span class="glyphicon glyphicon-thumbs-down" value='+param.product_Id+'></span></td>';
				else if(bidstatus==1)
					tableData = tableData + '<td class="bid_Status"><span class="glyphicon glyphicon-thumbs-left" value='+param.product_Id+'></span></td>';
				else
					tableData = tableData + '<td class="bid_Status"><span class="glyphicon glyphicon-thumbs-up" value='+param.product_Id+'></span></td>';
				tableData = tableData + '<td><a href="#" class="remove_Bidding" ><span class="glyphicon glyphicon-remove"></span></a></td>' + '</tr>';
	return tableData;
}

function BidStatus(product_Id,buyer_Price){
	var d = new Date()
	, n = d.getTime();
	var returnResult;
	ajaxObj = {  
			type: "GET",
			url: "http://localhost:8080/E-Auction/api/Bidding/maxBid/"+product_Id +"/"+buyer_Price, 
			data: "ts="+n,
			ascyn : false,
			contentType:"application/json",
			error: function(jqXHR, textStatus, errorThrown) {
				console.log(jqXHR.responseText);
			},
			success: function(data) {
				if(data[0].HTTP_CODE==200)
					returnResult = data[0].MSG;
			},
			complete: function(XMLHttpRequest) {
			}, 
			dataType: "json" //request JSON
		},
	$.ajax(ajaxObj);
	return returnResult;
	}