package p6.test1;

public class OutOfArrayExeption extends RuntimeException {
	public OutOfArrayExeption() { 
        super();
    }
    public OutOfArrayExeption(String message) {
        super(message);
    }
}
