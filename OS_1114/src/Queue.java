import java.util.Vector;

public class Queue<T> extends Vector<T>{
	private static final long serialVersionUID = 1L;
	
	private final int MAX_NUM_ELEMENT = 10;

	private int head, tail, currentSize, maxSize;
	public void initialize() {
	}
	public void finish() {
	}
	public Queue() {
		this.maxSize = MAX_NUM_ELEMENT;
		this.currentSize = 0;
		this.head = 0;
		this.tail = 0;		
		
		for (int i=0; i< this.maxSize; i++) {
			this.add(null);
		}
	}
	public Queue(int maxSize) {
		this.maxSize = maxSize;
		this.currentSize = 0;
		this.head = 0;
		this.tail = 0;		
		
		for (int i=0; i< this.maxSize; i++) {
			this.add(null);
		}
	}
	
	public void enqueue(T element) {
		if (this.currentSize < this.maxSize) { 
			this.set(this.tail, element);
			this.tail = (this.tail + 1) % this.maxSize;
			this.currentSize++;
		}
	}
	public T dequeue() {
		T element = null;
		if (this.currentSize > 0) {
			element = this.get(this.head);
			this.head = (this.head + 1) % this.maxSize;
			this.currentSize--;
		}
		return element;
	}
}
