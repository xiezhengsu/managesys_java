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

@Repository(value = "userDao")
public class UserDao {

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate Jdbc;
	
	public boolean checkUserName(String string) {
		String sql = "SELECT COUNT(*) FROM users WHERE username= '" + string + "'";
		final int[] i = new int[1];
		Jdbc.query(sql, new RowCallbackHandler() {

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

	public boolean addUser(final JSONObject user) {
		String sql = "INSERT INTO users (username,password,realname,telephone,mail,gender,level,createtime) VALUES (?,?,?,?,?,?,?,NOW())";
		int i = Jdbc.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, user.getString("username"));
				ps.setString(2, user.getString("password"));
				ps.setString(3, user.getString("realname"));
				ps.setString(4, user.getString("telephone"));
				ps.setString(5, user.getString("mail"));
				ps.setString(6, user.getString("gender"));
				ps.setString(7, user.getString("level"));
			}
		});
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	public int getUserIdByname(String string) {
		String sql = "SELECT id FROM users WHERE  username='" + string + "'";
		final int[] i = new int[1];
		Jdbc.query(sql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				i[0] = rs.getInt("id");
			}
		});
		return i[0];
	}

	public int deleteUserByid(String userId) {
		String sql = "UPDATE users UR SET UR.isdelete = 0 WHERE UR.id = " + userId;
		// System.out.println(sql);
		return Jdbc.update(sql);
	}

	public JSONArray getUserLists(String name, int page) {
		String sql = "";
		final JSONArray jsarray = new JSONArray();
		if (name != null && !name.equals("")) {
			sql = "SELECT * FROM users WHERE isdelete = 1 AND  username  LIKE '%"
					+ name + "%' LIMIT " + (page - 1) * 10 + ",10";
		} else {
			sql = "SELECT * FROM users  WHERE isdelete = 1  LIMIT " + (page - 1)
					* 10 + ",10";
		}
		Jdbc.query(sql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				JSONObject json = new JSONObject();
				json.put("id", rs.getInt("id"));
				json.put("username", rs.getString("username"));
				json.put("password", rs.getString("password"));
				json.put("realname", rs.getString("realname"));
				json.put("telephone", rs.getString("telephone"));
				json.put("mail", rs.getString("mail"));
				json.put("gender", rs.getString("gender"));
				json.put("level", rs.getString("level"));
				json.put("desc", rs.getString("desc"));
				String time = rs.getString("createtime");
				String timeStr=time.substring(0, time.indexOf("."));
				json.put("createtime", timeStr);
				jsarray.add(json);
			}
		});
		return jsarray;
	}

	public JSONObject getUserByid(String id) {
		String sql = "SELECT * FROM users WHERE id = " + id;
		final JSONObject json = new JSONObject();
		Jdbc.query(sql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				json.put("id", rs.getInt("id"));
				json.put("username", rs.getString("username"));
				json.put("realname", rs.getString("realname"));
				json.put("telephone", rs.getString("telephone"));
				json.put("mail", rs.getString("mail"));
				json.put("gender", rs.getString("gender")=="male"?"男":"女");
				json.put("level", rs.getString("level"));
				json.put("desc", rs.getString("desc"));
				json.put("createtime", rs.getString("createtime"));

			}
		});
		return json;
	}

	public boolean updateUser(final JSONObject user) {
		String sql = "UPDATE users SET username = ?, password = ? ,realname = ?,telephone = ? ,mail = ? ,gender = ?,level=?,createtime=NOW() WHERE id = ?";
		int i = Jdbc.update(sql, new PreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, user.getString("username"));
				ps.setString(2, user.getString("password"));
				ps.setString(3, user.getString("realname"));
				ps.setString(4, user.getString("telephone"));
				ps.setString(5, user.getString("mail"));
				ps.setString(6, user.getString("gender"));
				ps.setString(7, user.getString("level"));
				ps.setInt(8, user.getInt("id"));
				
			}
		});
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	public int getUserCount(String name) {
		String sql = "select count(*) from users where isdelete=1";
		if (name != null && !name.equals("")) {
			sql += " and username  LIKE '%"+ name + "%'";
		}
		final int[] i = new int[1];
		Jdbc.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				i[0] = rs.getInt(1);
			}
		});
		return i[0];
	}
	
	

}
