package cn.com.tojob.service;

import net.sf.json.JSONObject;


public interface LogService {

	void WriteLog(String optuser,String opttype,String optcategory,String optcontent,String optdec);
	JSONObject getLoglist(JSONObject log, int page, int prepage);
}
