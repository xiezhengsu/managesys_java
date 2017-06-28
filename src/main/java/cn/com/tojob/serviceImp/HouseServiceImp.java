package cn.com.tojob.serviceImp;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import cn.com.tojob.dao.HouseDao;
import cn.com.tojob.service.HouseService;
import cn.com.tojob.util.Page;

@Service(value="houseService")
public class HouseServiceImp implements HouseService{
	
	@Resource(name = "houseDao")
	public HouseDao houseDao;

	public JSONObject addHouseInfo(JSONObject msg,String username) {
		JSONObject json = new JSONObject();
		boolean temp = houseDao.addHouseInfo(msg,username);
		if (temp) {
			//int userid = userDao.getUserIdByname(user.getString("username"));
			//userDao.addUserAccess(userid,user.getJSONArray("access"));
			json.put("succ", true);
			json.put("message", "客户信息添加成功");
		}else{
			json.put("succ", false);
			json.put("message", "客户信息添加失败");
			
		}
		return json;
	}

	@Override
	public JSONObject deleteHouseByid(String id,String username) {
		int temp = houseDao.deleteHouseByid(id,username);
		JSONObject json =new JSONObject();
		if(temp>0){
			json.put("succ", true);
			json.put("message", "删除房源信息成功");
		}else{
			json.put("succ", false);
			json.put("message", "删除房源信息失败");
		}
		return json;
	}

	@Override
	public JSONObject getHouselist(JSONObject msg, String page,
			String prepage) {
		JSONObject json = new JSONObject();
		int count = houseDao.getHouseCount(msg,Integer.parseInt(page),Integer.parseInt(prepage));
		Page page1 = new Page(count,10);
		json.put("page", page);
		json.put("totalcount", page1.getResultCount());
		json.put("totalpage", page1.getTotalPage());
		JSONArray jsarray = houseDao.getHouselist(msg,Integer.parseInt(page),Integer.parseInt(prepage));
		json.put("list", jsarray);
		return json;
	}

	@Override
	public JSONObject getHouseById(String id) {
		JSONObject json = new JSONObject();
		JSONObject info = houseDao.getHouseById(id);
		json.put("succ", true);
		json.put("message", info);
		return json;
	}

	@Override
	public JSONObject updateHouse(JSONObject house) {
		JSONObject json = new JSONObject();
		boolean temp = houseDao.updateHouse(house);
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

	@Override
	public boolean checkHouseStatus(JSONObject house) {
		return houseDao.checkHouseStatus(house);
	}

}
