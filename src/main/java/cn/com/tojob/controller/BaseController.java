package cn.com.tojob.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class BaseController {
	/**
	 * 返回成功的处理答复
	 * 
	 * @param value
	 *            参数Value
	 * @return
	 */
	public Object success(Object value) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", 0);
		resultMap.put("data", value);
		return resultMap;
	}

	/**
	 * 返回错误的处理答复
	 * 
	 * @param value
	 *            参数Value
	 * @return
	 */
	public Object error(Object value) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("success", 1);
		resultMap.put("data", value);
		return resultMap;
	}

	public HttpSession getSession(HttpServletRequest request) {
		return request.getSession();
	}

	public int getUserIdFromSession(HttpServletRequest request) {
		return 1;
	}
}
