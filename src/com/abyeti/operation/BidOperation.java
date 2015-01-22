package com.abyeti.operation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.abyeti.dao.DbConnection;

public class BidOperation {


	private static Connection conn;
	private static PreparedStatement ps;
	private static ResultSet rs;

	/**
	 * Function to check whether Bidding is present on the specific product or not
	 * @param product_Id
	 * @return
	 * @throws SQLException
	 */
	public static boolean isBidOnProduct(String product_Id) throws SQLException {

		try {
			conn = DbConnection.getConnection();
			ps = conn.prepareStatement("SELECT COUNT(*) AS BidCount FROM bid WHERE product_Id=?");
			ps.setString(1, product_Id);
			rs = ps.executeQuery();
			if (rs.next()) {
				if (rs.getInt("BidCount") > 0) {
					ps.close(); // close the connection
					return true;
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				ps.close();
		}

		return false;
	}

}
