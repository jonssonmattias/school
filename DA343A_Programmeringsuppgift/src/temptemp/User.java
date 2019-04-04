package temptemp;

import java.io.Serializable;

import javax.swing.ImageIcon;
/**
 * 
 * @author Pontus Folke och John Lindahl
 * @version 1.0
 * @since 2019-03-13
 *
 *The Class User holds the information about a user.
 *It holds a user name and profile picture.
 *
 */
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private String username;
	private ImageIcon image;
	/**
	 * 
	 * Sets both instance variables
	 * @param username
	 * @param image
	 */
	public User(String username, ImageIcon image) {
		this.username = username;
		this.image = image;
	}
	/**
	 * Sets the user name to the parameter and sets the image to a default image
	 * @param username
	 */
	public User (String username) {
		this.username = username;
		this.image = new ImageIcon("images/gubbe.jpg");
	}
	
	public User(ImageIcon image) {
		this.image = image;
		setName();
	}
	
	public User() {
		setName();
	}
	
	public void setIcon (ImageIcon image) {
		this.image = image;
	}
	
	public void setName(String username) {
		this.username = username;
	}
	
	public void setName() {
		this.username = "Unknown";
	}
	
	public String getUsername() {
		return username;
	}
	
	public ImageIcon getImage() {
		return image;
	}
	
	@Override
	public int hashCode() {
		return username.hashCode();
	}
	/**
	 * 
	 * Compares this instance to another instance of User
	 */
	@Override
	public boolean equals(Object obj) {
		User other = (User) obj;
		if (!username.equals(other.username))
			return false;
		return true;
	}

}

