package cn.com.tojob.serviceImp;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.com.tojob.dao.LogDao;
import cn.com.tojob.service.LogService;
import cn.com.tojob.util.Page;

@Service(value="logService")
public class LogServiceImp implements LogService{
	
	@Resource(name="logDao")
	public LogDao logDao;

	@Override
	public void WriteLog(String optuser,String opttype,String optcategory,String optcontent,String optdec) {
		logDao.innsertLog(optuser,opttype,optcategory,optcontent,optdec);
	}

	public JSONObject getLoglist(JSONObject log, int page, int prepage) {
		JSONObject json = new JSONObject();
		int count = logDao.getLogCount(log,page,prepage);
		Page page1 = new Page(count,10);
		json.put("page", page);
		json.put("totalcount", page1.getResultCount());
		json.put("totalpage", page1.getTotalPage());
		JSONArray jsarray = logDao.getLoglist(log,page,prepage);
		json.put("list", jsarray);
		return json;
	}


}
