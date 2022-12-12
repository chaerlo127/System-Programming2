import constraint.Config;

public class QueueSynchronized<T> extends Queue<T>{
	private static final long serialVersionUID = 1L;
	
	public QueueSynchronized() {
		super(Config.MAX_NUM_ELEMENT);
	}
	public synchronized void enqueue(T element) {
		super.enqueue(element);
	}
	public synchronized T dequeue() {
		T element = super.dequeue();
		return element;
	}
}
