package com.abyeti.resourcee;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@Path("/nav")
public class Navigation {

	@Context
	private HttpServletRequest request;

	public JSONObject createJSONObject(String link, String classname,
			String value) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("link", link);
		jsonObject.put("class", classname);
		jsonObject.put("value", value);
		return jsonObject;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnNavigation() throws Exception {
		System.out.println("returnNavigation");
		JSONArray jsonArray = new JSONArray();
		String returnString = "";
		jsonArray.put(createJSONObject("index.html", "", "Home"));
		if (Functions.isLoggedIn(request)) {
			if(Functions.isSeller(request)){
				jsonArray.put(createJSONObject("newAuction.html", "", "New Auction"));
				jsonArray.put(createJSONObject("myAuction.html", "", "My Auction"));
				}
			else{
				jsonArray.put(createJSONObject("toBid.html", "", "Bid"));
				jsonArray.put(createJSONObject("myBidding.html", "", "My Bidding"));
			}
			jsonArray.put(createJSONObject("", "logout", "Logout"));
		} else {
			jsonArray.put(createJSONObject("login.html", "", "Login"));
			jsonArray.put(createJSONObject("newuser.html", "", "Register"));
		}
		returnString = jsonArray.toString();
		return Response.ok(returnString).build();
	}
}
