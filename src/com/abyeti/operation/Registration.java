package com.abyeti.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;

import com.abyeti.constant.DateTime;
import com.abyeti.dao.DbConnection;
import com.abyeti.model.Product;
import com.abyeti.model.User;
import com.abyeti.resourcee.Functions;
import com.abyeti.resourcee.GenerateId;
import com.abyeti.resourcee.Resources;

/**
 * @author Pushpendra
 *
 */
@Path("/Registration")
public class Registration {

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
	 * @param incomingData
	 * @return
	 * @throws Exception
	 */
	@Path("/newUser")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerNewUser(String incomingData) throws Exception {

		try {

			User newEntry = mapper.readValue(incomingData, User.class);
			conn = DbConnection.getConnection();
			ps = conn
					.prepareStatement("INSERT INTO login(username,password,firstname,lastname,emailid,sellerbuyer) VALUES(?,?,?,?,?,?) ");
			ps.setString(1, newEntry.getUsername());
			ps.setString(2, newEntry.getPassword());
			ps.setString(3, newEntry.getFirstname());
			ps.setString(4, newEntry.getLastname());
			ps.setString(5, newEntry.getEmailid());
			ps.setString(6, String.valueOf(newEntry.getSellerbuyer()));

			HTTPCODE = ps.executeUpdate();

			if (HTTPCODE != 0) {
				HTTPCODE = 200;
				MSG = "Registeration Successfully";
			} else {
				HTTPCODE = 200;
				MSG = "Registeration Failed ";
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

	@Path("/newAuction")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response addNewAuction(String incomingData) throws Exception {

		session = request.getSession();
		if (!Functions.isLoggedIn(request)) {
			return Response
					.status(500)
					.entity("Not yet Logged in. *Login Required to Register Auction")
					.build();
		}
		try {

			Product newProduct = mapper.readValue(incomingData, Product.class);
			conn = DbConnection.getConnection();

			/* Now Insert the Product */
			ps = conn
					.prepareStatement("INSERT INTO product(product_Id,product_Name,product_Disc,initial_Bid,last_Bid_Time,seller_Id) VALUES(?,?,?,?,?,?) ");
			ps.setString(1, String.valueOf(GenerateId.getGenerateProductId()));
			ps.setString(2, newProduct.getProduct_Name());
			ps.setString(3, newProduct.getProduct_Disc());
			ps.setString(4, String.valueOf(newProduct.getInitial_Bid()));
			ps.setString(5, newProduct.getLast_Bid_Time());
			ps.setString(6, (String) session.getAttribute("eauction_username"));

			HTTPCODE = ps.executeUpdate();

			if (HTTPCODE != 0) {
				HTTPCODE = 200;
				MSG = "Auction product has been entered successfully";
			} else {
				return Response.status(500)
						.entity("Unable to add Auction Product").build();
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

	public Response addNewBidding(String product_Id, String bidAmount,
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
			}
			else {
				HTTPCODE = 500;
				MSG = "Unable to Bid ";
			}
			returnString = Resources.createJSONMessage(HTTPCODE, MSG);
			
		} 
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(500)
					.entity("Server was not able to process your request")
					.build();
		}
		return Response.ok(returnString).build();
	}

}
