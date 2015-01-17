package com.abyeti.dao;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/DB")
public class DbConnection {
	public static String DRIVER_NAME = "com.mysql.jdbc.Driver";
	public static String URL = "jdbc:mysql://localhost:3306/dbeauctionapp";
	public static String USERNAME = "root";
	public static String PASSWORD = "";
	public static Connection conn = null;
	
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public static Connection getConnection() throws Exception {

		try {
			Class.forName(DRIVER_NAME);
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			if (conn != null)
				return conn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

}
