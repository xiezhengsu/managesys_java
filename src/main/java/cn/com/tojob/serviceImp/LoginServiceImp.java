package cn.com.tojob.serviceImp;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import cn.com.tojob.dao.LoginDao;
import cn.com.tojob.service.LoginService;

@Service(value = "loginService")
public class LoginServiceImp implements LoginService{

	@Resource(name = "loginDao")
	public LoginDao loginDao;
	
	
	@Override
	public JSONObject UserLogin(JSONObject user) {
		JSONObject json = new JSONObject();
		if (user != null && user.has("username") && user.has("password")) {
			JSONObject temp = loginDao.toLogin(user);
			System.out.println("userid-->>>> ---"+temp.get("userid"));
			if(temp.has("username")){
				json.put("succ", true);
				json.put("message", temp);
				return json;
			}else{
				json.put("succ", false);
				json.put("message","用户名密码错误");
				return json;
			}
		}else{
			json.put("succ", false);
			json.put("message","用户名密码不为空");
			return json;
		}
		
		//return null;
	}
}
