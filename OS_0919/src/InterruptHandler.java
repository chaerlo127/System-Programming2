
public class InterruptHandler {
	
	////////////////////////////////////////////////////
	// critical section
	private Queue<Interrupt> interruptQ;
	public Interrupt get() {
		return interruptQ.dequeue();
	}
	public void set(Interrupt interrupt) {
		this.interruptQ.enqueue(interrupt);
	}
	////////////////////////////////////////////////////
	
	public InterruptHandler() {
		this.interruptQ = new Queue<Interrupt>();
	}
	
	private void HandleProcessStart(Process process) {
//		getReadyQueue().enqueue(process);
	}
	private void HandleProcessTerminate() {
//		runningProcess = getReadyQueue().dequeue();
	}
	private void HandleIOStart() {
//		getWaitQueue().enqueue(runningProcess);
//		runningProcess = getReadyQueue().dequeue();
	}
	private void HandleIOTerminate(Process process) {
//		getReadyQueue().enqueue(process);
	}
	private void HandleTimeOut() {
//		getReadyQueue().enqueue(runningProcess);
//		runningProcess = getReadyQueue().dequeue();
	}

	public void handle() {
		Interrupt interrupt = this.get();
		if (interrupt != null) {
			switch (interrupt.geteInterrupt()) {
			case eProcessStarted:
				HandleProcessStart(interrupt.getProcess());
				break;
			case eProcessTerminated:
				HandleProcessTerminate();
				break;
			case eIOStarted:
				HandleIOStart();
				break;
			case eIOTerminated:
				HandleIOTerminate(interrupt.getProcess());
				break;
			case eTimeOut:
				HandleTimeOut();
				break;
			default:
				break;
			}
		}
	}
}

