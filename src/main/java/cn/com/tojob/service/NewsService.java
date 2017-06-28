package cn.com.tojob.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface NewsService {

	JSONObject addNewsInfo(JSONObject news);

	JSONObject deleteNewsById(String newsid);

	JSONArray getNewslist(String name, String page);

	JSONObject getInfoById(String id);

	JSONObject updateUser(JSONObject news);

}
