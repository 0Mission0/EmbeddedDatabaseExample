package idv.mission.example.EmbeddedDatabaseExample;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class Test {
	public static void main(String[] args) {
		// jdbc:h2:mem:testdb
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		builder.setType(EmbeddedDatabaseType.H2);
		builder.addScript("create-db.sql");
		builder.addScript("insert-data.sql");
		
		EmbeddedDatabase database = builder.build();

		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(database);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "mission");
		
		String sql = "SELECT * FROM users WHERE name=:name";

		User user = template.queryForObject(sql, params, new UserMapper());

		System.out.println(user);
		System.out.println(user.getId().intValue());
		System.out.println(user.getName());
		System.out.println(user.getEmail());
		
		database.shutdown();
	}

	private static final class UserMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getInt("id"));
			user.setName(rs.getString("name"));
			user.setEmail(rs.getString("email"));
			return user;
		}
	}
}