package assignment4;

import java.io.*;

public class Controller {
	private MonitorGUI monitorGUI;
	private Thread producer;
	private Thread consumer;
	private Thread modifier;
	private BoundedBuffer buffer;
	private int number=0;

	public Controller(MonitorGUI monitorGUI) {
		this.monitorGUI=monitorGUI;
	}

	public String setScourceText(File file) {
		BufferedReader reader;
		String text = "";
		try {
			reader = new BufferedReader(new FileReader(file.getPath()));
			String line = reader.readLine();
			while (line != null) {
				text+=line+"\n";
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}

	public void setDestText(String text) {
		monitorGUI.setDestText(text);
		highlightWord(monitorGUI.getReplaceText());
	}

	public void setNumberOfReplacements() {
		int number = 0;
		String[] sourceString = monitorGUI.getSourceText().trim().split(" ");
		String[] destString = monitorGUI.getDestText().trim().split(" ");
		for(int i=0;i<sourceString.length;i++)
			if(!sourceString[i].equals(destString[i])) {
				number++;
			}
		monitorGUI.setNumberOfReplacements(number);
	}

	public void highlightWord(String word) {
		int start = monitorGUI.getDestText().indexOf(word);
		int  end = start+word.length();
		while(start >= 0) {
			monitorGUI.highlightWord(start, end);
			start = monitorGUI.getDestText().indexOf(word, start+1);
			end = start+word.length();
		}
	}

	public void create(String text, String find, String replace, boolean notify) {
		int length = text.trim().split(" ").length;
		buffer = new BoundedBuffer(10, find, replace);
		modifier = new Modifier(buffer, length, notify);
		producer = new Producer(buffer, text.trim());
		consumer = new Consumer(buffer, length, this);
		consumer.start();
		producer.start();
		modifier.start();
		closeThreads();
	}

	public void saveFile(String text, String filename) throws FileNotFoundException {
		try (PrintWriter out = new PrintWriter(filename)) {
		    out.println(text);
		}
	}
	public void closeThreads() {
		if(modifier!=null)
			modifier=null;
		if(producer!=null)
			producer=null;
		if(consumer!=null)
			consumer=null;
		
	}

}
