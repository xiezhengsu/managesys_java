package cn.com.tojob.serviceImp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import cn.com.tojob.dao.MessageDao;
import cn.com.tojob.service.MessageService;
@Service(value="messageService")
public class MessageServiceImp implements MessageService{

	@Resource(name = "messageDao")
	public MessageDao messageDao; 
	
	@Override
	public JSONObject addJobInfo(JSONObject msg) {
		JSONObject json = new JSONObject();
		boolean temp = messageDao.addJobsInfo(msg);
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
	public JSONObject deleteMsgByid(String id) {
		int temp = messageDao.deleteMsgByid(id);
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
	public JSONArray getJobsList(String title, String page) {
		JSONArray jsarray =   messageDao.getJobsList(title,Integer.parseInt(page));
		return jsarray;
	}

	@Override
	public JSONObject getJobInfoById(String id) {
		JSONObject json =  new JSONObject();
		JSONObject user = messageDao.getJobInfoById(id);
		if(user!=null){
			JSONArray access = new JSONArray();
			//access = userDao.getUserAccess(id);
			user.put("access", access);
			json.put("succ", true);
			json.put("message", user);
			return json;
			
		}else{
			json.put("succ", false);
			json.put("message", "获取招聘信息失败");
		}
		return json;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public JSONObject updateJobInfo(JSONObject jobStr) {
		JSONObject json = new JSONObject();
		
		boolean temp = messageDao.updateJobInfo(jobStr);
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
