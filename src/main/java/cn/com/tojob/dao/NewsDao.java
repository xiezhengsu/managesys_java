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

@Repository(value = "newsDao")
public class NewsDao {

	@Resource(name = "jdbcTemplate")
	private JdbcTemplate Jdbc;

	public boolean addNewsInfo(final JSONObject news) {
		String sql = "insert into news(newstitle,newscontent,newsuser,createtime,newstype)value(?,?,?,NOW(),1)";
		int i = Jdbc.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, news.getString("title"));
				ps.setString(2, news.getString("content"));
				ps.setString(3, news.getString("user"));
			}
		});
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	public int deleteNewsById(String newsid) {
		String sql = "delete from news WHERE newsid = " + newsid;
		// System.out.println(sql);
		return Jdbc.update(sql);
	}

	public JSONArray getNewslist(String title, String page) {
		String sql = "";
		final JSONArray jsarray = new JSONArray();
		if (title != null && !title.equals("")) {
			sql = "SELECT * FROM news WHERE isdelete = 1 and newstitle LIKE '%"
					+ title + "%' LIMIT " + (Integer.parseInt(page) - 1) * 10 + ",10";
		} else {
			sql = "SELECT * FROM news  WHERE isdelete = 1  LIMIT " + (Integer.parseInt(page) - 1)
					* 10 + ",10";
		}
		Jdbc.query(sql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				JSONObject json = new JSONObject();
				json.put("id", rs.getInt("newsid"));
				json.put("title", rs.getString("newstitle"));
				json.put("content", rs.getString("newscontent"));
				json.put("user", rs.getString("newsuser"));
				json.put("createtime", rs.getString("createtime"));
				jsarray.add(json);
			}
		});
		return jsarray;
	}

	public JSONObject getInfoById(String id) {
		String sql = "SELECT * FROM news WHERE newsid = " + id;
		final JSONObject json = new JSONObject();
		Jdbc.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				json.put("id", rs.getInt("id"));
				json.put("title", rs.getString("title"));
				json.put("content", rs.getString("content"));
				json.put("user", "admin");
				json.put("creattime", rs.getString("creattime"));
			}
		});
		return json;
	}

	public boolean updateUser(final JSONObject news) {
		String sql = "UPDATE jobs SET newstitle = ?, newscontent = ? ,newsuser = ?,createtime = ? WHERE newsid = ?";
		int i = Jdbc.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, news.getString("title"));
				ps.setString(2, news.getString("content"));
				ps.setString(3, news.getString("creater"));
				ps.setString(4, news.getString("createtime"));
				ps.setInt(5, news.getInt("id"));
				
			}
		});
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	
}
