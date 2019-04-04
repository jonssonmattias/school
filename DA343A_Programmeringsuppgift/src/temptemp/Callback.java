package temptemp;

/**
 * 
 * 
 * @author Linnea Dahlgren
 *Callback interface for Callbacks between 
 *SendWindow and ClientController
 */
public interface Callback {
	public void getUser(User user);
	public void getContacts(Users users);
}
