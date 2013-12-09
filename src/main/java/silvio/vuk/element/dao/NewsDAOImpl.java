package silvio.vuk.element.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import silvio.vuk.element.news.News;

@Component("newsDAO")
public class NewsDAOImpl implements NewsDAO {

	private static final String NEWS_TABLE = "table_news";
	private final int NEWS_PER_PAGE = 10;
	
	@Autowired
	private DataSource dataSource;

	public void setDataSource(DataSource ds) 
	{
		dataSource = ds;
	}

	/**
	 * Dohvaæa posljednjih {@link NewsDAOImpl#NEWS_PER_PAGE} vijesti, krenuvši od vrijednosti prenesene parametrom.
	 * 
	 * @param limit broj koji predstavlja od koje vijesti æe se dohvaæati podaci.
	 * @return listu vijesti.
	 */
	@Override
	public List<News> getNews(int limit) {
		JdbcTemplate select = new JdbcTemplate(dataSource);
		
		String sql = "SELECT * FROM " + NEWS_TABLE + " ORDER BY id DESC limit " +limit + "," +(limit + NEWS_PER_PAGE);
		
		return select.query(sql, new NewsRowMapper());
	}

	/**
	 * Spremanje pojedine vijesti u bazu podataka.
	 * 
	 * @param news vijest koja se unosi.
	 */
	@Override
	public void insertSingleNews(News news) {
		JdbcTemplate insert = new JdbcTemplate(dataSource);
		
		String sql = "INSERT INTO " + NEWS_TABLE + "(title, author, txt) VALUES(?,?,?)";
		
	    insert.update(sql, new Object[] { news.getTitle(), news.getAuthor(), news.getText() });

	}

	/**
	 * Provjerava dali vijest veæ postoji u bazi.
	 * 
	 * @param news vijest koja se provjerava.
	 * @return true ako postoji, false ako ne postoji.
	 */
	@Override
	public boolean checkIfNewsExists(News news) {
		JdbcTemplate check = new JdbcTemplate(dataSource);
		
		String sql = "SELECT COUNT(id) FROM " + NEWS_TABLE + " WHERE title = ?";
		
		try
		{
			int count = check.queryForObject(sql, Integer.class, news.getTitle());

			switch(count)
			{
			case 0:
				return false;
			case 1:
				return true;
			default:
				throw new IllegalStateException("Query returned value > 1! NOT ALLOWED!");
			}
		}
		catch(IncorrectResultSizeDataAccessException ex)
		{
			throw new IllegalStateException("Query returned more than 1 row! NOT ALLOWED!");
		}
	}

	/**
	 * Vraæa broj ukupnih vijesti.
	 * 
	 * @return broj vijesti u bazi.
	 */
	@Override
	public int numberOfNews() {
		JdbcTemplate check = new JdbcTemplate(dataSource);

		String sql = "SELECT COUNT(id) FROM " + NEWS_TABLE;

		int count = check.queryForObject(sql, Integer.class);
		return count;
	}

}
