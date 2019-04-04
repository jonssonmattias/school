package temptemp;
/**
 * 
 * 
 * @author Oscar Jonsson
 * @version 1.0
 * @since 2019-03-13
 * Interface that the ServerUI uses
 *
 */
public interface ServerViewer {
	public void setController(ServerController controller);
	public void setIp(String ip);
	public void setPort(int port);
	public int getPort();
	public void addText(String log);
}
