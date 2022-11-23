

public class Scheduler extends Thread {
	// component
	private InterruptHandler interruptHandler;
	private Queue<Process> readyQueue;
	private Queue<Process> waitQueue;
	
	// associations
	private Queue<Interrupt> interruptQueue;
	private Queue<Interrupt> fileIOCommandQueue;
	
	// working variables
	private boolean bPowerOn;
	private Process runningProcess;

	/////////////////////////////////////////////////
	public Scheduler( 
			Queue<Interrupt> interruptQueue, 
			Queue<Interrupt> fileIOCommandQueue) {
		// components
		this.interruptHandler = new InterruptHandler();			
		this.readyQueue = new Queue<Process>();
		this.waitQueue = new Queue<Process>();
		
		// associations
		this.interruptQueue = interruptQueue;
		this.fileIOCommandQueue = fileIOCommandQueue;
		
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
				this.runningProcess.executeInstruction(interruptQueue);
			}
		}
	}
	
	private class InterruptHandler {

		public InterruptHandler() {
		}
		
		
		// IHR (Interrupt Handling Routine)
		// Handling 함수는 거의 표준화 되어 있음. 
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
		private void HandleReadStart(Process process) {
			// io start
			
			waitQueue.enqueue(process); // runningProcess일 수도 있음. 
			runningProcess = readyQueue.dequeue();
		}
		private void HandleReadTerminated(Process process) {
			
			waitQueue.dequeue(runningProcess); // 특정 프로세스를 뽑는 함수가 필요하다. --> 기말고사 점수가 포함 된다. read start가 되려면 파라미터를 뽑아야한다. 프로세스를 파일 시스템에게 일을 시켜야 한다. waitqueue에 저장하기 전에 일을 시키고 넣어줘야 한다.
			readyQueue.enqueue(process);
		}

		private void HandleWriteStart(Process process) {
			// io start
			
			waitQueue.enqueue(process); // runningProcess일 수도 있음. 
			runningProcess = readyQueue.dequeue();			
		}

		private void HandleWriteTerminated(Process process) {
			waitQueue.dequeue(runningProcess);
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
				case eReadStart:
					HandleReadStart(interrupt.getProcess());
					break;
				case eReadTerminated:
					HandleReadTerminated(interrupt.getProcess());
					break;
				case eWriteStart:
					HandleWriteStart(interrupt.getProcess());
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
