/**
 * 
 */
package com.abyeti.resourcee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.abyeti.dao.DbConnection;

/**
 * @author Pushpendra
 *
 */
public class GenerateId {
	
	private static Connection conn;
	private static PreparedStatement ps;
	private static ResultSet rs;
	private static int generatedId = 1;
	
	/**
	 * Function to generate the Product Id 
	 * @return
	 * @throws Exception
	 */
	public static int getGenerateProductId() throws Exception
	{
		try{
			
			conn = DbConnection.getConnection();
			ps = conn.prepareStatement("SELECT product_Id AS Last_Product_Id FROM product ORDER BY product_Id DESC LIMIT 1");
			rs = ps.executeQuery();
			if(rs.next())
				generatedId = Integer.parseInt(rs.getString("Last_Product_Id")) + 1;
			ps.close();
			return generatedId;
		}	
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(conn!=null)
				ps.close();
		}
		return generatedId;
	}

	
	public static int getGenerateBidId() throws Exception
	{
		try{
			
			conn = DbConnection.getConnection();
			ps = conn.prepareStatement("SELECT bid_Id AS Last_Bid_Id FROM bid ORDER BY bid_Id DESC LIMIT 1");
			rs = ps.executeQuery();
			if(rs.next())
				generatedId = Integer.parseInt(rs.getString("Last_Bid_Id")) + 1;
			ps.close();
			return generatedId;
		}	
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(conn!=null)
				ps.close();
		}
		return generatedId;
	}
}
