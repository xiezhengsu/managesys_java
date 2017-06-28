package cn.com.tojob.service;

import net.sf.json.JSONObject;

public interface HouseService {

	JSONObject addHouseInfo(JSONObject msg, String username);

	JSONObject deleteHouseByid(String id,String username);

	JSONObject getHouselist(JSONObject msg, String page, String prepage);

	JSONObject getHouseById(String id);

	JSONObject updateHouse(JSONObject house);

	boolean checkHouseStatus(JSONObject house);

}
