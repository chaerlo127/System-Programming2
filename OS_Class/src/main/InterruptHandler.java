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

    // class
    public class Interrupt{
    	// attribute
        private EInterrupt eInterrupt;
        private Process process;
        
        // getters
        public EInterrupt geteInterrupt() {return eInterrupt;}
        public Process getProcess() {return process;}
        
        // constructors
        public Interrupt(EInterrupt eInterrupt, Process process){
            this.eInterrupt = eInterrupt;
            this.process = process;
        }
    }
    // attribute
    private Scheduler scheduler;
    	// critical section
    private Queue<Interrupt> interruptQ;
    
    // getters and setters 
    public Interrupt get() {return interruptQ.dequeue();}
    public void set(Interrupt Interrupt) {this.interruptQ.enqueue(Interrupt);}
    
    public Interrupt makeInterrupt(EInterrupt eInterrupt, Process process) {return new Interrupt(eInterrupt, process);}
    
    // constructor
    public InterruptHandler(Scheduler scheduler){
        this.interruptQ = new Queue<Interrupt>();
        this.scheduler = scheduler;
        
    }
    
    // process start
    private void HandleProcessStart(Process process) {
    	System.out.println("------------------ [" +process.getProNum()+  "] Process Start ------------------");
    }

    // process terminate
    private void HandleProcessTerminate(Process process) {
    	// 끝났을 때에는 끝난 위치에서 콘솔로 보여줌. 
    }
    
    // IO device Interrupt Start
    private void HandleIOStart(Process process) {
    	this.scheduler.getWaitQueue().enqueue(process);
    	System.out.println("------------------  [" +process.getProNum()+  "] IO Interrupt Finished ------------------");
    	this.set(this.makeInterrupt(EInterrupt.eIOTerminated, process));
    }
    
    // IO device Interrupt Terminate
    private void HandleIOTerminate() {
    	Process process = this.scheduler.getWaitQueue().dequeue();
    	this.scheduler.enReadyQueue(process);
    }
    
    // Timeout Interrupt 
    private void HandleTimeOut(Process process) {
    	this.scheduler.enReadyQueue(process);
    }
    
	public void handle() {
		Interrupt interrupt = this.get();
		if (interrupt != null) {
			switch (interrupt.geteInterrupt()) {
			case eProcessStarted: // Loader
				HandleProcessStart(interrupt.getProcess());
				break;
			case eProcessTerminated:
				HandleProcessTerminate(interrupt.getProcess());
				break;
			case eIOStarted:
				HandleIOStart(interrupt.getProcess());
				break;
			case eIOTerminated:
				HandleIOTerminate();
				break;
			case eTimeOut:
				HandleTimeOut(interrupt.getProcess()); // 독립적인 Thread 가 있어야 함.
				break;
			default:
				break;
			}
		}
	}
}