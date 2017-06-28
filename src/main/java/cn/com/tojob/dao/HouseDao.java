package cn.com.tojob.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

@Repository(value = "houseDao")
public class HouseDao {
	
	@Resource(name="jdbcTemplate")
	public JdbcTemplate jdbcTemplate;

	public boolean addHouseInfo(final JSONObject msg, final String username) {
		//String sql = "insert into customer (name, company, housearea, industry, demandarea, telephone, distribution, trackingstatus, quality, nature, optusername, isDriving, driviecontent, createtime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())";
//		String sql = "insert into house(area,housearea,rent,company,companyaddr,manageuser,distribution,"
//				+ "currentstatus,salefloor,optusername,isDriving,driviecontent,"
//				+ "detailcontent,createtime,leasetype) values (?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?)";
		String sql = "insert into `house` (`manageuser`, `company`, `companyaddr`, `housearea`,"
				+ " `leasetype`, `rent`, `housedistrict`, `leasefloor`, `distribution`, `isDriving`, "
				+ "`drivieton`, `currentstatus`, `detailcontent`, `optusername`, `createtime`, `updatetime`, `telephone`) "
				+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),NOW(),?)";

		int i = jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, msg.getString("manageuser"));
				ps.setString(2, msg.getString("company"));
				ps.setString(3, msg.getString("companyaddr"));
				ps.setString(4, msg.getString("housearea"));
				ps.setString(5, msg.getString("leasetype"));
				ps.setString(6, msg.getString("rent"));
				ps.setString(7, msg.getString("housedistrict"));
				ps.setString(8, msg.getString("leasefloor"));
				ps.setString(9, msg.getString("distribution"));
				ps.setString(10, msg.getString("isDriving"));
				ps.setString(11, msg.getString("drivieton"));
				ps.setString(12, msg.getString("currentstatus"));
				ps.setString(13, msg.getString("detailcontent"));
				ps.setString(14, username);
				ps.setString(15, msg.getString("telephone"));
			}
		});
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	public int deleteHouseByid(String id,String username) {
//		String sql = "delete from house where id ="+id;
		String sql = "update house set isdelete=0,optusername='"+username+"' where id ="+Integer.parseInt(id);
		int i = jdbcTemplate.update(sql);
		return i;
	}

	public JSONArray getHouselist(JSONObject msg, int page, int prepage) {
		final JSONArray jsarray = new JSONArray();
		String sql = "SELECT * FROM house WHERE isdelete = 1";
		//房源ID
//		if(msg.has("id") && !msg.getString("id").equals("")){
//			sql += " and id = "+Integer.parseInt(msg.getString("id"));
//		}
		//房源区域
		if(msg.has("housedistrict") && !msg.getString("housedistrict").equals("")){
			sql += " and housedistrict = '"+msg.getString("housedistrict")+ "'";
		}
		//房源面积
		if(msg.has("housearea1") && !msg.getString("housearea1").equals("")){
			sql += " and housearea >= "+Integer.parseInt(msg.getString("housearea1"));
		}
		if(msg.has("housearea2") && !msg.getString("housearea2").equals("")){
				sql += " and housearea <= "+Integer.parseInt(msg.getString("housearea2"));
		}
		//出租楼层
		if(msg.has("leasefloor") && !msg.getString("leasefloor").equals("")){
			sql += " and leasefloor = '"+msg.getString("leasefloor")+"'";
		}
		//单位名称
		if(msg.has("company") && !msg.getString("company").equals("")){
			sql += " and company like '%"+msg.getString("company")+ "%'";
		}
		//公司地址
		if(msg.has("companyaddr") && !msg.getString("companyaddr").equals("")){
			sql += " and companyaddr like '%"+msg.getString("companyaddr")+ "%'";
		}
		//手机号
		if(msg.has("telephone") && !msg.getString("telephone").equals("")){
			sql += " and telephone like '%"+msg.getString("telephone").trim()+ "%'";
		}
		//负责人姓名
		if(msg.has("manageuser") && !msg.getString("manageuser").equals("")){
			sql += " and manageuser like '%"+msg.getString("manageuser")+ "%'";
		}
		//目前状况
		if(msg.has("currentstatus") && !msg.getString("currentstatus").equals("")){
			sql += " and currentstatus like '%"+msg.getString("currentstatus")+ "%'";
		}
		//配电大小
		if(msg.has("distribution1") && !msg.getString("distribution1").equals("")){
			sql += " and distribution >= "+Integer.parseInt(msg.getString("distribution1"));
		}
		if(msg.has("distribution2") && !msg.getString("distribution2").equals("")){
			sql += " and distribution <= "+Integer.parseInt(msg.getString("distribution2"));
		}
		//是否需要行车
		if(msg.has("isdriving") && !msg.getString("isdriving").equals("")&& !msg.getString("isdriving").equals("全部")){
			sql += " and isDriving = '"+msg.getString("isdriving")+ "'";
			if(msg.getString("isdriving").equals("true")){
				if(msg.has("driviecontent1") && !msg.getString("driviecontent1").equals("")){
					sql += " and drivieton >= "+msg.getString("driviecontent1");
				}
				if(msg.has("driviecontent2") && !msg.getString("driviecontent2").equals("")){
					sql += " and drivieton <= "+msg.getString("driviecontent2");
				}
			}
		}
		System.out.println("list-sql-::"+sql);
		sql += " order by updatetime desc LIMIT " + (page - 1) * prepage + ","+prepage;
		
		jdbcTemplate.query(sql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				JSONObject json = new JSONObject();
				json.put("id", rs.getInt("id"));
				json.put("manageuser", rs.getString("manageuser"));
				json.put("company", rs.getString("company"));
				json.put("companyaddr", rs.getString("companyaddr"));
				json.put("housearea", rs.getString("housearea"));
				json.put("telephone", rs.getString("telephone"));
				json.put("leasetype", rs.getString("leasetype"));
				json.put("rent", rs.getString("rent"));
				json.put("housedistrict", rs.getString("housedistrict"));
				json.put("leasefloor", rs.getString("leasefloor"));
				json.put("distribution", rs.getString("distribution"));
				json.put("isDriving", rs.getString("isDriving"));
				json.put("drivieton", rs.getString("drivieton"));
				json.put("currentstatus", rs.getString("currentstatus"));
				json.put("detailcontent", rs.getString("detailcontent"));
				json.put("optusername", rs.getString("optusername"));
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
	
	public int getHouseCount(JSONObject msg, int page, int prepage) {
		String sql = "SELECT count(*) FROM house WHERE isdelete = 1";
		//房源ID
//		if(msg.has("id") && !msg.getString("id").equals("")){
//			sql += " and id = "+Integer.parseInt(msg.getString("id"));
//		}
		//房源区域
		if(msg.has("housedistrict") && !msg.getString("housedistrict").equals("")){
			sql += " and housedistrict = '"+msg.getString("housedistrict")+ "'";
		}
		//房源面积
		if(msg.has("housearea1") && !msg.getString("housearea1").equals("")){
			sql += " and housearea >= "+Integer.parseInt(msg.getString("housearea1"));
		}
		if(msg.has("housearea2") && !msg.getString("housearea2").equals("")){
				sql += " and housearea <= "+Integer.parseInt(msg.getString("housearea2"));
		}
		//出租楼层
		if(msg.has("leasefloor") && !msg.getString("leasefloor").equals("")){
			sql += " and leasefloor = '"+msg.getString("leasefloor")+"'";
		}
		//单位名称
		if(msg.has("company") && !msg.getString("company").equals("")){
			sql += " and company like '%"+msg.getString("company")+ "%'";
		}
		//公司地址
		if(msg.has("companyaddr") && !msg.getString("companyaddr").equals("")){
			sql += " and companyaddr like '%"+msg.getString("companyaddr")+ "%'";
		}
		//手机号
		if(msg.has("telephone") && !msg.getString("telephone").equals("")){
			sql += " and telephone like '%"+msg.getString("telephone").trim()+ "%'";
		}
		//负责人姓名
		if(msg.has("manageuser") && !msg.getString("manageuser").equals("")){
			sql += " and manageuser like '%"+msg.getString("manageuser")+ "%'";
		}
		//目前状况
		if(msg.has("currentstatus") && !msg.getString("currentstatus").equals("")){
			sql += " and currentstatus like '%"+msg.getString("currentstatus")+ "%'";
		}
		//配电大小
		if(msg.has("distribution1") && !msg.getString("distribution1").equals("")){
			sql += " and distribution >= "+Integer.parseInt(msg.getString("distribution1"));
		}
		if(msg.has("distribution2") && !msg.getString("distribution2").equals("")){
			sql += " and distribution <= "+Integer.parseInt(msg.getString("distribution2"));
		}
		//是否行车  
		if(msg.has("isdriving") && !msg.getString("isdriving").equals("") && !msg.getString("isdriving").equals("全部")){
			sql += " and isDriving = '"+msg.getString("isdriving")+"'";
			if(msg.getString("isdriving").equals("true")){
				//行车  吨位    
				if(msg.has("driviecontent1") && !msg.getString("driviecontent1").equals("")){
					sql += " and drivieton >= "+Integer.parseInt(msg.getString("driviecontent1"));
				}
				if(msg.has("driviecontent2") && !msg.getString("driviecontent2").equals("")){
					sql += " and drivieton <= "+Integer.parseInt(msg.getString("driviecontent2"));
				}
			}
		}		
		System.out.println("list-house-sql-::"+sql);
		final int[] i = new int[1];
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				i[0] = rs.getInt(1);
			}
		});
		return i[0];
	}

	public boolean updateHouse(final JSONObject house) {
		String sql = "UPDATE house SET manageuser = ?, company = ? ,companyaddr = ?,housearea = ?"
				+ ", leasetype = ? ,rent = ?,housedistrict = ?, leasefloor = ? ,"
				+ "distribution = ?,isDriving = ?,drivieton = ?,detailcontent=?,"
				+ "optusername=?,telephone=?, updatetime = NOW(),currentstatus=? WHERE id = ?";
		int i = jdbcTemplate.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, house.getString("manageuser"));
				ps.setString(2, house.getString("company"));
				ps.setString(3, house.getString("companyaddr"));
				ps.setString(4, house.getString("housearea"));
				ps.setString(5, house.getString("leasetype"));
				ps.setString(6, house.getString("rent"));
				ps.setString(7, house.getString("housedistrict"));
				ps.setString(8, house.getString("leasefloor"));
				ps.setString(9, house.getString("distribution"));
				ps.setString(10, house.getString("isDriving"));
				ps.setString(11, house.getString("drivieton"));
				ps.setString(12, house.getString("detailcontent"));
				ps.setString(13, house.getString("optusername"));
				ps.setString(14, house.getString("telephone"));
				ps.setString(15, house.getString("currentstatus"));
				ps.setInt(16, house.getInt("id"));
			}
		});
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkHouseStatus(JSONObject house) {
		String sql = "select * from house where currentstatus='"+house.getString("currentstatus")+"' and id="+house.getInt("id");
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

	public JSONObject getHouseById(String id) {
		String sql = "select * from house where id="+id+" and isdelete=1";
		final JSONObject json = new JSONObject();
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				json.put("id", rs.getInt("id"));
				json.put("manageuser", rs.getString("manageuser"));
				json.put("company", rs.getString("company"));
				json.put("companyaddr", rs.getString("companyaddr"));
				json.put("housearea", rs.getString("housearea"));
				json.put("telephone", rs.getString("telephone"));
				json.put("leasetype", rs.getString("leasetype"));
				json.put("rent", rs.getString("rent"));
				json.put("housedistrict", rs.getString("housedistrict"));
				json.put("leasefloor", rs.getString("leasefloor"));
				json.put("distribution", rs.getString("distribution"));
				json.put("isDriving", rs.getString("isDriving"));
				json.put("drivieton", rs.getString("drivieton"));
				json.put("currentstatus", rs.getString("currentstatus"));
				json.put("detailcontent", rs.getString("detailcontent"));
				json.put("optusername", rs.getString("optusername"));
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
