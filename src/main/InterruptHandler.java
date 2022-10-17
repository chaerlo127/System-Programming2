package main;


//ready queue���� ���ο� ���� �̾ƿͶ�
// process context switching
// waiting queue �ѱ�
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
        // ���� ����Ǵ� process�� �������ϴϱ� ���ο� Process�� ����Ǿ�� �Ѵ�.
//        runningProcess = getReadyQueue().dequeue();
    }
    private void HandleIOStart() {
//        getWaitQueue().enqueue(runningProcess);
//        runningProcess = getReadyQueue().dequeue();
    }
    private void HandleIOTerminate(Process process) {
        // waiting queue dequeue��?
//			getWaitQueue().dequeue() -> test �غ��� �ȵ� �ÿ��� dequeue �߰� �ϱ�
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
                    HandleTimeOut(); // �������� Thread �� �־�� ��.
                    break;
                default:
                    break;
            }
        }
    }
}