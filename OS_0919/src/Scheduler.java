import java.util.Vector;

public class Scheduler extends Thread {
	// component
	private InterruptHandler interruptHandler;
	
	// attributes
	private boolean bPowerOn;
	
	// shared resources(variable), critical section
	private Queue<Process> readyQueue;
	private Queue<Process> waitQueue;
	private Queue<Interrupt> interruptQueue;
	private Queue<FileIOCommand> fileIOCommandQueue;
	private Vector<Integer> filesystemBuffer;
	
	
	// working variables
	private Process runningProcess;

	public Scheduler(Queue<Process> readyQueue, Queue<Process> waitQueue, Queue<Interrupt> interruptQueue,
			Queue<FileIOCommand> fileIOCommandQueue, Vector<Integer> filesystemBuffer) {
		this.bPowerOn = true;
		this.readyQueue = readyQueue;
		this.waitQueue = waitQueue;
		this.interruptQueue = interruptQueue;
		this.fileIOCommandQueue = fileIOCommandQueue; 
		this.filesystemBuffer = filesystemBuffer;
		
		this.interruptHandler = new InterruptHandler();
		
		this.runningProcess = null;
		
	}
	
	public void run() {
		while (this.bPowerOn) {
			this.runningProcess = this.readyQueue.dequeue(); // null이면 blocking이 된다. --> 왜 deReadyQueue가 아닌지
			
			boolean result = this.runningProcess.executeInstruction();
			this.interruptHandler.handle();
		}
	}

}
