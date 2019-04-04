package temptemp;

import java.io.Serializable;
import java.util.Date;

import javax.swing.ImageIcon;


/**
 * 
 * @author John Lindahl, Pontus Folke and Oscar Jonsson
 * @version 1.0
 * @since 2019-03-13
 * The Message class is a message.
 * It holds the content as a String and/or an ImageIcon.
 * It holds a list of the receiver.
 * It holds the sender and gets two dates upon sending and receiving.
 * 
 *
 */
public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	private final User sender;
	private Users recipients;
	private final String text;
	private final ImageIcon image;
	private Date timeReceived;
	private Date timeSent;
	
	/**
	 * 
	 * The constructor sets the sender,receivers and content
	 * @param sender
	 * @param recipients
	 * @param text
	 * @param image
	 */
	public Message(User sender, Users recipients, String text, ImageIcon image) {
		this.sender = sender;
		this.recipients = recipients;
		this.text = text;
		this.image = image;
	}
	
	public User getSender() {
		return sender;
	}
	
	public Users getRecipients() {
		return recipients;
	}
	
	public String getText() {
		return text;
	}
	
	public ImageIcon getImage() {
		return image;
	}
	
	public Date getTimeReceived() {
		return timeReceived;
	}
	/**
	 * Sets the date that the Message is received
	 */
	public void received() {
		timeReceived = new Date();
	}
	
	public Date getTimeSent() {
		return timeSent;
	}
	/**
	 * Sets the date that the Message is sent
	 */
	public void sent() {
		timeSent = new Date();
	}
}
