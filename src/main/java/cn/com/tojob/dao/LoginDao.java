package cn.com.tojob.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import net.sf.json.JSONObject;

@Repository(value="loginDao")
public class LoginDao {
	@Resource(name = "jdbcTemplate")
	private JdbcTemplate Jdbc;

	public JSONObject toLogin(final JSONObject user) {
		String sql = "select * from users where username = '"+user.getString("username")+"' and password = '"+user.getString("password")+"' and isdelete = 1";
		final JSONObject json = new JSONObject();
		Jdbc.query(sql, new RowCallbackHandler() {
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				json.put("userid", rs.getInt("id"));
				json.put("username", rs.getString("username"));
				json.put("level", rs.getString("level"));
			}
		});
		System.out.println("jsonarray----dao----"+json);
		return json;
		
	}

}
