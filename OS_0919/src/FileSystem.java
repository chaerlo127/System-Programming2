import java.util.Vector;

public class FileSystem extends Thread {
	private Queue<Interrupt> interruptQueue;
	private Vector<Integer> filesystemBuffer;
	
	public FileSystem(Queue<Interrupt> interruptQueue, Vector<Integer> filesystemBuffer) {
		this.interruptQueue = interruptQueue;
		this.filesystemBuffer = filesystemBuffer;
	}
	
	public void finish() {}
	public void initialize() {}
	
	@Override
	public void run() {
	}
}
