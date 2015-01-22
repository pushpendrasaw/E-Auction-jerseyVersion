/**
 * 
 */
package com.abyeti.operation;

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
import org.codehaus.jettison.json.JSONObject;

import com.abyeti.dao.DbConnection;
import com.abyeti.resourcee.ToJSON;

/**
 * @author Pushpendra
 *
 */

@Path("/product")
public class ProductOperation {

	@Context
	private HttpServletRequest request;
	private String returnString;
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	private Response rb;
	HttpSession session = null;
	ObjectMapper mapper = new ObjectMapper();
	private int HTTPCODE;
	private String MaxBid;
	private String buyers;
	/**
	 * Function to get the Auction List ORDER By Last Auction Time in Decreasing
	 * order
	 * 
	 * @return
	 * @throws Exception
	 */
	@Path("/products")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProductOrderBYTime() throws Exception {

		try {
			conn = DbConnection.getConnection();
			ps = conn
					.prepareStatement("select product_Id,product_Name,product_Disc,initial_Bid,last_Bid_Time, seller_Id from product ORDER BY last_Bid_Time DESC");

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

	/***
	 * 
	 * @return
	 * @throws Exception
	 */
	@Path("/myAuctions")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnMyAUction() throws Exception {

		session = request.getSession();
		String seller = (String) session.getAttribute("eauction_username");
		try {
			conn = DbConnection.getConnection();
			ps = conn.prepareStatement("select p.product_Id,p.product_Name,p.product_Disc,p.initial_Bid,p.last_Bid_Time, b.maxBid, b.buyer_Id from product p left Join( select buyer_Id, product_Id,max(buyer_Price) as maxBid from bid group by product_Id) b on b.product_Id = p.product_Id WHERE p.seller_Id=?");
			ps.setString(1, seller);
			rs = ps.executeQuery();

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
		} finally {
			if (conn != null)
				conn.close();
		}
		return rb;
	}

	/**
	 * 
	 * @param product_Id
	 * @return
	 * @throws Exception
	 */

	@Path("/maxBid/{product_Id}")
	@GET
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnMaxBidAndBuyer_Id(
			@PathParam("product_Id") String product_Id) throws Exception {
		session = request.getSession();
		if (session.getAttribute("eauction_username") == null)
			return Response.status(500)
					.entity("Authentic Buyer Required To Bid Auction Product")
					.build();
		System.out.println("pid :"+product_Id);
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();

		try {
			conn = DbConnection.getConnection();
			ps = conn
					.prepareStatement("SELECT MAX(buyer_Price) as maxBidd, buyer_Id FROM bid WHERE product_Id=? ");
			ps.setString(1, product_Id);
			rs = ps.executeQuery();
			if(rs.next()){
				HTTPCODE = 200;
				MaxBid = rs.getString("maxBidd");
				buyers = rs.getString("buyer_Id");
			}
			
			jsonObject.put("CODE",HTTPCODE);
			jsonObject.put("maxBid", MaxBid);
			jsonObject.put("buyers", buyers);
			
			returnString = jsonArray.put(jsonObject).toString();
			ps.close(); // close connection
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
