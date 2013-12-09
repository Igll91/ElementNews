package silvio.vuk.element.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import silvio.vuk.element.news.News;

public class NewsSetExtractor implements ResultSetExtractor<News> {

	@Override
	public News extractData(ResultSet rs) throws SQLException, DataAccessException 
	{
		News news = new News();
		news.setId(rs.getInt(1));
		news.setTitle(rs.getString(2));
		news.setAuthor(rs.getString(3));
		news.setText(rs.getString(4));
		return news;
	}

}
