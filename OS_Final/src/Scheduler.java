

public class Scheduler extends Thread {
	// component
	private InterruptHandler interruptHandler;
	private Queue<Process> readyQueue;
	private Queue<Process> waitQueue;
	
	// associations
	private Queue<Interrupt> interruptQueue;
	private Queue<Interrupt> fileIOInterruptQueue;
	
	// working variables
	private boolean bPowerOn;
	private Process runningProcess;

	/////////////////////////////////////////////////
	public void initialize() {
	}
	public void finish() {
	}
	public Scheduler( 
			Queue<Interrupt> interruptQueue, Queue<Interrupt> fileIOInterruptQueue) {
		// components
		this.interruptHandler = new InterruptHandler();			
		this.readyQueue = new Queue<Process>();
		this.waitQueue = new Queue<Process>();
		
		// associations
		this.interruptQueue = interruptQueue;
		this.fileIOInterruptQueue = fileIOInterruptQueue;
		// working objects
		this.runningProcess = null;			
		this.bPowerOn = true;
	}

	public void run() {
		while (this.bPowerOn) {
			this.interruptHandler.handle();
			if (this.runningProcess == null) {
				this.runningProcess = this.readyQueue.dequeue();
			} else {
				if(!this.runningProcess.executeInstruction(interruptQueue, fileIOInterruptQueue)) { // interrupt �߻��ϰų� halt�� �߻��ϰ� �Ǹ�
					this.runningProcess = null;
				}
			}
		}
	}
	
	private class InterruptHandler {

		public InterruptHandler() {
		}
		// IHR (Interrupt Handling Routine)
		// Handling �Լ��� ���� ǥ��ȭ �Ǿ� ����.
		// process�� �����͸� ����ش� �Ķ���Ͱ� ������ process���� ���ͷ�Ʈ�� �ɱ����� �̹� push ������. 
		private void HandleTimeOut(Process process) {
//			getReadyQueue().enqueue(runningProcess);
//			runningProcess = getReadyQueue().dequeue();
		}
		private void HandleProcessStart(Process process) {
			process.initialize();
			readyQueue.enqueue(process);
		}
		private void HandleProcessTerminated(Process process) {
			process.finish();
			runningProcess = null;
		}
		private void HandleOpenStart(Process process) {
			// io start
			waitQueue.enqueue(process); // runningProcess�� ���� ����. 
			// ������ disk ��ġ�� ���� �� ����Ʈ�� ������� ����� ������ �Ѵ�. 
			
			// read�� ����
//			fileSystem.read(runningProcess); // process �ּ�
			runningProcess = readyQueue.dequeue();
		}
		private void HandleReadTerminated(Process process) { // file system�� ���� ������.
			waitQueue.remove(runningProcess); 
			// Ư�� ���μ����� �̴� �Լ��� �ʿ��ϴ�. --> �⸻��� ������ ���� �ȴ�. read start�� �Ƿ��� �Ķ���͸� �̾ƾ��Ѵ�. 
			// ���μ����� ���� �ý��ۿ��� ���� ���Ѿ� �Ѵ�. waitqueue�� �����ϱ� ���� ���� ��Ű�� �־���� �Ѵ�.
			readyQueue.enqueue(process);
		}
		private void HandleWriteStart(Process process) {
			// io start
			
			waitQueue.enqueue(process); // runningProcess�� ���� ����. 
			runningProcess = readyQueue.dequeue();			
		}
		private void HandleWriteTerminated(Process process) {
			waitQueue.remove(runningProcess);
			readyQueue.enqueue(process);			
		}

		public void handle() {
			Interrupt interrupt = interruptQueue.dequeue();
			if (interrupt != null) {
				switch (interrupt.geteInterrupt()) {
				case eTimeOut:
					HandleTimeOut(interrupt.getProcess());
					break;
				case eProcessStart:
					HandleProcessStart(interrupt.getProcess());
					break;
				case eProcessTerminated:
					HandleProcessTerminated(interrupt.getProcess());
					break;
				case eOpenStart:
					HandleOpenStart(interrupt.getProcess());
					break;
				case eReadTerminated:
					HandleReadTerminated(interrupt.getProcess());
					break;
				case eWriteTerminated:
					HandleWriteTerminated(interrupt.getProcess());
					break;
				default:
					break;
				}
			}
		}
	}
}
