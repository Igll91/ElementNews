package silvio.vuk.element.dao;

import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component("userBlockedIPDAO")
public class UsersIPBlockedDAOImpl implements UserIPBlockedDAO {

	private static final String TABLE_IP_BLOCKED = "table_banned_ip";

	private static final Logger logger = LoggerFactory.getLogger(UsersIPDAOImpl.class);

	@Autowired
	private DataSource dataSource;

	public void setDataSource(DataSource ds) 
	{
		dataSource = ds;
	}

	/**
	 * Provjerava dali je ip adresa blokirana.
	 * 
	 * @param ip ip adresa koja æe se provjeriti
	 */
	@Override
	public boolean checkBlockedIP(String ip) {
		JdbcTemplate select = new JdbcTemplate(dataSource);

		String sql = "SELECT COUNT(ip) AS count FROM " + TABLE_IP_BLOCKED  + " WHERE ip = ?";
		try
		{
			int count = select.queryForObject(sql, Integer.class, ip);

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
			logger.error(ex.getMessage());
			throw new IllegalStateException("Query returned more than 1 row! NOT ALLOWED!");
		}
		catch(DataAccessException ex)
		{
			logger.error(ex.getMessage());
			throw new RuntimeException();
		}
	}

	/**
	 * Uklanja se zabrana IP adrese.
	 * 
	 * @param ip ip adresa koja æe se provjeriti
	 */
	@Override
	public void removeBlockedIP(String ip) {
		JdbcTemplate remove = new JdbcTemplate(dataSource);

		String sql = "DELETE FROM " + TABLE_IP_BLOCKED  + " WHERE ip = ?"; 
		try
		{
			remove.update(sql, ip);
		}
		catch(DataAccessException ex)
		{
			logger.error(ex.getMessage());
		}
	}

	/**
	 * Stavlja se zabrana IP adrese.
	 * 
	 * @param ip ip adresa koja æe se provjeriti
	 */
	@Override
	public void insertBlockedIP(String ip) {
		JdbcTemplate insert = new JdbcTemplate(dataSource);

		String sql = "INSERT INTO " + TABLE_IP_BLOCKED  + " VALUES(?, ?)";
		DateTime dateTime = new DateTime();
		try
		{
			insert.update(sql, new Object[]{ip, dateTime.toString()});
		}
		catch(DataAccessException ex)
		{
			logger.error(ex.getMessage());
		}
	}

}
