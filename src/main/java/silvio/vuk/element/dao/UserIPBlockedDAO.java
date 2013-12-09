package silvio.vuk.element.dao;

public interface UserIPBlockedDAO {

	public abstract boolean checkBlockedIP(String ip);
	
	public abstract void removeBlockedIP(String ip);
	
	public abstract void insertBlockedIP(String ip);
}
