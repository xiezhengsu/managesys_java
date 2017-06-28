package cn.com.tojob.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public interface MessageService {

	JSONObject addJobInfo(JSONObject msg);

	JSONObject deleteMsgByid(String id);

	JSONArray getJobsList(String title, String page);

	JSONObject getJobInfoById(String id);

	JSONObject updateJobInfo(JSONObject user);

}
