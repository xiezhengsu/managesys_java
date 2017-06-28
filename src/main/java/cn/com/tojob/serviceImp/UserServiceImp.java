package cn.com.tojob.serviceImp;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.com.tojob.dao.UserDao;
import cn.com.tojob.service.UserService;
import cn.com.tojob.util.Page;

@Service(value = "userService")
public class UserServiceImp implements UserService{

	@Resource(name = "userDao")
	public UserDao userDao;
	@Override
	public JSONObject addUserInfo(JSONObject user) {
		JSONObject json = new JSONObject();
		boolean check = userDao.checkUserName(user.getString("username"));
		if (check) {
			boolean temp = userDao.addUser(user);
			if (temp) {
				//int userid = userDao.getUserIdByname(user.getString("username"));
				//userDao.addUserAccess(userid,user.getJSONArray("access"));
				json.put("succ", true);
				json.put("message", "用户添加成功");
			}else{
				json.put("succ", false);
				json.put("message", "用户添加失败");
				
			}
			return json;
		}else{
			json.put("succ", false);
			json.put("message", "用户名存在");
			return json;
		}
		//return false
	}
	@Override
	public JSONObject deleteUser(String userId) {
		int temp = userDao.deleteUserByid(userId);
		JSONObject json =new JSONObject();
		if(temp>0){
			json.put("succ", true);
			json.put("message", "删除用户成功");
		}else{
			json.put("succ", false);
			json.put("message", "删除用户失败");
		}
		return json;
	}
	@Override
	public JSONObject getUserList(String name, String page) {
		JSONObject json = new JSONObject();
		int count = userDao.getUserCount(name);
		Page page1 = new Page(count,10);
		json.put("page", page);
		json.put("totalcount", page1.getResultCount());
		json.put("totalpage", page1.getTotalPage());
		JSONArray jsarray = userDao.getUserLists(name,Integer.parseInt(page));
		json.put("list", jsarray);
		return json;
	}
	@Override
	public JSONObject getUserById(String id) {
		JSONObject json =  new JSONObject();
		JSONObject user = userDao.getUserByid(id);
		if(user!=null){
			JSONArray access = new JSONArray();
			//access = userDao.getUserAccess(id);
			user.put("access", access);
			json.put("succ", true);
			json.put("message", user);
			return json;
			
		}else{
			json.put("succ", false);
			json.put("message", "获取用户失败");
		}
		return json;
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public JSONObject updateUser(JSONObject user) {
		JSONObject json = new JSONObject();
		boolean temp = userDao.updateUser(user);
		if(temp){
			//userDao.updateUserAccess(user);
			json.put("succ", true);
			json.put("message", "用户修改成功");
		}else{
			json.put("succ", false);
			json.put("message", "用户修改失败");
		}
		return json;
	}

}
