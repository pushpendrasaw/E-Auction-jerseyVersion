package com.abyeti.repositors;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;

import com.abyeti.constant.DateTime;
import com.abyeti.dao.DbConnection;
import com.abyeti.resourcee.GenerateId;
import com.abyeti.resourcee.Resources;
import com.abyeti.resourcee.ToJSON;

@Path("/Auctions")
public class Auction {

	@Context
	private HttpServletRequest request;
	private String MSG;
	private int HTTPCODE;
	private String returnString;
	private Connection conn = null;
	private PreparedStatement ps = null;
	HttpSession session = null;
	ObjectMapper mapper = new ObjectMapper();
	private Response rb;
	private ResultSet rs;

	/**
	 * Get Product list to be bid by buyer
	 * 
	 * @return
	 * @throws Exception
	 */

	@Path("/unBidList")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnUnBidList() throws Exception {

		session = request.getSession();

		try {
			conn = DbConnection.getConnection();
			ps = conn
					.prepareStatement("select product_Id,product_Name,product_Disc,initial_Bid, last_Bid_Time from product order by last_Bid_Time desc");
			rs = ps.executeQuery();

			ToJSON converter = new ToJSON();
			JSONArray json = new JSONArray();
			json = converter.toJSONArray(rs);
			ps.close(); // close connection

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

		session = request.getSession();
		String buyer_Id = (String) session.getAttribute("eauction_username");

		try {
			conn = DbConnection.getConnection();
			ps = conn
					.prepareStatement("SELECT MAX(buyer_Price) as MaxBid FROM bid where product_Id=?");
			ps.setString(1, product_Id);
			rs = ps.executeQuery();

			if (rs.next()) {
				if (Double.valueOf(bidAmount) > rs.getDouble("MaxBid")) {
					if (firstTimeBid(product_Id, buyer_Id, conn))
						rb = insertBidAmount(product_Id, bidAmount, buyer_Id,
								conn);
					else
						rb = updateBidAmount(product_Id, bidAmount, buyer_Id,
								conn);
				} // compare amount end
				else {
					HTTPCODE = 200;
					MSG = " Your Bidding Price is less than Maximum Bid ";
					returnString = Resources.createJSONMessage(HTTPCODE, MSG);
					rb = Response.ok(returnString).build();
				} // compare else end
			} // if next end
			else {
				ps = conn
						.prepareStatement("SELECT initial_Bid FROM product where product_Id=?");
				ps.setString(1, product_Id);
				rs = ps.executeQuery();
				if (rs.next())
					if (Double.valueOf(bidAmount) > rs.getDouble("initial_Bid"))
						rb = insertBidAmount(product_Id, bidAmount, buyer_Id,
								conn);
					else {
						HTTPCODE = 200;
						MSG = " Your Bidding Price is less than Maximum Bid ";
						returnString = Resources.createJSONMessage(HTTPCODE,
								MSG);
						rb = Response.ok(returnString).build();
					}
			}
			ps.close(); // close connection
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				conn.close();
		}
		return rb;
	}

	/**
	 * Function to Get last Auction time of a particular product
	 * 
	 * @param product_Id
	 * @return
	 * @throws Exception
	 */
	@Path("/lastAuctionTime/{product_Id}")
	@GET
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnLastAuctionTime(
			@PathParam("product_Id") String product_Id) throws Exception {

		session = request.getSession();
		try {
			conn = DbConnection.getConnection();
			ps = conn
					.prepareStatement("SELECT last_Bid_Time FROM product WHERE product_Id=?");
			ps.setString(1, product_Id);

			rs = ps.executeQuery();

			if (rs.next()) {
				if (rs.getTimestamp("last_Bid_Time").after(
						new java.sql.Timestamp(DateTime.getDateTime()))) {
					HTTPCODE = 200;
					MSG = " Allow to Bid ";
					returnString = Resources.createJSONMessage(HTTPCODE, MSG);
					rb = Response.ok(returnString).build();
				} else {
					HTTPCODE = 202;
					MSG = " Auction Time Over \n Not Allowed to Bid ";
				}
			} else {
				HTTPCODE = 200;
				MSG = "No bidding till date";
			}
			ps.close(); // close connection
		} catch (Exception e) {
			e.printStackTrace();
			rb = Response.status(500)
					.entity("Server was not able to process your request")
					.build();
		} finally {
			if (conn != null)
				conn.close();
		}
		returnString = Resources.createJSONMessage(HTTPCODE, MSG);
		rb = Response.ok(returnString).build();
		return rb;
	}

	/**
	 * Function to Check first Time to bid the Product
	 * 
	 * @param product_Id
	 * @param buyer_Id
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	private boolean firstTimeBid(String product_Id, String buyer_Id,
			Connection conn) throws Exception {

		try {
			ps = conn
					.prepareStatement("SELECT product_Id FROM bid where product_Id=? and buyer_Id=?");
			ps.setString(1, product_Id);
			ps.setString(2, buyer_Id);
			rs = ps.executeQuery();

			if (rs.next())
				if (rs.getRow() != 0)
					return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 
	 * Second Time Bidding update for particular product
	 * 
	 * @throws JSONException
	 * 
	 * 
	 */
	public Response updateBidAmount(String product_Id, String bidAmount,
			String buyer_Id, Connection conn) throws JSONException {

		try {
			ps = conn
					.prepareStatement("UPDATE bid SET buyer_price=?, bid_Time =? WHERE product_Id=? and buyer_Id=? ");
			ps.setString(1, bidAmount);
			ps.setTimestamp(2, new java.sql.Timestamp(DateTime.getDateTime()));
			ps.setString(3, product_Id);
			ps.setString(4, buyer_Id);
			HTTPCODE = ps.executeUpdate();
			if (HTTPCODE != 0) {
				HTTPCODE = 200;
				MSG = " Update your Bidding Successfully";
			} else {
				HTTPCODE = 500;
				MSG = " Unable to Bid";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500)
					.entity("Server was not able to process your request")
					.build();
		}
		returnString = Resources.createJSONMessage(HTTPCODE, MSG);
		return Response.ok(returnString).build();
	}

	/**
	 * 
	 * @param product_Id
	 * @param bidAmount
	 * @param buyer_Id
	 * @param conn
	 * @return
	 * @throws JSONException
	 */

	public Response insertBidAmount(String product_Id, String bidAmount,
			String buyer_Id, Connection conn) {

		try {

			/* Now Insert the Product */
			ps = conn
					.prepareStatement("INSERT INTO bid(bid_Id,buyer_Id,buyer_Price,product_Id,bid_Time) VALUES(?,?,?,?,?) ");
			ps.setString(1, String.valueOf(GenerateId.getGenerateBidId()));
			ps.setString(2, buyer_Id);
			ps.setString(3, bidAmount);
			ps.setString(4, product_Id);
			ps.setTimestamp(5, new java.sql.Timestamp(DateTime.getDateTime()));
			HTTPCODE = ps.executeUpdate();

			if (HTTPCODE != 0) {
				HTTPCODE = 200;
				MSG = "Bidding Successfully";
			} else {
				HTTPCODE = 500;
				MSG = "Unable to Bid ";
			}
			returnString = Resources.createJSONMessage(HTTPCODE, MSG);

		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500)
					.entity("Server was not able to process your request")
					.build();
		}
		return Response.ok(returnString).build();
	}

}