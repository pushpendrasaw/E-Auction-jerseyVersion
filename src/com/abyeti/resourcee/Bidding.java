package com.abyeti.resourcee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.abyeti.dao.DbConnection;

@Path("/Bidding")
public class Bidding {

	@Context
	private HttpServletRequest request;

	@Path("/new/{product_id}")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response addItem(@PathParam("product_Id") String product_Id,
			@PathParam("Buyer_Price") String buyer_Price) throws Exception {

		HttpSession session = request.getSession();
		if (session.getAttribute("eauction_username") == null) {
			System.out.println("Not Logged In");
			return Response.status(500)
					.entity("Authentic Buyer Required To Bid Auction Product")
					.build();
		}

		String returnString = null;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		Connection conn = null;
		PreparedStatement ps = null;

		try {

			/*
			 * We can create a new instance and it will accept a JSON string By
			 * doing this, we can now access the data.
			 */
			// JSONObject itemData = new JSONObject(incomingData);
			// System.out.println( "jsonData: " + itemData.toString() );

			conn = DbConnection.getConnection();

			PreparedStatement query = conn
					.prepareStatement("SELECT * FROM bid");
			ResultSet rs = query.executeQuery();
			int bidId = rs.getRow() + 1;

			ps = conn
					.prepareStatement("INSERT INTO bid (bid_Id,buyer_Id,buyer_Price,product_Id) VALUES(?,?,?,?) ");
			ps.setString(1, String.valueOf(bidId));
			ps.setString(2, (String) session.getAttribute("eauction_username"));
			ps.setString(3, buyer_Price);
			ps.setString(4, product_Id);

			int http_code = ps.executeUpdate();

			if (http_code != 0) {
				/*
				 * The put method allows you to add data to a JSONObject. The
				 * first parameter is the KEY (no spaces) The second parameter
				 * is the Value
				 */
				jsonObject.put("HTTP_CODE", "200");
				jsonObject.put("MSG", "Thanks for Buying");
				/*
				 * When you are dealing with JSONArrays, the put method is used
				 * to add JSONObjects into JSONArray.
				 */
				returnString = jsonArray.put(jsonObject).toString();
			} else {
				System.out.println("Invalid");
				return Response.status(500).entity("Unable to Bid").build();
			}

			System.out.println("returnString: " + returnString);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500)
					.entity("Server was not able to process your request")
					.build();
		}

		return Response.ok(returnString).build();
	}

	@Path("/myAuctions")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnAllSales() throws Exception {

		PreparedStatement query = null;
		Connection conn = null;
		String returnString = "sss";
		Response rb = null;
		HttpSession session = request.getSession();
		String seller = (String) session.getAttribute("eauction_username");
		System.out.println("Session: " + seller);

		try {
			conn = DbConnection.getConnection();
			query = conn
					.prepareStatement("select p.product_Id,p.product_Name,p.product_Disc,p.initial_Bid,p.last_Bid_Time from product p WHERE p.seller_Id=?");
			query.setString(1, seller);

			ResultSet rs = query.executeQuery();

			ToJSON converter = new ToJSON();
			JSONArray json = new JSONArray();

			json = converter.toJSONArray(rs);
			query.close(); // close connection

			returnString = json.toString();
			rb = Response.ok(returnString).build();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.close();
		}

		return rb;
	}

	@Path("/bidded")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnAllBidded() throws Exception {

		PreparedStatement query = null;
		Connection conn = null;
		String returnString = null;
		Response rb = null;
		String buyer = Functions.getLoggedInUsername(request);

		try {
			conn = DbConnection.getConnection();
			query = conn
					.prepareStatement("select p.product_Id,p.product_Name,p.product_Disc,p.initial_Bid,p.seller_Id,b.buyer_Price, sellername from product p join ( select u.firstname sellername, u.username from login u where u.sellerbuyer=?) c on c.username = p.seller_Id, bid b where p.product_Id = b.product_Id and b.buyer_Id=?");
			query.setString(1, "SELLER");
			query.setString(2, buyer);

			ResultSet rs = query.executeQuery();

			ToJSON converter = new ToJSON();
			JSONArray json = new JSONArray();

			json = converter.toJSONArray(rs);
			query.close(); // close connection

			returnString = json.toString();
			rb = Response.ok(returnString).build();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.close();
		}

		return rb;
	}

	@Path("/maxBid/{product_Id}")
	@GET
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
		MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnMaxBid(@PathParam("product_Id") String product_Id)
			throws Exception {
		
		System.out.println("Max Bid : "+ product_Id);
//		HttpSession session = request.getSession();
//		if (session.getAttribute("eauction_username") == null) {
//			System.out.println("Not Logged In");
//			return Response.status(500)
//					.entity("Authentic Buyer Required To Bid Auction Product")
//					.build();
//		}
		String returnString = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Response rb = null;

		try {
			conn = DbConnection.getConnection();

			ps = conn.prepareStatement("SELECT MAX(buyer_Price) as maxBidd, buyer_Id FROM bid WHERE product_Id=? ");
			ps.setString(1, product_Id);

			rs = ps.executeQuery();
//			if(rs.next())
//			System.out.println(" max : "+ rs.getString("maxBidd"));
			ToJSON converter = new ToJSON();
			JSONArray json = new JSONArray();
			json = converter.toJSONArray(rs);

			ps.close(); // close connection

			returnString = json.toString();
			rb = Response.ok(returnString).build();
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500)
					.entity("Server was not able to process your request")
					.build();
		}

		return rb;
	}
}
