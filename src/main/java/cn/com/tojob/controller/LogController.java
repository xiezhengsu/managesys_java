package cn.com.tojob.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.com.tojob.service.LogService;


@Controller
@RequestMapping(value = "/log")
public class LogController extends BaseController{

	private static final Logger LOGGER = LoggerFactory.getLogger(LogController.class);

	@Resource(name="logService")
	private LogService logService;

	/**
	 * 查询所有日志信息
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @param perPage
	 * @param category
	 *            1-登录，2-搜索，3-浏览，4-收藏，5-标签 ,6-下载
	 * @return
	 */
	@RequestMapping(value="/getLogs", method = RequestMethod.GET)
	@ResponseBody
	public Object getLogs(HttpServletRequest request,HttpServletResponse response,
			HttpSession session,@RequestParam(value = "loginfo", required = false) String loginfo
			,@RequestParam(value = "page", required = true) String page
			,@RequestParam(value = "perPage", required = true) String perPage 
			,@RequestParam(value = "search", required=false)String search) {
		JSONObject log = JSONObject.fromObject(loginfo);
		LOGGER.info("查询日志信息:start");
		try {
			JSONObject jsarray = logService.getLoglist(log,Integer.parseInt(page),Integer.parseInt(perPage));
			LOGGER.info("查询日志反馈信息:end");
			return success(jsarray);
		} catch (Exception e) {
			System.out.println(e);
			LOGGER.error("查询日志信息出错:" + e.getMessage());
			return error("查询客户列表失败");
		}
	}

}
