package silvio.vuk.element.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import silvio.vuk.element.news.News;

public class NewsRowMapper implements RowMapper<News> {

	@Override
	public News mapRow(ResultSet rs, int arg1) throws SQLException {
		NewsSetExtractor nse = new NewsSetExtractor();
		return nse.extractData(rs);
	}

}
