package com.abyeti.resourcee;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Resources {

	/**
	 * 
	 * to create the JSON array for giving messages with code.
	 * @param CODE , MSG
	 * @return JSON Array Object 
	 * @throws JSONException
	 */
	public static String createJSONMessage(int CODE, String MSG) throws JSONException {
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		jsonObject.put("CODE", CODE);
		jsonObject.put("MSG", MSG);
		return jsonArray.put(jsonObject).toString();
	}
}
