package com.abyeti.repositors;

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

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;

import com.abyeti.dao.DbConnection;
import com.abyeti.resourcee.Functions;
import com.abyeti.resourcee.GenerateId;
import com.abyeti.resourcee.Resources;
import com.abyeti.resourcee.ToJSON;

@Path("/Bidding")
public class Bidding {

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

	@Path("/new/{product_id}")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response addItem(@PathParam("product_Id") String product_Id,
			@PathParam("Buyer_Price") String buyer_Price) throws Exception {

		session = request.getSession();
		if (session.getAttribute("eauction_username") == null) {
			System.out.println("Not Logged In");
			return Response.status(500)
					.entity("Authentic Buyer Required To Bid Auction Product")
					.build();
		}

		try {

			conn = DbConnection.getConnection();
			ps = conn
					.prepareStatement("INSERT INTO bid (bid_Id,buyer_Id,buyer_Price,product_Id) VALUES(?,?,?,?) ");
			ps.setString(1, String.valueOf(GenerateId.getGenerateBidId()));
			ps.setString(2, (String) session.getAttribute("eauction_username"));
			ps.setString(3, buyer_Price);
			ps.setString(4, product_Id);

			HTTPCODE = ps.executeUpdate();

			if (HTTPCODE != 0) {
				HTTPCODE = 200;
				MSG = " Bidding Succussfully";
			} else {
				HTTPCODE = 200;
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

	/***
	 * 
	 * @return
	 * @throws Exception
	 */
	@Path("/bidded")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnAllBidded() throws Exception {

		String buyer = Functions.getLoggedInUsername(request);

		try {
			conn = DbConnection.getConnection();
			ps = conn
					.prepareStatement("select p.product_Id, p.product_Name,p.product_Disc,p.initial_Bid,p.seller_Id,b.bid_Id,b.buyer_Price, sellername from product p join ( select u.firstname sellername, u.username from login u where u.sellerbuyer=?) c on c.username = p.seller_Id, bid b where p.product_Id = b.product_Id and b.buyer_Id=?");
			ps.setString(1, "SELLER");
			ps.setString(2, buyer);

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

	/**
	 * 
	 * @param product_Id
	 * @param buyer_Price
	 * @return
	 * @throws Exception
	 */
	@Path("/maxBid/{product_Id}/{buyer_Price}")
	@GET
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnMaxBid(@PathParam("product_Id") String product_Id,
			@PathParam("buyer_Price") String buyer_Price) throws Exception {

		session = request.getSession();
		if (session.getAttribute("eauction_username") == null)
			return Response.status(500)
					.entity("Authentic Buyer Required To Bid Auction Product")
					.build();
		try {
			conn = DbConnection.getConnection();
			ps = conn
					.prepareStatement("SELECT MAX(buyer_Price) as maxBidd, buyer_Id FROM bid WHERE product_Id=? ");
			ps.setString(1, product_Id);
			rs = ps.executeQuery();
			if (rs.next()) {
				HTTPCODE = 200;
				if (rs.getDouble("maxBidd") > Double.valueOf(buyer_Price))
					MSG = "0";
				else if (rs.getDouble("maxBidd") < Double.valueOf(buyer_Price))
					MSG = "2";
				else
					MSG = "1";
			} else {
				HTTPCODE = 200;
				MSG = " No Data ";
			}

			ps.close(); // close connection

			returnString = Resources.createJSONMessage(HTTPCODE, MSG);
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
