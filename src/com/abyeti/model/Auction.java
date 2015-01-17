package com.abyeti.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.abyeti.dao.DbConnection;
import com.abyeti.resourcee.ToJSON;

@Path("/Auctions")
public class Auction {

	@Context
	private HttpServletRequest request;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnAllPcParts() throws Exception {

		PreparedStatement query = null;
		Connection conn = null;
		String returnString = "sss";
		Response rb = null;
		HttpSession session = request.getSession();

		System.out.println("Session: "
				+ session.getAttribute("eauction_username"));

		try {
			conn = DbConnection.getConnection();
			query = conn
					.prepareStatement("select product_Id,product_Name,product_Disc,initial_Bid from product");

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

	/***** Get Product list to be bid by buyer ****/

	@Path("/unBidList")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnUnBidList() throws Exception {

		PreparedStatement query = null;
		Connection conn = null;
		String returnString = "sss";
		Response rb = null;
		HttpSession session = request.getSession();

		System.out.println("Session: "
				+ session.getAttribute("eauction_username"));

		try {
			conn = DbConnection.getConnection();
			query = conn
					.prepareStatement("select product_Id,product_Name,product_Disc,initial_Bid, last_Bid_Time from product order by last_Bid_Time desc");

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

	@Path("/searchMaxBid/{product_Id}/{bidAmount}")
	@GET
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnSearchMaxBId(
			@PathParam("product_Id") String product_Id,
			@PathParam("bidAmount") String bidAmount) throws Exception {

		PreparedStatement query = null;
		Connection conn = null;
		String returnString = "sss";
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		Response rb = null;
		HttpSession session = request.getSession();

		String buyer_Id = (String) session.getAttribute("eauction_username");
		System.out.println("Session: "
				+ session.getAttribute("eauction_username"));

		try {
			conn = DbConnection.getConnection();
			query = conn
					.prepareStatement("SELECT MAX(buyer_Price) as MaxBid FROM bid where product_Id=?");
			query.setString(1, product_Id);
			ResultSet rs = query.executeQuery();

			if (rs.next()) {
				System.out.println(" Row Count : " + rs.getDouble("MaxBid")
						+ "\n bidamount : " + Double.valueOf(bidAmount));
				if (Double.valueOf(bidAmount) > rs.getDouble("MaxBid")) {
					if (firstTimeBid(product_Id, buyer_Id, conn))
						rb = insertBidAmount(product_Id, bidAmount, buyer_Id,
								conn);
					else
						rb = updateBidAmount(product_Id, bidAmount, buyer_Id,
								conn);
				} else {
					jsonObject.put("HTTP_CODE", "200");
					jsonObject.put("MSG",
							" Your Bidding Price is less than Maximum Bid ");
					returnString = jsonArray.put(jsonObject).toString();
					rb = Response.ok(returnString).build();
				}
			} else {
				query = conn
						.prepareStatement("SELECT initial_Bid FROM product where product_Id=?");
				query.setString(1, product_Id);
				rs = query.executeQuery();
				if (rs.next())
					rb = insertBidAmount(product_Id, bidAmount, buyer_Id, conn);
			}
			query.close(); // close connection
			System.out.println("rb " + rb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.close();
		}
		return rb;
	}
	
	/**** Get last Auction time of a particular product ****/
	@Path("/lastAuctionTime/{product_Id}")
	@GET
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnLastAuctionTime(
			@PathParam("product_Id") String product_Id) throws Exception {

		PreparedStatement query = null;
		Connection conn = null;
		String returnString = "sss";
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		Response rb = null;
		HttpSession session = request.getSession();

		System.out.println("Session: "
				+ session.getAttribute("eauction_username"));

		try {
			conn = DbConnection.getConnection();
			query = conn
					.prepareStatement("SELECT last_Bid_Time FROM product WHERE product_Id=?");
			query.setString(1, product_Id);
			ResultSet rs = query.executeQuery();
			Date dateobj = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateobj);
			cal.set(Calendar.MILLISECOND, 0);
			System.out.println("rs : "+rs.getTimestamp("last_Bid_Time"));
			System.out.println("date : "+new java.sql.Timestamp(cal.getTimeInMillis()));
			if(rs.getTimestamp("last_Bid_Time").after(new java.sql.Timestamp(cal.getTimeInMillis())))
			{
				jsonObject.put("HTTP_CODE", "200");
				jsonObject.put("MSG", " Allow to Bid ");
				returnString = jsonArray.put(jsonObject).toString();
				rb = Response.ok(returnString).build();
			} 
			else {
				jsonObject.put("HTTP_CODE", "202");
				jsonObject.put("MSG", "  Auction Time Over \n Not Allowed to Bid ");
				returnString = jsonArray.put(jsonObject).toString();
				rb = Response.ok(returnString).build();
			}
			query.close(); // close connection
			System.out.println("rb " + rb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.close();
			rb = Response.status(500).entity("Server was not able to process your request").build();
		}
		return rb;
	}

	/*** Check first Time to bid the Product ***/
	private boolean firstTimeBid(String product_Id, String buyer_Id,
			Connection conn) throws Exception {

		PreparedStatement query = null;
		try {
			query = conn
					.prepareStatement("SELECT product_Id FROM bid where product_Id=? and buyer_Id=?");
			query.setString(1, product_Id);
			query.setString(2, buyer_Id);
			ResultSet rs = query.executeQuery();

			if (rs.next()) {
				int rowCount = rs.getRow();
				if (rowCount != 0)
					return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/*** First time bidding insertion ***/
	public Response insertBidAmount(String product_Id, String bidAmount,
			String buyer_Id, Connection conn) {
		String returnString = null;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			/* Generate Bid Id */
			ps = conn
					.prepareStatement("SELECT bid_Id AS Last_Bid_Id FROM bid ORDER BY bid_Id DESC LIMIT 1");
			rs = ps.executeQuery();
			int bidId = 1;
			if (rs.next())
				bidId = Integer.parseInt(rs.getString("Last_Bid_Id")) + 1;
			System.out.println("podid : " + bidId);

			Date dateobj = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateobj);
			cal.set(Calendar.MILLISECOND, 0);
			System.out.println(new java.sql.Timestamp(cal.getTimeInMillis()));

			/* Now Insert the Product */
			ps = conn
					.prepareStatement("INSERT INTO bid(bid_Id,buyer_Id,buyer_Price,product_Id,bid_Time) VALUES(?,?,?,?,?) ");
			ps.setString(1, String.valueOf(bidId));
			ps.setString(2, buyer_Id);
			ps.setString(3, bidAmount);
			ps.setString(4, product_Id);
			ps.setTimestamp(5, new java.sql.Timestamp(cal.getTimeInMillis()));

			int http_code = ps.executeUpdate();

			if (http_code != 0) {
				jsonObject.put("HTTP_CODE", "200");
				jsonObject.put("MSG", " Bidding Successfully");
				returnString = jsonArray.put(jsonObject).toString();
			} else {
				System.out.println("Invalid");
				return Response.status(500).entity("Unable to Bid ").build();
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

	/*** Second Time Bidding updation for particular product **/
	public Response updateBidAmount(String product_Id, String bidAmount,
			String buyer_Id, Connection conn) {
		String returnString = null;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		PreparedStatement ps = null;

		try {
			/* Generate Bid Id */
			Date dateobj = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateobj);
			cal.set(Calendar.MILLISECOND, 0);

			/* Now Insert the Product */
			ps = conn
					.prepareStatement("UPDATE bid SET buyer_price=?, bid_Time =? WHERE product_Id=? and buyer_Id=? ");
			ps.setString(1, bidAmount);
			ps.setTimestamp(2, new java.sql.Timestamp(cal.getTimeInMillis()));
			ps.setString(3, product_Id);
			ps.setString(4, buyer_Id);

			int http_code = ps.executeUpdate();

			if (http_code != 0) {
				jsonObject.put("HTTP_CODE", "200");
				jsonObject.put("MSG", " Update your Bidding Successfully");
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

}