package cn.com.tojob.serviceImp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.com.tojob.dao.NewsDao;
import cn.com.tojob.service.NewsService;

@Service(value = "newsService")
public class NewsServiceImp implements NewsService{

	@Resource(name = "newsDao")
	public NewsDao newsDao;
	
	@Override
	public JSONObject addNewsInfo(JSONObject news) {
		JSONObject json = new JSONObject();
		boolean temp = newsDao.addNewsInfo(news);
		if (temp) {
			//int userid = userDao.getUserIdByname(user.getString("username"));
			//userDao.addUserAccess(userid,user.getJSONArray("access"));
			json.put("succ", true);
			json.put("message", "招聘信息添加成功");
		}else{
			json.put("succ", false);
			json.put("message", "用户添加失败");
			
		}
		return json;
	}

	@Override
	public JSONObject deleteNewsById(String newsid) {
		int temp = newsDao.deleteNewsById(newsid);
		JSONObject json =new JSONObject();
		if(temp>0){
			json.put("succ", true);
			json.put("message", "删除信息成功");
		}else{
			json.put("succ", false);
			json.put("message", "删除信息失败");
		}
		return json;
	}

	@Override
	public JSONArray getNewslist(String title, String page) {
		return newsDao.getNewslist(title,page);
	}

	@Override
	public JSONObject getInfoById(String id) {
		return newsDao.getInfoById(id);
	}

	@Override
	public JSONObject updateUser(JSONObject news) {
		JSONObject json = new JSONObject();
		boolean temp = newsDao.updateUser(news);
		if(temp){
			//userDao.updateUserAccess(user);
			json.put("succ", true);
			json.put("message", "信息修改成功");
		}else{
			json.put("succ", false);
			json.put("message", "信息修改失败");
		}
		return json;
	}

}
