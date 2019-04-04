package f8;

import java.io.Serializable;

public class Request implements Serializable {
	private static final long serialVersionUID = 7782539125550354619L;
	private Type type;
	private String name;
	private int age;
	
	public enum Type {
		PUT,GET,LIST,REMOVE
	}
	
	public Request(Type type, String name, int age) {
		this.type = type;
		this.name = name;
		this.age = age;
	}

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}
	
	public String toString() {
		return type + " " + name + " " +age;
	}
}

