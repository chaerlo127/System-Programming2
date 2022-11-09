public class Scheduler extends Thread {
	// component
	private InterruptHandler interruptHandler;
	private Queue<Process> readyQueue;
	private Queue<Process> waitQueue;

	// shared resources(variable), critical section
	private Queue<Interrupt> interruptQueue;
	private Queue<Interrupt> fileIOCommandQueue;
	
	// working variables
	private boolean bPowerOn;
	private Process runningProcess;

	public Scheduler( Queue<Interrupt> interruptQueue,
			Queue<Interrupt> fileIOCommandQueue) {
		// components
		this.readyQueue = new Queue<Process>();
		this.waitQueue = new Queue<Process>();
		this.interruptHandler = new InterruptHandler();
		
		// association
		this.interruptQueue = interruptQueue; // 외부 통신
		this.fileIOCommandQueue = fileIOCommandQueue; 
		
		// working variable
		this.runningProcess = null;
		this.bPowerOn = true;
	}
	
	public void run() {
		while (this.bPowerOn) {
			this.interruptHandler.handle();
			if(this.runningProcess == null) {
				this.runningProcess = this.readyQueue.dequeue();
			}else {
				this.runningProcess.executeInstruction(interruptQueue);
			}
		}
	}
	

	private class InterruptHandler {
		public InterruptHandler() {
		}

		private void HandleTimeOut(Process process) {
//			getReadyQueue().enqueue(runningProcess);
//			runningProcess = getReadyQueue().dequeue();
		}
		private void HandleProcessStart(Process process) {
			process.initialize();
			readyQueue.enqueue(process);
//		getReadyQueue().enqueue(process);
		}

		private void HandleProcessTerminated(Process process) {
			process.finish();
			runningProcess = null;
		}

		private void HandleReadStart(Process process) {
//		getWaitQueue().enqueue(runningProcess);
//		runningProcess = getReadyQueue().dequeue();
		}

		private void HandleReadTerminated(Process process) {
//		getReadyQueue().enqueue(process);
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
				default:
					break;
				}
			}
		}
	}



}
