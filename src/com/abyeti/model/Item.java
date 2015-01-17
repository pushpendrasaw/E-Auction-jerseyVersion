package com.abyeti.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.abyeti.dao.DbConnection;
import com.abyeti.resourcee.Functions;
import com.abyeti.resourcee.ToJSON;

@Path("/item")
public class Item {

	@Context private HttpServletRequest request;
	
	@Path("/new")
	@POST
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response addItem(String incomingData) throws Exception {
		
		HttpSession session = request.getSession();
		if(!Functions.isLoggedIn(request)) {
			System.out.println("Not Logged In");
			return Response.status(500).entity("NOt yet Logged in. *Login Required").build();
		}
		
		String returnString = null;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			JSONObject itemData = new JSONObject(incomingData);
			
			conn = DbConnection.getConnection();
			/*	Generate Product Id */
			ps = conn.prepareStatement("SELECT product_Id AS Last_Product_Id FROM product ORDER BY product_Id DESC LIMIT 1");
			rs = ps.executeQuery();
			int podId = 1;
			if(rs.next())
			 podId = Integer.parseInt(rs.getString("Last_Product_Id")) + 1;
			System.out.println("podid : "+ podId);
			
			/* Now Insert the Product*/
			ps = conn.prepareStatement("INSERT INTO product(product_Id,product_Name,product_Disc,initial_Bid,last_Bid_Time,seller_Id) VALUES(?,?,?,?,?,?) ");
			ps.setString(1, String.valueOf(podId));
			ps.setString(2, itemData.optString("product_Name"));
			ps.setString(3, itemData.optString("product_Disc"));
			ps.setString(4, itemData.optString("initial_Bid"));
			ps.setString(5, itemData.optString("last_Bid_Time"));
			ps.setString(6, (String) session.getAttribute("eauction_username"));
			
			int http_code = ps.executeUpdate();
						
			if( http_code != 0 ) {
				jsonObject.put("HTTP_CODE", "200");
				jsonObject.put("MSG", "Item has been entered successfully");
				returnString = jsonArray.put(jsonObject).toString();
			} else {
				System.out.println("Invalid");
				return Response.status(500).entity("Unable to add Auction Product").build();
			}
			
			System.out.println( "returnString: " + returnString );
			
		} catch(Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request").build();
		}
		
		return Response.ok(returnString).build();
	}
	
	@Path("/all")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnItemsOfSeller() throws Exception {
		
		PreparedStatement query = null;
		Connection conn = null;
		String returnString = null;
		Response rb = null;
		HttpSession session = request.getSession();
		String seller = (String)session.getAttribute("eauction_username");
		System.out.println(" returnItemsOfSeller Session: "+ seller);
		
		try {
			conn = DbConnection.getConnection();
			query = conn.prepareStatement("select product_Id,product_Name,product_Disc,initial_Bid, last_Bid_Time from product");
						
			ResultSet rs = query.executeQuery();
			
			ToJSON converter = new ToJSON();
			JSONArray json = new JSONArray();
			
			json = converter.toJSONArray(rs);
			query.close(); //close connection
			
			returnString = json.toString();
			
			rb = Response.ok(returnString).build();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			 if (conn != null) conn.close();
		}
		
		return rb;
	}
	
	@Path("/{product_Id}/{product_disc}/{initial_Bid}/{last_Bid_Time}")
	@PUT
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response UpdateItems(@PathParam("product_Id") String product_Id, 
								@PathParam("product_Disc") String product_Disc,
								@PathParam("initial_Bid") String initial_Bid,
								@PathParam("last_Bid_Time") String last_Bid_Time,
								String incomingdata) throws Exception {
		
		PreparedStatement query = null;
		Connection conn = null;
		String returnString = null;
		Response rb = null;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();		
		try {
			conn = DbConnection.getConnection();
			query = conn.prepareStatement("UPDATE product SET product_Disc=?, initial_Bid=?, last_Bid_Time=? WHERE product_Id=?");
			query.setString(1, product_Disc);
			query.setString(2, initial_Bid);
			query.setString(3, last_Bid_Time);
			query.setString(4, product_Id);
			
			System.out.println(incomingdata+"\n"+query.toString());
			query.executeUpdate();
			
			jsonObject.put("CODE", "200");
			jsonObject.put("MSG", "Item has been updated successfully");
			query.close(); //close connection
			returnString = jsonArray.put(jsonObject).toString();
			
			rb = Response.ok(returnString).build();
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request").build();
		}
		finally {
			 if (conn != null) conn.close();
		}
		
		return rb;
	}

	@Path("/{product_Id}")
	@DELETE
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response DeleteItems(@PathParam("product_Id") String product_Id, 
								String incomingdata) throws Exception {
		
		PreparedStatement query = null;
		Connection conn = null;
		String returnString = null;
		Response rb = null;
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObject = new JSONObject();		
		try {
			conn = DbConnection.getConnection();
			query = conn.prepareStatement("DELETE FROM product WHERE product_Id=?");
			query.setString(1, product_Id);
			
			query.executeUpdate();
			
			jsonObject.put("CODE", "200");
			jsonObject.put("MSG", "Item has been deleted successfully");
			query.close(); //close connection
			returnString = jsonArray.put(jsonObject).toString();
			
			rb = Response.ok(returnString).build();
			
		}
		catch (Exception e) {
			e.printStackTrace();
			return Response.status(500).entity("Server was not able to process your request").build();
		}
		finally {
			 if (conn != null) conn.close();
		}
		
		return rb;
	}	

	@Path("/inventory")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInventory() throws Exception {
		
		PreparedStatement query = null;
		Connection conn = null;
		String returnString = null;
		Response rb = null;
		HttpSession session = request.getSession();
		
		System.out.println("Session: "+ session.getAttribute("eauction_username"));
		
		try {
			conn = DbConnection.getConnection();
			query = conn.prepareStatement("select product_Id,product_Name,product_Disc,initial_Bid,last_Bid_Time, seller_Id from product");
			
			ResultSet rs = query.executeQuery();
			
			ToJSON converter = new ToJSON();
			JSONArray json = new JSONArray();
			
			json = converter.toJSONArray(rs);
			query.close(); //close connection
			
			returnString = json.toString();
			rb = Response.ok(returnString).build();
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			 if (conn != null) conn.close();
		}
		
		return rb;
	}
	
	
}
