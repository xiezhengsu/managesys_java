package cn.com.tojob.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Repository(value = "messageDao")
public class MessageDao {
	
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate Jdbc;

	public boolean addJobsInfo(final JSONObject msg) {
		String sql = "INSERT INTO jobs (jobtitle,jobcontent,jobcreater,jobtype,isdelete,createtime) VALUES (?,?,?,1,1,NOW())";
		int i = Jdbc.update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, msg.getString("title"));
				ps.setString(2, msg.getString("content"));
				ps.setString(3, msg.getString("creater"));
			}
		});
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	public int deleteMsgByid(String id) {
		String sql = "delete from jobs WHERE jobid = " + id;
		// System.out.println(sql);
		return Jdbc.update(sql);
	}

	public JSONArray getJobsList(String title, int page) {
		String sql = "";
		final JSONArray jsarray = new JSONArray();
		if (title != null && !title.equals("")) {
			sql = "SELECT * FROM jobs WHERE isdelete = 1 and jobtitle LIKE '%"
					+ title + "%' LIMIT " + (page - 1) * 10 + ",10";
		} else {
			sql = "SELECT * FROM jobs  WHERE isdelete = 1  LIMIT " + (page - 1)
					* 10 + ",10";
		}
		Jdbc.query(sql, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				JSONObject json = new JSONObject();
				json.put("jobid", rs.getInt("jobid"));
				json.put("jobtitle", rs.getString("jobtitle"));
				json.put("jobcontent", rs.getString("jobcontent"));
				json.put("jobcreater", rs.getString("jobcreater"));
				json.put("createtime", rs.getString("createtime"));
				jsarray.add(json);
			}
		});
		System.out.println("jsonarray----dao----"+jsarray);
		return jsarray;
	}

	public JSONObject getJobInfoById(String id) {
		String sql = "SELECT * FROM jobs WHERE jobid = " + id;
		final JSONObject json = new JSONObject();
		Jdbc.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				json.put("jobid", rs.getInt("id"));
				json.put("jobtitle", rs.getString("title"));
				json.put("jobcontent", rs.getString("content"));
				json.put("jobcreater", rs.getString("creater"));
				json.put("creattime", rs.getString("creattime"));
			}
		});
		return json;
	}

	public boolean updateJobInfo(final JSONObject jobStr) {
		String sql = "UPDATE jobs SET jobtitle = ?, jobcontent = ? ,jobcreater = ?,createtime = ? WHERE jobid = ?";
		int i = Jdbc.update(sql, new PreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, jobStr.getString("title"));
				ps.setString(2, jobStr.getString("content"));
				ps.setString(3, jobStr.getString("creater"));
				ps.setString(4, jobStr.getString("createtime"));
				ps.setInt(5, jobStr.getInt("id"));
				
			}
		});
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

}
