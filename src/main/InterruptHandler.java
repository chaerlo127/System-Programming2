package main;


//ready queue에서 새로운 것을 뽑아와라
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
    // critical section
    private Queue<Interrupt> interruptQ;
    public Interrupt get() {
        return interruptQ.dequeue();
    }
    public void set(Interrupt Interrupt) {
        this.interruptQ.enqueue(Interrupt);
    }
    ///////////////////////////////////////////////////////

    public InterruptHandler(){
        this.interruptQ = new Queue<Interrupt>();
    }
    private void HandleProcessStart(Process process) {
//        getReadyQueue().enqueue(process);
    }

    private void HandleProcessTerminate() {
        // 현재 실행되는 process가 끝나야하니까 새로운 Process가 실행되어야 한다.
//        runningProcess = getReadyQueue().dequeue();
    }
    private void HandleIOStart() {
//        getWaitQueue().enqueue(runningProcess);
//        runningProcess = getReadyQueue().dequeue();
    }
    private void HandleIOTerminate(Process process) {
        // waiting queue dequeue는?
//			getWaitQueue().dequeue() -> test 해보고 안될 시에는 dequeue 추가 하기
//        getReadyQueue().enqueue(process);
    }
    private void HandleTimeOut() {
        // context switching
//        getReadyQueue().enqueue(runningProcess);
//        runningProcess = getReadyQueue().dequeue();
    }
    public void handle() {
        Interrupt interrupt = this.get();
        if(interrupt != null){
            switch (interrupt.geteInterrupt()) {
                case eProcessStarted: // Loader
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
                    HandleTimeOut(); // 독립적인 Thread 가 있어야 함.
                    break;
                default:
                    break;
            }
        }
    }
}