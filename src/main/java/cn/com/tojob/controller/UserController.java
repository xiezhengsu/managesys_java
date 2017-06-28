package cn.com.tojob.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.tojob.service.UserService;
import cn.com.tojob.util.ResultDto;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController{
	@Resource(name="userService")
	public UserService userService;
	
	@RequestMapping(value="/add",method=RequestMethod.GET)
	@ResponseBody
	//public ResultDto addUser(HttpServletRequest request, HttpServletResponse response,@RequestParam(value = "userStr", required = true) String userStr) throws IOException{
//		ResultDto rtDto = new ResultDto();
	public Object addUser(HttpServletRequest request, HttpServletResponse response,@RequestParam(value = "userStr", required = true) String userStr) throws IOException{
		JSONObject user = JSONObject.fromObject(userStr);
		JSONObject json = userService.addUserInfo(user);
		response.setContentType("text/html;charset=utf-8");
		if(json.getBoolean("succ")){
			//rtDto.setMessage(json.toString());
			//return rtDto;
			return success(json.getString("message"));
		}else{
//				rtDto.setMessage(json.toString());
//				return rtDto;
			return error(json.getString("message"));
		}
			
	}
	
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object deleteByUserId(HttpServletRequest request,HttpServletResponse response,
			HttpSession session,@RequestParam(value="userid") String userId/*,@RequestParam(value="username") String username*/){
				
		try {
			JSONObject json = userService.deleteUser(userId);
			if(json.getBoolean("succ")){
				return success(json.getString("message"));
			}else{
				return error(json.getString("message"));
			}
		} catch (Exception e) {
			//logger.error(ExceptionUtils.getFullStackTrace(e));
			return error("删除用户失败");
		}
		
	}
	@RequestMapping(value="/list" , method = RequestMethod.GET)
	@ResponseBody
	public Object getUserlist(HttpServletRequest request,HttpServletResponse response,
			HttpSession session,@RequestParam(value = "name", required = false) String name
			,@RequestParam(value = "page", required = true) String page){
		String username = (String) session.getAttribute("username");
		System.out.println("username-----"+username);
		try {
			JSONObject jsarray = userService.getUserList(name,page);
			return success(jsarray);
		} catch (Exception e) {
			e.printStackTrace();
			//logger.error(ExceptionUtils.getFullStackTrace(e));
			return error("查询用户列表失败");
		}
	}
	
	/**
	 * 获取用户数据
	 * @param request
	 * @param response
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/info" , method = RequestMethod.POST)
	@ResponseBody
	public Object getUserById(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,@RequestParam(value = "id", required = true) String id){		
		try {
			JSONObject json = userService.getUserById(id);
			if(json.getBoolean("succ")){
				return success(json.getJSONObject("message"));
			}else{
				return error(json.getString("message"));
			}
		} catch (Exception e) {
//			logger.error(ExceptionUtils.getFullStackTrace(e));
			return error("获取用户失败");
		}
	}
	
	/**
	 * 跟新用户用户 {"id":"25","username":"admin22","pwd":"2","phone":"3","email":"4","note":"5","access":[{"accessid":"1"},{"accessid":"2"}]}
	 * @param request
	 * @param response
	 * @param session
	 * @param userStr
	 * @return
	 */
	@RequestMapping(value="/update"/* , method = RequestMethod.POST*/)
	@ResponseBody
	public Object updateUser(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,@RequestParam(value = "userStr", required = true) String userStr){
		try {
			JSONObject user = JSONObject.fromObject(userStr);
			JSONObject json = userService.updateUser(user);
			if(json.getBoolean("succ")){
				return success(json.getString("message"));
			}else{
				return error(json.getString("message"));
			}
		}catch(Exception e){
			e.printStackTrace();
			//logger.error(ExceptionUtils.getFullStackTrace(e));
			return error("用户修改失败");
		}
	}
}
