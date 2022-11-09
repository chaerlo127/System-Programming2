public class FileSystem extends Thread {
	private Queue<Interrupt> interruptQueue;
	
	public FileSystem(Queue<Interrupt> interruptQueue) {
		this.interruptQueue = interruptQueue;
	}
	
	public void finish() {}
	public void initialize() {}
	@Override
	public void run() {
	}
}
