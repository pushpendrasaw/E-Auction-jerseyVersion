package com.abyeti.resourcee;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
/**
 * 
 * @author Pushpendra
 *
 */
public class Functions {

	/**
	 * 
	 * @param req
	 * @return
	 */
	public static boolean isLoggedIn(HttpServletRequest req) {
		HttpSession session = req.getSession();
		System.out.println("Session in function" +session.getAttribute("eauction_username"));
		if (session.getAttribute("eauction_username") == null)
			return false;
		return true;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */

	public static String getLoggedInUsername(HttpServletRequest req) {
		HttpSession session = req.getSession();
		return session.getAttribute("eauction_username").toString();
	}
	
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public static boolean isSeller(HttpServletRequest req) {
		HttpSession session = req.getSession();
		System.out.println("Session in function for seller" +session.getAttribute("eauction_sellerbuyer"));
		if (session.getAttribute("eauction_sellerbuyer").equals("SELLER"))
			return true;
		return false;
	}

	/**
	 * 
	 * @param req
	 * @return
	 */
	public static String getLoggerAs(HttpServletRequest req) {
		HttpSession session = req.getSession();
		return session.getAttribute("eauction_sellerbuyer").toString();
	}

}
