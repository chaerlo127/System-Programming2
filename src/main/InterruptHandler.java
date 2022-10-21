package main;

// ready queue에서 새로운 것을 뽑아와라
// process context switching
// waiting queue 넘김
public class InterruptHandler {
    public enum EInterrupt {
        eTimeOut,
        eIOStarted,
        eIOTerminated,
        eProcessStarted,
        eProcessTerminated,
    }

    public class Interrupt{
        private EInterrupt eInterrupt;
        private Process process;
        public EInterrupt geteInterrupt() {
            return eInterrupt;
        }
        public Process getProcess() {
            return process;
        }
        public Interrupt(EInterrupt eInterrupt, Process process){
            this.eInterrupt = eInterrupt;
            this.process = process;
        }
    }
    
    private Scheduler scheduler;
    // critical section
    private Queue<Interrupt> interruptQ;
    public Interrupt get() {
        return interruptQ.dequeue();
    }
    public void set(Interrupt Interrupt) {
        this.interruptQ.enqueue(Interrupt);
    }
    public Interrupt makeInterrupt(EInterrupt eInterrupt, Process process) {
    	return new Interrupt(eInterrupt, process);
    }
    
    ///////////////////////////////////////////////////////
    // constructor
    public InterruptHandler(Scheduler scheduler){
        this.interruptQ = new Queue<Interrupt>();
        this.scheduler = scheduler;
        
    }
    private void HandleProcessStart(Process process) {
    	this.scheduler.getReadyQueue().enqueue(process);
//        getReadyQueue().enqueue(process);
    }

    private void HandleProcessTerminate() {
        // 현재 실행되는 process가 끝나야하니까 새로운 Process가 실행되어야 한다.
//        runningProcess = getReadyQueue().dequeue();
    }
    private void HandleIOStart(Process process) {
    	System.out.println("------------------ IO Interrupt Start ------------------" + scheduler.getWaitQueue().indexOf(process));
    	this.scheduler.getWaitQueue().enqueue(process);
    	this.set(this.makeInterrupt(EInterrupt.eIOTerminated, process));
//		runningProcess = getReadyQueue().dequeue();
    }
    private void HandleIOTerminate() {
    	Process process = this.scheduler.getWaitQueue().dequeue();
    	System.out.println("------------------ IO Interrupt Finished ------------------"+ scheduler.getWaitQueue().indexOf(process));
    	this.scheduler.enReadyQueue(process);
    }
    private void HandleTimeOut() {
        // context switching
//        getReadyQueue().enqueue(runningProcess);
//        runningProcess = getReadyQueue().dequeue();
    }
	public void handle() {
		Interrupt interrupt = this.get();
		if (interrupt != null) {
			switch (interrupt.geteInterrupt()) {
			case eProcessStarted: // Loader
				HandleProcessStart(interrupt.getProcess());
				break;
			case eProcessTerminated:
//				HandleProcessTerminate(interrupt.getProcess());
				break;
			case eIOStarted:
				HandleIOStart(interrupt.getProcess());
				break;
			case eIOTerminated:
				HandleIOTerminate();
				break;
			case eTimeOut:
				HandleTimeOut(); // 독립적인 Thread 가 있어야 함.
				break;
			default:
				break;
			}
		}
	}
}