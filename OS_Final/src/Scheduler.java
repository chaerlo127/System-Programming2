

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
	public void initialize() {}
	public void finish() {this.bPowerOn = false;}
	
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
	
	// Time out Interrupt 
	private Thread currentThread;
	private TimeOutInterruptThread killInterrupt;
	public void run() {
		while (this.bPowerOn) {
			this.interruptHandler.handle();
			if (this.runningProcess == null) { // ���μ����� ���ٸ�
				this.runningProcess = this.readyQueue.dequeue();
				if(this.runningProcess != null) { 
					this.currentThread =  Thread.currentThread(); // timer ����
					killInterrupt = new TimeOutInterruptThread(currentThread);
					killInterrupt.start();
				}
			} else {
				if(!this.runningProcess.executeInstruction(interruptQueue, fileIOInterruptQueue, killInterrupt)) { // interrupt �߻��ϰų� halt�� �߻��ϰ� �Ǹ�
					this.runningProcess = null;
					killInterrupt.interrupt(); // timer ����
				}
			}
		}
	}
	
	private class InterruptHandler {
		// IHR (Interrupt Handling Routine)
		// Handling �Լ��� ���� ǥ��ȭ �Ǿ� ����.
		// process�� �����͸� ����ش� �Ķ���Ͱ� ������ process���� ���ͷ�Ʈ�� �ɱ����� �̹� push ������. 
		public InterruptHandler() {}
		private void HandleTimeOut(Process process) {
			System.out.println("------------------  [" + process.getProNum() + "] Time Out Interrupt ------------------");
			readyQueue.enqueue(process);
		}
		private void HandleProcessStart(Process process) {
			System.out.println("------------------  [" + process.getProNum() + "] Process Start ------------------");
			process.initialize();
			readyQueue.enqueue(process);
		}
		private void HandleProcessTerminated(Process process) {
			System.out.println("------------------  [" + process.getProNum() + "] Process Terminated ------------------");
			process.finish();
			runningProcess = null;
		}
		// handle open���� ���� (not ReadStart, WriteStart)
		private void HandleOpenStart(Process process) {
			waitQueue.enqueue(process); // io start
			runningProcess = readyQueue.dequeue();
		}
		private void HandleReadTerminated(Process process) {
			waitQueue.remove(runningProcess); 
			readyQueue.enqueue(process);
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
