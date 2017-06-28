package cn.com.tojob.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.tojob.service.LoginService;


@Controller
@RequestMapping("/login")
public class LoginController  extends BaseController{
	@Resource(name = "loginService")
	public LoginService loginService;
	
	
	/*
	 * {"username":"推荐","pwd":"推荐"}
	 */
	@RequestMapping(value="/tologin"/* , method = RequestMethod.POST*/)
	@ResponseBody
	public Object toLogin(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,@RequestParam(value = "userinfo", required = true) String userinfo){
		JSONObject user = JSONObject.fromObject(userinfo);
		JSONObject json = loginService.UserLogin(user);
		if(json.has("succ") && json.getBoolean("succ")){
//			System.out.println("userId------>"+json.getJSONObject("message").get("userid"));
			session.setAttribute("userid", json.getJSONObject("message").get("userid").toString());
			session.setAttribute("username", user.get("username"));
			session.setAttribute("level", user.get("level"));
//			String username = (String) session.getAttribute("username");
//			System.out.println("username-login----"+username);
			return success(json.getJSONObject("message"));
		}else if(json.has("succ") && !json.getBoolean("succ")){
			return error(json.getString("message"));
		}else{
			return error("登陆失败");
		}
	}
	
	@RequestMapping("/logout")
	@ResponseBody
	public Object Redirect(HttpServletRequest request, HttpServletResponse response,
			HttpSession session){
		session.invalidate();
		return success("退出成功");
		
	}
}
