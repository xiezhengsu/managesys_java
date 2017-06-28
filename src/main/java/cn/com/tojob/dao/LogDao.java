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

@Repository(value="logDao")
public class LogDao {
	
	@Resource(name="jdbcTemplate")
	public JdbcTemplate jdbcTemplate;

	public int getLogCount(JSONObject log, int page, int prepage) {
		String sql = "select count(*) from log where isdelete =1";
		if(log.has("optcontent") && !log.getString("optcontent").equals("")){
			sql += " and optcontent like '%"+log.getString("optcontent")+"%'";
		}
		if(log.has("opttype") && !log.getString("opttype").equals("") && !log.getString("opttype").equals("全部")){
			sql += " and optcategory = '"+log.getString("opttype")+"'";
		}
		if(log.has("optuser") && !log.getString("optuser").equals("")){
			sql += " and optuser like '%"+log.getString("optuser")+"%'";
		}
		final int[] i = new int[1];
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				i[0] = rs.getInt(1);
			}
		});
		return i[0];
	}

	public JSONArray getLoglist(JSONObject log, int page, int prepage) {
		String sql = "select * from log where isdelete =1";
		if(log.has("optcontent") && !log.getString("optcontent").equals("")){
			sql += " and optcontent like '%"+log.getString("optcontent")+"%'";
		}
		if(log.has("opttype") && !log.getString("opttype").equals("") && !log.getString("opttype").equals("全部")){
			sql += " and optcategory = '"+log.getString("opttype")+"'";
		}
		if(log.has("optuser") && !log.getString("optuser").equals("")){
			sql += " and optuser like '%"+log.getString("optuser")+"%'";
		}
		sql += " order by opttime desc LIMIT " + (page - 1) * prepage + ","+prepage;
		final JSONArray jsarray = new JSONArray();
		jdbcTemplate.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				JSONObject json = new JSONObject();
				json.put("id", rs.getInt("id"));
				json.put("optuser", rs.getString("optuser"));
				json.put("opttype", rs.getString("opttype"));
				json.put("optcategory", rs.getString("optcategory"));
				json.put("optcontent", rs.getString("optcontent"));
				json.put("optdec", rs.getString("optdec"));
				String time = rs.getString("opttime");
				String timeStr=time.substring(0, time.indexOf("."));
				json.put("opttime", timeStr);
				jsarray.add(json);
			}
		});
		return jsarray;
	}

	public void innsertLog(final String optuser, final String opttype, final String optcategory, final String optcontent,
			final String optdec) {
		String sql = "insert into log(optuser,opttype,optcategory,optcontent,optdec,opttime)values(?,?,?,?,?,NOW())";
		jdbcTemplate.update(sql, new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, optuser);
				ps.setString(2, opttype);
				ps.setString(3, optcategory);
				ps.setString(4, optcontent);
				ps.setString(5, optdec);
			}
		});
	}
}
