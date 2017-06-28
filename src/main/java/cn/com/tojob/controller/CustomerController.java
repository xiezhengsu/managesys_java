package cn.com.tojob.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.tojob.service.CustomerService;
import cn.com.tojob.service.LogService;

@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController{
	
	@Resource(name="customerService")
	public CustomerService customerService;
	@Resource(name="logService")
	public LogService logService;

	/**
	 * 添加客户信息
	 * @param request
	 * @param response
	 * @param session
	 * @param jobmsg  json
	 * @return
	 */
	@RequestMapping(value="/add")
	@ResponseBody
	public Object addCustomerInfo(HttpServletRequest request,HttpServletResponse response,
			HttpSession session,@RequestParam(value="customer",required=true) String customer){
		JSONObject msg = JSONObject.fromObject(customer);
		String username = (String) session.getAttribute("username");
		if(!username.equals("") && username != null){
			JSONObject json = customerService.addCustomerInfo(msg,username);
			response.setContentType("text/html;charset=utf-8");
			if(json.getBoolean("succ")){
				logService.WriteLog(username, "客户","添加客户", "添加客户信息："+msg.getString("name"), "");
				return success(json.getString("message"));
			}else{
				return error(json.getString("message"));
			}
		}else{
			return error("用户信息已失效，请重新登录");
		}
		
	}
	
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object deleteByCusId(HttpServletRequest request,HttpServletResponse response,
			HttpSession session,@RequestParam(value="customerid") String id,@RequestParam(value="name") String name){
		String username = (String) session.getAttribute("username");
		try {
			JSONObject json = customerService.deleteCustomerByid(id,username);
			if(json.getBoolean("succ")){
				logService.WriteLog(username,"客户","删除客户", "删除客户信息："+name+"，客户ID："+id, "");
				return success(json.getString("message"));
			}else{
				return error(json.getString("message"));
			}
		} catch (Exception e) {
			System.out.println(e);
			//logger.error(ExceptionUtils.getFullStackTrace(e));
			return error("删除客户信息失败");
		}
		
	}
	@RequestMapping(value="/list" , method = RequestMethod.GET)
	@ResponseBody
	public Object getCustomerlist(HttpServletRequest request,HttpServletResponse response,
			HttpSession session,@RequestParam(value = "cusinfo", required = false) String cusinfo
			,@RequestParam(value = "page", required = true) String page
			,@RequestParam(value = "prepage", required = true) String prepage){
		JSONObject msg = JSONObject.fromObject(cusinfo);
		String username = (String) session.getAttribute("username");
		try {
			JSONObject jsarray = customerService.getCustomerlist(msg,page,prepage);
			return success(jsarray);
		} catch (Exception e) {
			e.printStackTrace();;
			//logger.error(ExceptionUtils.getFullStackTrace(e));
			return error("查询客户列表失败");
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
	@RequestMapping(value="/info"/* , method = RequestMethod.POST*/)
	@ResponseBody
	public Object getCustomerById(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,@RequestParam(value = "id", required = true) String id){		
		try {
			String username = (String) session.getAttribute("username");
			JSONObject json = customerService.getCustomerById(id);
			if(json.getBoolean("succ")){
				logService.WriteLog(username, "客户","查看客户", "用户 "+username+" 查看了客户信息，该客户为："+
						json.getJSONObject("message").getString("name")+"，该客户ID为："+
						json.getJSONObject("message").getString("id"),"");
				return success(json.getJSONObject("message"));
			}else{
				return error("获取客户信息失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error(ExceptionUtils.getFullStackTrace(e));
			return error("获取客户信息失败");
		}
//		try {
//			JSONObject json = customerService.getCustomerById(id);
//			if(json.getBoolean("succ")){
//				return success(json.getJSONObject("message"));
//			}else{
//				return error(json.getString("message"));
//			}
//		} catch (Exception e) {
////			logger.error(ExceptionUtils.getFullStackTrace(e));
//			return error("获取客户信息失败");
//		}
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
	public Object updateCustomer(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,@RequestParam(value = "cusStr", required = true) String cusStr){
		try {
			JSONObject cus = JSONObject.fromObject(cusStr);
			String username = (String) session.getAttribute("username");
			cus.put("optusername", username);
			JSONObject json = customerService.updateCustomer(cus,username);
			System.out.println((cus.has("trackingstatus") && !"".equals(cus.getString("trackingstatus")))+"--bool----succ");
			//判断跟踪状况是否发生改变，若发生改变，需要写入日志
			if(cus.has("trackingstatus") && !"".equals(cus.getString("trackingstatus"))){
				boolean track = customerService.checkTrackingStatus(cus);
				if(!track){//有变化
					System.out.println("--写入日志--");
					logService.WriteLog(username, "客户", "更新客户","跟踪状况："+cus.getString("trackingstatus")+"，客户姓名："+cus.getString("name"), "");
				}
			}
			if(json.getBoolean("succ")){
				
//				logService.WriteLog(username, "客户", "更新客户信息："+cus.getString("name"), "");
				return success(json.getString("message"));
			}else{
				return error(json.getString("message"));
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
			return error("客户信息修改失败");
		}
	}
}
