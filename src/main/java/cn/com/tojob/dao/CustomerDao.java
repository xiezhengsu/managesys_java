package cn.com.tojob.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import cn.com.tojob.util.CollectionUtils;


@Repository(value = "customerDao")
public class CustomerDao {
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	public boolean addCustomerInfo(final JSONObject msg, final String username) {
		String sql = "insert into customer (name, company, housearea, industry, demandarea, telephone, distribution, trackingstatus, quality, nature, optusername, isDriving, driviecontent,leasetype, createtime,updatetime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),NOW())";
		int i = jdbcTemplate.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, msg.getString("name"));
				ps.setString(2, msg.getString("company"));
				ps.setString(3, msg.getString("housearea"));
				ps.setString(4, msg.getString("industry"));
				ps.setString(5, msg.getString("demandarea"));
				ps.setString(6, msg.getString("telephone"));
				ps.setString(7, msg.getString("distribution"));
				ps.setString(8, msg.getString("trackstatus"));
				ps.setString(9, msg.getString("cusquality"));
				ps.setString(10, msg.getString("companynature"));
				ps.setString(11, username);
				ps.setString(12, msg.getString("isdriving"));
				ps.setString(13, msg.getString("drivingtext"));
				ps.setString(14, msg.getString("leasetype"));
			}
		});
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	public int deleteCustomerByid(String id,String username) {
		String sql = "update customer set isdelete=0,optusername='"+username+"' where id ="+Integer.parseInt(id);
		int i = jdbcTemplate.update(sql);
		return i;
	}

	public JSONArray getCustomerlist(JSONObject msg, int page, int prepage) {
		final JSONArray jsarray = new JSONArray();
		String sql = "SELECT * FROM customer WHERE isdelete = 1";
		//客户ID
		/*if(msg.has("id") && !msg.getString("id").equals("")){
			sql += " and id = "+Integer.parseInt(msg.getString("id"));
		}*/
		//房源面积
		if(msg.has("housearea1") && !msg.getString("housearea1").equals("")){
			sql += " and housearea >= "+Integer.parseInt(msg.getString("housearea1"));
		}
		if(msg.has("housearea2") && !msg.getString("housearea2").equals("")){
			sql += " and housearea <= "+Integer.parseInt(msg.getString("housearea2"));
		}
		//客户行业
		if(msg.has("industry") && !msg.getString("industry").equals("")
				&& !msg.getString("industry").equals("[]")){
			sql += " and industry = '"+msg.getString("industry")+"'";
		}
		//客户姓名
		if(msg.has("name") && !msg.getString("name").equals("")){
			sql += " and name like '%"+msg.getString("name")+ "%'";
		}
		//客户单位
		if(msg.has("company") && !msg.getString("company").equals("")){
			sql += " and company like '%"+msg.getString("company")+ "%'";
		}
		//客户手机号
		if(msg.has("telephone") && !msg.getString("telephone").equals("")){
			sql += " and telephone like '%"+msg.getString("telephone").trim()+ "%'";
		}
		//客户需求区域
		if(msg.has("demandarea") && !msg.getString("demandarea").equals("") && !msg.getString("demandarea").equals("全部区域")){
			sql += " and demandarea = '"+msg.getString("demandarea")+ "'";
		}
		//客户是否行车  
		if(msg.has("isdriving") && !msg.getString("isdriving").equals("") && !msg.getString("isdriving").equals("全部")){
			sql += " and isDriving = '"+msg.getString("isdriving")+"'";
			if(msg.getString("isdriving").equals("true")){
				//行车  吨位    
				if(msg.has("driviecontent1") && !msg.getString("driviecontent1").equals("")){
					sql += " and driviecontent >= "+Integer.parseInt(msg.getString("driviecontent1"));
				}
				if(msg.has("driviecontent2") && !msg.getString("driviecontent2").equals("")){
					sql += " and driviecontent <= "+Integer.parseInt(msg.getString("driviecontent2"));
				}
			}
		}
		//客户配电大小 kva    
		if(msg.has("distribution1") && !msg.getString("distribution1").equals("")){
			sql += " and distribution >= "+Integer.parseInt(msg.getString("distribution1"));
		}
		if(msg.has("distribution2") && !msg.getString("distribution2").equals("")){
			sql += " and distribution <= "+Integer.parseInt(msg.getString("distribution2"));
		}
		//客户跟踪情况    
		if(msg.has("trackingstatus") && !msg.getString("trackingstatus").equals("")){
			sql += " and trackingstatus like '%"+msg.getString("trackingstatus")+ "%'";
		}
		System.out.println("list-sql-::"+sql);
		sql += " order by updatetime desc LIMIT " + (page - 1) * prepage + ","+prepage;
		jdbcTemplate.query(sql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				JSONObject json = new JSONObject();
				json.put("id", rs.getInt("id"));
				json.put("name", rs.getString("name"));
				json.put("company", rs.getString("company"));
				json.put("housearea", rs.getString("housearea"));
				json.put("leasetype", rs.getString("leasetype"));
				json.put("industry", rs.getString("industry"));
				json.put("demandarea", rs.getString("demandarea"));
				json.put("telephone", rs.getString("telephone"));
				json.put("distribution", rs.getString("distribution"));
				json.put("trackingstatus", rs.getString("trackingstatus"));
				json.put("quality", rs.getString("quality"));
				json.put("nature", rs.getString("nature"));
				json.put("optusername", rs.getString("optusername"));
				json.put("isDriving", rs.getString("isDriving"));
				json.put("driviecontent", rs.getString("driviecontent"));
				String time = rs.getString("createtime");
				String timeStr=time.substring(0, time.indexOf("."));
				json.put("createtime",timeStr);
				String time1 = rs.getString("updatetime");
				String timeStr1=time1.substring(0, time1.indexOf("."));
				json.put("updatetime",timeStr1);
				jsarray.add(json);
			}
		});
		return jsarray;
	}

	public boolean updateCustomer(final JSONObject cus, final String username) {
		String sql = "UPDATE customer SET name = ?, company = ? ,housearea = ?,industry = ?"
				+ ",demandarea=?, telephone = ? ,distribution = ?,trackingstatus = ?, quality = ? ,nature = ?"
				+ ", optusername = ? ,isDriving = ?,driviecontent = ?,leasetype=?, updatetime = NOW() WHERE id = ?";
		int i = jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, cus.getString("name"));
				ps.setString(2, cus.getString("company"));
				ps.setString(3, cus.getString("housearea"));
				ps.setString(4, cus.getString("industry"));
				ps.setString(5, cus.getString("demandarea"));
				ps.setString(6, cus.getString("telephone"));
				ps.setString(7, cus.getString("distribution"));
				ps.setString(8, cus.getString("trackingstatus"));
				ps.setString(9, cus.getString("quality"));
				ps.setString(10, cus.getString("nature"));
				ps.setString(11, cus.getString("optusername"));
				ps.setString(12, cus.getString("isDriving"));
				ps.setString(13, cus.getString("driviecontent"));
				ps.setString(14, cus.getString("leasetype"));
				ps.setInt(15, cus.getInt("id"));
				
			}
		});
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	public int getCustomerCount(JSONObject msg, int page, int prepage) {
		String sql = "SELECT count(*) FROM customer WHERE isdelete = 1";
		//客户ID
		/*if(msg.has("id") && !msg.getString("id").equals("")){
			sql += " and id = "+Integer.parseInt(msg.getString("id"));
		}*/
		//房源面积
		if(msg.has("housearea1") && !msg.getString("housearea1").equals("")){
			sql += " and housearea >= "+Integer.parseInt(msg.getString("housearea1"));
		}
		if(msg.has("housearea2") && !msg.getString("housearea2").equals("")){
			sql += " and housearea <= "+Integer.parseInt(msg.getString("housearea2"));
		}
		//客户行业
		if(msg.has("industry") && !msg.getString("industry").equals("")
				&& !msg.getString("industry").equals("[]")){
			sql += " and industry = '"+msg.getString("industry")+"'";
		}
		//客户姓名
		if(msg.has("name") && !msg.getString("name").equals("")){
			sql += " and name like '%"+msg.getString("name")+ "%'";
		}
		//客户单位
		if(msg.has("company") && !msg.getString("company").equals("")){
			sql += " and company like '%"+msg.getString("company")+ "%'";
		}
		//客户手机号
		if(msg.has("telephone") && !msg.getString("telephone").equals("")){
			sql += " and telephone like '%"+msg.getString("telephone").trim()+ "%'";
		}
		//客户需求区域
		if(msg.has("demandarea") && !msg.getString("demandarea").equals("") && !msg.getString("demandarea").equals("全部区域")){
			sql += " and demandarea = '"+msg.getString("demandarea")+ "'";
		}
		//客户是否行车  
		if(msg.has("isdriving") && !msg.getString("isdriving").equals("") && !msg.getString("isdriving").equals("全部")){
			sql += " and isDriving = '"+msg.getString("isdriving")+"'";
			if(msg.getString("isdriving").equals("true")){
				//行车  吨位    
				if(msg.has("driviecontent1") && !msg.getString("driviecontent1").equals("")){
					sql += " and driviecontent >= "+Integer.parseInt(msg.getString("driviecontent1"));
				}
				if(msg.has("driviecontent2") && !msg.getString("driviecontent2").equals("")){
					sql += " and driviecontent <= "+Integer.parseInt(msg.getString("driviecontent2"));
				}
			}
		}
		//客户配电大小 kva    
		if(msg.has("distribution1") && !msg.getString("distribution1").equals("")){
			sql += " and distribution >= "+Integer.parseInt(msg.getString("distribution1"));
		}
		if(msg.has("distribution2") && !msg.getString("distribution2").equals("")){
			sql += " and distribution <= "+Integer.parseInt(msg.getString("distribution2"));
		}
		//客户跟踪情况    
		if(msg.has("trackingstatus") && !msg.getString("trackingstatus").equals("")){
			sql += " and trackingstatus like '%"+msg.getString("trackingstatus")+ "%'";
		}
		System.out.println("list-count-customer-sql-::"+sql);
//		sql += " LIMIT " + (page - 1) * prepage + ","+prepage;
		final int[] i = new int[1];
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				i[0] = rs.getInt(1);
			}
		});
		return i[0];
	}

	public boolean checkCustomerName(String name) {
		String sql = "select * from customer where name='"+name+"'";
		final int[] i = new int[1];
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				i[0] = rs.getInt(1);
			}
		});
		if (i[0] == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean checkTrackingStatus(JSONObject cus) {
		String sql = "select * from customer where trackingstatus='"+cus.getString("trackingstatus")+"' and id="+cus.getInt("id");
		final int[] i = new int[1];
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				i[0] = rs.getInt(1);
			}
		});
		if (i[0] == 0) {
			return true;
		} else {
			return false;
		}
	}

	public JSONObject getCustomerById(String id) {
		String sql = "select * from customer where id="+id+" and isdelete=1";
		final JSONObject json = new JSONObject();
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				json.put("id", rs.getInt("id"));
				json.put("name", rs.getString("name"));
				json.put("company", rs.getString("company"));
				json.put("housearea", rs.getString("housearea"));
				json.put("leasetype", rs.getString("leasetype"));
				json.put("industry", rs.getString("industry"));
				json.put("demandarea", rs.getString("demandarea"));
				json.put("telephone", rs.getString("telephone"));
				json.put("distribution", rs.getString("distribution"));
				json.put("trackingstatus", rs.getString("trackingstatus"));
				json.put("quality", rs.getString("quality"));
				json.put("nature", rs.getString("nature"));
				json.put("optusername", rs.getString("optusername"));
				json.put("isDriving", rs.getString("isDriving"));
				json.put("driviecontent", rs.getString("driviecontent"));
				String time = rs.getString("createtime");
				String timeStr=time.substring(0, time.indexOf("."));
				json.put("createtime",timeStr);
				String time1 = rs.getString("updatetime");
				String timeStr1=time1.substring(0, time1.indexOf("."));
				json.put("updatetime",timeStr1);
			}
		});
		return json;
	}

}
