package cn.com.tojob.service;

import net.sf.json.JSONObject;

public interface CustomerService {

	JSONObject addCustomerInfo(JSONObject msg, String username);

	JSONObject deleteCustomerByid(String id,String username);

	JSONObject getCustomerlist(JSONObject msg, String page, String prepage);

	JSONObject getCustomerById(String id);

	JSONObject updateCustomer(JSONObject cus, String username);

	boolean checkTrackingStatus(JSONObject cus);

}
