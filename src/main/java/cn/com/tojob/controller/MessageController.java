package cn.com.tojob.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.tojob.service.MessageService;

@Controller
@RequestMapping("/jobmsg")
public class MessageController extends BaseController{

	@Resource(name="messageService")
	public MessageService messageService;
	
	/**
	 * 添加招聘信息
	 * @param request
	 * @param response
	 * @param session
	 * @param jobmsg  json
	 * @return
	 */
	@RequestMapping(value="/add",method = RequestMethod.POST)
	@ResponseBody
	public Object addJobmsg(HttpServletRequest request,HttpServletResponse response,
			HttpSession session,@RequestParam(value="jobmsg",required=true) String jobmsg){
		JSONObject msg = JSONObject.fromObject(jobmsg);
		JSONObject json = messageService.addJobInfo(msg);
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
			HttpSession session,@RequestParam(value="id") String id){
				
		try {
			JSONObject json = messageService.deleteMsgByid(id);
			if(json.getBoolean("succ")){
				return success(json.getString("message"));
			}else{
				return error(json.getString("message"));
			}
		} catch (Exception e) {
			//logger.error(ExceptionUtils.getFullStackTrace(e));
			return error("删除信息失败");
		}
		
	}
	@RequestMapping(value="/list" , method = RequestMethod.GET)
	@ResponseBody
	public Object getUserlist(HttpServletRequest request,HttpServletResponse response,
			HttpSession session,@RequestParam(value = "title", required = false) String title
			,@RequestParam(value = "page", required = true) String page){
		try {
			JSONArray jsarray = messageService.getJobsList(title,page);
			JSONObject json = new JSONObject();
			json.put("perPage", page);
			json.put("jobslist", jsarray);
			
			return success(json);
		} catch (Exception e) {
			System.out.println(e);
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
	public Object getJobById(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,@RequestParam(value = "id", required = true) String id){		
		try {
			JSONObject json = messageService.getJobInfoById(id);
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
	@RequestMapping(value="/update" , method = RequestMethod.POST)
	@ResponseBody
	public Object updateUser(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,@RequestParam(value = "jobStr", required = true) String jobStr){
		try {
			JSONObject job = JSONObject.fromObject(jobStr);
			JSONObject json = messageService.updateJobInfo(job);
			if(json.getBoolean("succ")){
				return success(json.getString("message"));
			}else{
				return error(json.getString("message"));
			}
		}catch(Exception e){
			//logger.error(ExceptionUtils.getFullStackTrace(e));
			return error("用户修改失败");
		}
	}
}
