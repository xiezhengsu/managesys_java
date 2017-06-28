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

import cn.com.tojob.service.HouseService;
import cn.com.tojob.service.LogService;

@Controller
@RequestMapping("/house")
public class HouseController extends BaseController{
	
	@Resource(name="houseService")
	public HouseService houseService;
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
	@RequestMapping(value="/add"/*,method = RequestMethod.POST*/)
	@ResponseBody
	public Object addHouseInfo(HttpServletRequest request,HttpServletResponse response,
			HttpSession session,@RequestParam(value="customer",required=true) String customer){
		String username = (String) session.getAttribute("username");
		
		JSONObject msg = JSONObject.fromObject(customer);
		JSONObject json = houseService.addHouseInfo(msg,username);
		response.setContentType("text/html;charset=utf-8");
		if(json.getBoolean("succ")){
			//rtDto.setMessage(json.toString());
			//return rtDto;
			logService.WriteLog(username, "房源","添加房源", "添加房源信息，房源负责人："+msg.getString("manageuser"), "");
			return success(json.getString("message"));
		}else{
//				rtDto.setMessage(json.toString());
//				return rtDto;
			return error(json.getString("message"));
		}
	}
	
	@RequestMapping(value="/delete")
	@ResponseBody
	public Object deleteByHouseId(HttpServletRequest request,HttpServletResponse response,
			HttpSession session,@RequestParam(value="houseid") String id,@RequestParam(value="manageuser") String manageuser){
				
		try {
			String username = (String) session.getAttribute("username");
			JSONObject json = houseService.deleteHouseByid(id,username);
			if(json.getBoolean("succ")){
				logService.WriteLog(username, "房源","删除房源", "删除房源信息："+manageuser+"，房源ID："+id,"");
				return success(json.getString("message"));
			}else{
				return error(json.getString("message"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			//logger.error(ExceptionUtils.getFullStackTrace(e));
			return error("删除信息失败");
		}
		
	}
	@RequestMapping(value="/list" , method = RequestMethod.GET)
	@ResponseBody
	public Object getHouselist(HttpServletRequest request,HttpServletResponse response,
			HttpSession session,@RequestParam(value = "houseinfo", required = false) String houseinfo
			,@RequestParam(value = "page", required = true) String page
			,@RequestParam(value = "prepage", required = true) String prepage){
		JSONObject msg = JSONObject.fromObject(houseinfo);
		try {
			JSONObject jsarray = houseService.getHouselist(msg,page,prepage);
			return success(jsarray);
		} catch (Exception e) {
			e.printStackTrace();
			//logger.error(ExceptionUtils.getFullStackTrace(e));
			return error("查询房源列表失败");
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
	public Object getHouseById(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,@RequestParam(value = "id", required = true) String id){		
		try {
			String username = (String) session.getAttribute("username");
			JSONObject json = houseService.getHouseById(id);
			if(json.getBoolean("succ")){
				logService.WriteLog(username, "房源","查看房源", "用户 "+username+" 查看了房源信息，该房源负责人为："+
						json.getJSONObject("message").getString("manageuser")+"，该房源ID为："+
						json.getJSONObject("message").getString("id"),"");
				return success(json.getJSONObject("message"));
			}else{
				return error("获取房源信息失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
//			logger.error(ExceptionUtils.getFullStackTrace(e));
			return error("获取房源信息失败");
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
	public Object updateHouse(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,@RequestParam(value = "houseStr", required = true) String houseStr){
		try {
			JSONObject house = JSONObject.fromObject(houseStr);
			String username = (String) session.getAttribute("username");
			house.put("optusername", username);
			JSONObject json = houseService.updateHouse(house);
			if(house.has("currentstatus") && !"".equals(house.getString("currentstatus"))){
				boolean track = houseService.checkHouseStatus(house);
				if(!track){//有变化
					System.out.println("--写入日志--");
					logService.WriteLog(username, "房源", "更新房源","目前状况："+house.getString("currentstatus")+"，负责人姓名："+house.getString("manageuser"), "");
				}
			}
			if(json.getBoolean("succ")){
				logService.WriteLog(username, "房源", "更新房源","目前状况："+house.getString("currentstatus")+"，负责人姓名："+house.getString("manageuser"), "");
				return success(json.getString("message"));
			}else{
				return error(json.getString("message"));
			}
		}catch(Exception e){
			e.printStackTrace();
			//logger.error(ExceptionUtils.getFullStackTrace(e));
			return error("房源信息修改失败");
		}
	}
	

}
