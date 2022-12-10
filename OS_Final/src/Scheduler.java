

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
		this.bPowerOn = false;
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
	
	Thread currentThread;
	TimerInterrupt killInterrupt;
	public void run() {
		while (this.bPowerOn) {
			this.interruptHandler.handle();
			if (this.runningProcess == null) {
				this.runningProcess = this.readyQueue.dequeue();
				if(this.runningProcess != null) {
					// timer 생성
					this.currentThread =  Thread.currentThread();
					killInterrupt = new TimerInterrupt(currentThread);
					killInterrupt.start();
				}
			} else {
				if(!this.runningProcess.executeInstruction(interruptQueue, fileIOInterruptQueue, killInterrupt)) { // interrupt 발생하거나 halt가 발생하게 되면
					this.runningProcess = null;
					killInterrupt.interrupt(); // timer 삭제
				}
			}
		}
	}
	
	private class InterruptHandler {

		public InterruptHandler() {
		}
		// IHR (Interrupt Handling Routine)
		// Handling 함수는 거의 표준화 되어 있음.
		// process의 포인터를 잡아준다 파라미터가 있으면 process에서 인터럭트를 걸기전에 이미 push 해줬음. 
		private void HandleTimeOut(Process process) {
			System.out.println("------------------  [" + process.getProNum() + "] Time Out Interrupt ------------------");
			readyQueue.enqueue(process);
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
			waitQueue.enqueue(process); // runningProcess일 수도 있음. 
			// 전달한 disk 위치로 가서 몇 바이트를 읽으라고 명령을 내려라 한다. 
			
			// read의 과정
			runningProcess = readyQueue.dequeue();
		}
		private void HandleReadTerminated(Process process) { // file system의 일이 끝났음.
			waitQueue.remove(runningProcess); 
			readyQueue.enqueue(process);
		}
//		private void HandleWriteStart(Process process) {
//			// io start
//			
//			waitQueue.enqueue(process); // runningProcess일 수도 있음. 
//			runningProcess = readyQueue.dequeue();			
//		}
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
