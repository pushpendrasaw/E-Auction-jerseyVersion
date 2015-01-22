package com.abyeti.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;

import com.abyeti.constant.DateTime;
import com.abyeti.dao.DbConnection;
import com.abyeti.resourcee.Resources;

@Path("/Update")
public class Updatation {

	@Context
	private HttpServletRequest request;
	private String MSG;
	private int HTTPCODE;
	private String returnString;
	private Connection conn = null;
	private PreparedStatement ps = null;
	HttpSession session = null;
	ObjectMapper mapper = new ObjectMapper();

	/**
	 * 
	 * @param product_Id
	 * @param product_Disc
	 * @param initial_Bid
	 * @param last_Bid_Time
	 * @param incomingdata
	 * @return
	 * @throws Exception
	 */
	@Path("/product/{product_Id}/{product_Disc}/{initial_Bid}/{last_Bid_Time}")
	@PUT
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response UpdateItems(@PathParam("product_Id") String product_Id,
			@PathParam("product_Disc") String product_Disc,
			@PathParam("initial_Bid") String initial_Bid,
			@PathParam("last_Bid_Time") String last_Bid_Time,
			String incomingdata) throws Exception  {

		try {
			conn = DbConnection.getConnection();
			ps = conn
					.prepareStatement("UPDATE product SET product_Disc=?, initial_Bid=?, last_Bid_Time=? WHERE product_Id=?");
			ps.setString(1, product_Disc);
			ps.setString(2, initial_Bid);
			ps.setString(3, last_Bid_Time);
			ps.setString(4, product_Id);

			HTTPCODE = ps.executeUpdate();
			if(HTTPCODE!=0) {
				HTTPCODE = 200;
				MSG = "Auction Product has been updated successfully";
			}
			else {
				HTTPCODE = 200;
				MSG = "Unable to Update the Auction Product ";
			}
					
			ps.close(); // close connection

			returnString = Resources.createJSONMessage(HTTPCODE, MSG);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500)
					.entity("Server was not able to process your request")
					.build();
		} finally {
			if (conn != null)
				conn.close();
		}
		return Response.ok(returnString).build();
	}

	/**
	 * Update the Bidding Status of the User
	 * 
	 * @param product_Id
	 * @param bidAmount
	 * @param buyer_Id
	 * @return
	 * @throws JSONException
	 */
	public Response updateBidAmount(String product_Id, String bidAmount,
			String buyer_Id) throws JSONException {

		try {
			conn = DbConnection.getConnection();
			ps = conn
					.prepareStatement("UPDATE bid SET buyer_price=?, bid_Time =? WHERE product_Id=? and buyer_Id=? ");
			ps.setString(1, bidAmount);
			ps.setTimestamp(2, new java.sql.Timestamp(DateTime.getDateTime()));
			ps.setString(3, product_Id);
			ps.setString(4, buyer_Id);
			HTTPCODE = ps.executeUpdate();

			if (HTTPCODE != 0) {
				HTTPCODE = 200;
				MSG = "Update your Bidding Successfully";
			} else {
				return Response.status(500).entity("Unable to Bid").build();
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

}
