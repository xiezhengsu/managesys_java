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

import cn.com.tojob.service.NewsService;

@Controller
@RequestMapping("/news")
public class NewsController extends BaseController{
	@Resource(name="newsService")
	public NewsService newsService;
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public Object addNews(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value = "newsStr", required = true) String newsStr){
		JSONObject news = JSONObject.fromObject(newsStr);
		JSONObject json = newsService.addNewsInfo(news);
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
	public Object deleteBynewsId(HttpServletRequest request,HttpServletResponse response,
			HttpSession session,@RequestParam(value="newsid") String newsid){
				
		try {
			JSONObject json = newsService.deleteNewsById(newsid);
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
	public Object getNewslist(HttpServletRequest request,HttpServletResponse response,
			HttpSession session,@RequestParam(value = "title", required = false) String title
			,@RequestParam(value = "page", required = true) String page){
		try {
			JSONArray jsarray = newsService.getNewslist(title,page);
			JSONObject json = new JSONObject();
			json.put("perPage", page);
			json.put("newsList", jsarray);
			
			return success(json);
		} catch (Exception e) {
			//logger.error(ExceptionUtils.getFullStackTrace(e));
			System.out.println(e);
			return error("查询新聞列表失败");
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
	@RequestMapping(value="/detailinfo" , method = RequestMethod.POST)
	@ResponseBody
	public Object getInfoById(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,@RequestParam(value = "id", required = true) String id){		
		try {
			JSONObject json = newsService.getInfoById(id);
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
	public Object updateNews(HttpServletRequest request, HttpServletResponse response,
			HttpSession session,@RequestParam(value = "newsstr", required = true) String newsstr){
		try {
			JSONObject news = JSONObject.fromObject(newsstr);
			JSONObject json = newsService.updateUser(news);
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
