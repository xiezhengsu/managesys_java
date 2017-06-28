package cn.com.tojob.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;


public class CheckLoginInterceptor implements MethodInterceptor {


	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object[] args = invocation.getArguments();
		HttpServletRequest httpRequest = null;
		HttpServletResponse httpResponse = null;
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof HttpServletRequest)
				httpRequest = (HttpServletRequest) args[i];
			if (args[i] instanceof HttpServletResponse)
				httpResponse = (HttpServletResponse) args[i];
		}
		// System.out.println(httpRequest.getRequestURI());

		httpResponse
				.setContentType("text/html;charset=UTF-8;pageEncoding=UTF-8");
		httpResponse.setCharacterEncoding("UTF-8");
		HttpSession session = httpRequest.getSession(true);
		String module = invocation.getMethod().getDeclaringClass()
				.getSimpleName();
		String action = invocation.getMethod().getName();
		Pattern pattern = Pattern.compile("LoginController");
		Matcher matcher = pattern.matcher(module);
		
		String url = httpRequest.getRequestURI().toString(); 
//		System.out.println("url---"+url); //   '/customer/list'
		
		boolean check = false;
		if (check || matcher.find()) {
			return invocation.proceed();
		}

		String userId = "";
		try {
			if (session.getAttribute("userid") != null) {
				userId = (String) session.getAttribute("userid");
			} else {
				userId = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			userId = "";
		}
//		System.out.println("userid:"+userId);
		if (userId.equals("")) {
			httpResponse.sendError(401, "您已经太长时间没有操作,请刷新页面");
		}
		return invocation.proceed();

	}

	/**
	 * 判断是否为Ajax请求
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 是true, 否false
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		return request.getRequestURI().endsWith("ajax");
	}

	/**
	 * 判断是否为POST请求
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return 是true, 否false
	 */
	public static boolean isDoRequest(HttpServletRequest request) {
		return request.getRequestURI().endsWith("do");
	}

	public static boolean isHtmlRequest(HttpServletRequest request) {
		return request.getRequestURI().endsWith("html");
	}

}
