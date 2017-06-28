package cn.com.tojob.service;

import net.sf.json.JSONObject;

public interface UserService {

	JSONObject addUserInfo(JSONObject user);

	JSONObject deleteUser(String userId);

	JSONObject getUserList(String name, String page);

	JSONObject getUserById(String id);

	JSONObject updateUser(JSONObject user);

}
