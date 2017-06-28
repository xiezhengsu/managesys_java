package cn.com.tojob.serviceImp;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import cn.com.tojob.dao.CustomerDao;
import cn.com.tojob.service.CustomerService;
import cn.com.tojob.util.Page;

@Service(value="customerService")
public class CustomerServiceImp implements CustomerService{
	
	@Resource(name = "customerDao")
	public CustomerDao customerDao;

	@Override
	public JSONObject addCustomerInfo(JSONObject msg,String username) {
		JSONObject json = new JSONObject();
		boolean check = customerDao.checkCustomerName(msg.getString("name"));
		if(check){
			boolean temp = customerDao.addCustomerInfo(msg,username);
			if (temp) {
				json.put("succ", true);
				json.put("message", "客户信息添加成功");
			}else{
				json.put("succ", false);
				json.put("message", "客户信息添加失败");
				
			}
		}else{
			json.put("succ", false);
			json.put("message", "客户信息已存在");
			
		}
		return json;
	}

	@Override
	public JSONObject deleteCustomerByid(String id,String username) {
		int temp = customerDao.deleteCustomerByid(id,username);
		JSONObject json =new JSONObject();
		if(temp>0){
			json.put("succ", true);
			json.put("message", "删除客户信息成功");
		}else{
			json.put("succ", false);
			json.put("message", "删除客户信息失败");
		}
		return json;
	}

	@Override
	public JSONObject getCustomerlist(JSONObject msg, String page, String prepage) {
		JSONObject json = new JSONObject();
		int count = customerDao.getCustomerCount(msg,Integer.parseInt(page),Integer.parseInt(prepage));
		Page page1 = new Page(count,10);
		json.put("page", page);
		json.put("totalcount", page1.getResultCount());
		json.put("totalpage", page1.getTotalPage());
		JSONArray jsarray = customerDao.getCustomerlist(msg,Integer.parseInt(page),Integer.parseInt(prepage));
		json.put("list", jsarray);
		return json;
	}

	@Override
	public JSONObject getCustomerById(String id) {
		JSONObject json = new JSONObject();
		JSONObject info = customerDao.getCustomerById(id);
		json.put("succ", true);
		json.put("message", info);
		return json;
	}

	@Override
	public JSONObject updateCustomer(JSONObject cus,String username) {
		JSONObject json = new JSONObject();
		boolean temp = customerDao.updateCustomer(cus,username);
		if(temp){
			//userDao.updateUserAccess(user);
			json.put("succ", true);
			json.put("message", "信息修改成功");
		}else{
			json.put("succ", false);
			json.put("message", "信息修改失败");
		}
		return json;
	}

	@Override
	public boolean checkTrackingStatus(JSONObject cus) {
		// TODO Auto-generated method stub
		return customerDao.checkTrackingStatus(cus);
	}

}
