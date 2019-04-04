package f8.client;

import java.io.IOException;

public interface Client {
	public void setClientController(ClientController controller);
	public void put(String name, String age) throws IOException;
	public void get(String name) throws IOException;
	public void list() throws IOException;
	public void remove(String name) throws IOException;
	public void exit() throws IOException;
}
