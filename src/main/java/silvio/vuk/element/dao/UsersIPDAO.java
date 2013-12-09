package silvio.vuk.element.dao;

public interface UsersIPDAO {

	public abstract int checkIP(String ip);
	
	public abstract void removeIP(String ip);
	
	public abstract void insertIP(String ip);
	
	public abstract void increaseIPsOccurrence(String ip, int occurrence);
}
