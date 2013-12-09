package silvio.vuk.element.dao;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Svaki puta kada korisnik unese nepostojeæe/krive podatke kod logiranja, zapisuje se njegova ip adresa i broj pogrešaka.
 * 
 * @author Silvio
 *
 */
@Component("UsersIPDAO")
public class UsersIPDAOImpl implements UsersIPDAO {
	
	private static final String TABLE_IP = "table_used_ip";

	private static final Logger logger = LoggerFactory.getLogger(UsersIPDAOImpl.class);
	
	@Autowired
	private DataSource dataSource;

	public void setDataSource(DataSource ds) 
	{
		dataSource = ds;
	}
	
	/**
	 * Provjerava koliko puta je korisnik sa tom ip adresom pogriješio unos.
	 * 
	 * @param ip ip adresa koja se provjerava.
	 */
	public int checkIP(String ip)
	{
		JdbcTemplate select = new JdbcTemplate(dataSource);
		
		String sql = "SELECT occurrence FROM " + TABLE_IP + " WHERE ip = ?"; 
		try
		{
			int occ = select.queryForObject(sql, Integer.class, ip);
			
			return occ;
		}
		catch(IncorrectResultSizeDataAccessException ex)
		{
			if(ex.getMessage().equals("Incorrect result size: expected 1, actual 0"))
			{
				return 0;
			}
			else
			{
				logger.error(ex.getMessage());
				logger.error("checkIP returned more than 1 row! Re-check whole flow!");
				throw new IllegalStateException("Query returned more than 1 row! NOT ALLOWED!");
			}
		}
	    catch(DataAccessException ex)
	    {
	    	logger.error(ex.getMessage());
	    	return -1;
	    }
	}

	/**
	 * Uklanja zapis pogrešaka tog ip-a.
	 */
	@Override
	public void removeIP(String ip) {
		JdbcTemplate remove = new JdbcTemplate(dataSource);
		
		String sql = "DELETE FROM " + TABLE_IP  + " WHERE ip = ?"; 
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
	 * Zapis nove greške tog ip-a s defaultnom vrijednošæu prvog pojavljivanja = 1.
	 * 
	 * @param ip ip adresa koja se provjerava.
	 */
	@Override
	public void insertIP(String ip) {
		JdbcTemplate insert = new JdbcTemplate(dataSource);
		
		String sql = "INSERT INTO " + TABLE_IP  + " VALUES(?, 1)"; 
		try
		{
			insert.update(sql, ip);
		}
		catch(DataAccessException ex)
		{
			logger.error(ex.getMessage());
		}
	}

	/**
	 * Postavljanje broja pogreške tog ip-a na odreðeni broj.
	 * 
	 * @param ip ip adresa koja se provjerava.
	 * @param occurrence broj pogrešaka
	 */
	@Override
	public void increaseIPsOccurrence(String ip, int occurrence) {
		JdbcTemplate update = new JdbcTemplate(dataSource);
		
		String sql = "UPDATE " + TABLE_IP  + " SET occurrence = ? WHERE IP = ?";
		
		try
		{
			update.update(sql, new Object[]{ occurrence, ip });
		}
		catch(DataAccessException ex)
		{
			logger.error(ex.getMessage());
		}
	}

}
