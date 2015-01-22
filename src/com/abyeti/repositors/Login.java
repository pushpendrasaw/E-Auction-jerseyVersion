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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;

import com.abyeti.dao.DbConnection;
import com.abyeti.model.User;
import com.abyeti.resourcee.Functions;
import com.abyeti.resourcee.Resources;

@Path("/Login")
public class Login {

	@Context
	private HttpServletRequest request;
	private String MSG;
	private int HTTPCODE;
	private String returnString;
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	HttpSession session = null;
	ObjectMapper mapper = new ObjectMapper();

	/**
	 * Function to check whether user logged in or not
	 * 
	 * @return
	 * @throws Exception
	 */
	@Path("/confirmLogin")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response isLoggedIn() throws Exception {

		if (!Functions.isLoggedIn(request)) {
			HTTPCODE = 500;
			MSG = "You are not logged in, <a href='login.html'>Login</a>";
		} else {
			HTTPCODE = 200;
			MSG = "Welcome " + Functions.getLoggedInUsername(request);
		}

		returnString = Resources.createJSONMessage(HTTPCODE, MSG);
		return Response.ok(returnString).build();
	}

	/**
	 * Function to logout the user from the session
	 * 
	 * @throws Exception
	 */
	@Path("/logout")
	@GET
	public void LogOut() throws Exception {
		HttpSession session = request.getSession();
		session.invalidate();
	}

	/**
	 * Function to login the user for the session
	 * 
	 * @param incomingData
	 *            - have data in JSON Format sent by the naive user
	 * 
	 * @return
	 * @throws Exception
	 * 
	 * 
	 */

	@Path("/login")
	@POST
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED,
			MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response LoginUser(String incomingData) throws Exception {

		session = request.getSession();
		try {

			User user = mapper.readValue(incomingData, User.class);
			conn = DbConnection.getConnection();
			ps = conn
					.prepareStatement("SELECT username,password,sellerbuyer FROM login WHERE username=? AND password=? ");
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			rs = ps.executeQuery();
			if (rs.next()) {
				HTTPCODE = 200;
				MSG = "Login Successfully";
				session.setAttribute("eauction_username", user.getUsername());
				session.setAttribute("eauction_sellerbuyer",
						rs.getString("sellerbuyer"));
			} else {
				HTTPCODE = 500;
				MSG = "<p class='text-danger'>Login Unsuccessfully </p>";
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