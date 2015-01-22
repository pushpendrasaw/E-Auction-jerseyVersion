package com.abyeti.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

import com.abyeti.dao.DbConnection;
import com.abyeti.resourcee.Resources;

/**
 * @author Pushpendra
 *
 */
@Path("/Delete")
public class Deletion {

	@Context
	private HttpServletRequest request;
	private static String MSG;
	private static int HTTPCODE;
	private static String returnString;
	private static Connection conn = null;
	private static PreparedStatement ps = null;
	private Response rb = null;
	HttpSession session = null;
	ObjectMapper mapper = new ObjectMapper();

	@Path("/product/{product_Id}")
	@DELETE
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	
	public Response DeleteProduct(@PathParam("product_Id") String product_Id, String incomingdata) throws Exception {
		try {
			if (BidOperation.isBidOnProduct(product_Id))
				rb = DeleteBid(product_Id);
			conn = DbConnection.getConnection();
			ps = conn.prepareStatement("DELETE FROM product WHERE product_Id=?");
			ps.setString(1, product_Id);
			HTTPCODE = ps.executeUpdate();
			if(HTTPCODE!=0) {
				HTTPCODE = 200;
				MSG = " Auction Product has been deleted successfully";
			}
			else
			{
				HTTPCODE = 200;
				MSG = "Unable To Delete the Auction Product";
			}
			returnString = Resources.createJSONMessage(HTTPCODE, MSG);
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
	 * function to delete the bid from bid table
	 * 
	 * @param product_Id
	 * @return
	 * @throws SQLException
	 */
	public static Response deleteBid(String product_Id) throws SQLException {
		try {

			conn = DbConnection.getConnection();
			ps = conn.prepareStatement(" DELETE FROM bid WHERE product_Id=?");
			ps.setString(1, product_Id);
			HTTPCODE = ps.executeUpdate();
			ps.close();
			if (HTTPCODE != 0) {
				HTTPCODE = 200;
				MSG = "Remove your Bidding Successfully..";
			} else {
				HTTPCODE = 200;
				MSG = "\n No More Bid to Remove/Delete";
			}

			returnString = Resources.createJSONMessage(HTTPCODE, MSG);
		} catch (Exception e) {
			e.printStackTrace();
			return Response.status(500)
					.entity("Server was not able to process your request")
					.build();
		} finally {
			if (conn != null)
				ps.close();
		}
		return Response.ok(returnString).build();
	}


	/**
	 * Function to delete the Bid
	 * @param bid_Id
	 * @param incomingdata
	 * @return
	 * @throws Exception
	 */
	@Path("/Bid/{bid_Id}")
	@DELETE
	@Consumes({MediaType.APPLICATION_FORM_URLENCODED,MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public Response DeleteBid(@PathParam("bid_Id") String bid_Id) throws Exception {
		try {
			conn = DbConnection.getConnection();
			ps = conn.prepareStatement("DELETE FROM bid WHERE bid_Id=?");
			ps.setString(1, bid_Id);
			HTTPCODE = ps.executeUpdate();
			if(HTTPCODE!=0){
				HTTPCODE = 200;
				MSG = "Bidding deleted successfully";
			}
			else
			{
				HTTPCODE = 200;
				MSG = "Unable to  delete";
			
			}
			ps.close(); //close connection
			returnString = Resources.createJSONMessage(HTTPCODE, MSG);
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
}
