package main;


public class Scheduler extends Thread{

	private boolean bPowerOn;

	private Queue<Process> readyQueue;
	private Queue<Process> waitQueue;
	private InterruptHandler interruptHandler;
	
	private Process runningProcess;

	// critical section
	//getters
	public Queue<Process> getReadyQueue() {
		return readyQueue;
	}
	public Queue<Process> getWaitQueue() {
		return waitQueue;
	}
	///////////////////////////////////////////////////////


	//os�� ����
	public Scheduler() {
		this.bPowerOn = true;
		this.readyQueue = new Queue<Process>();
		this.waitQueue = new Queue<Process>();
		this.interruptHandler = new InterruptHandler();
		
		this.runningProcess = null;
	}

	// execute, ���ุ �Ѵ�.
	// idle process ��Ȳ -> waitQueue �� ���� ��. ��� ���ư��� cpu �������� Ŀ����.
	public void run() {
		while (bPowerOn) {
			// running process�� null�� �ƴϸ� instruction �� �д´�.
			if (this.runningProcess != null) {
				boolean result = this.runningProcess.executeInstruction();// execute
				if(!result){
					// ��������
					this.runningProcess = this.deReadyQueue(); // ������ ���ͷ��Ͱ� �ؾ��� ����.
				}
			} else {
				this.runningProcess = this.deReadyQueue();
			}
			this.interruptHandler.handle();
		}
	}
	//critical section -> ReadyQueue�� critical section�� �ƴ�.
	public synchronized void enReadyQueue(Process process) {
		// synchronized: ���� ���� �ƹ��� ���� �ȵ�. �� ����� ���ڴ�. dequeue �� enqueue �� ��ġ�� ����. ���������� �����ض�.
		// ex) lock�� ����, ������,,?
		this.readyQueue.enqueue(process);
	}
	//critical section
	public synchronized Process deReadyQueue(){
		return this.readyQueue.dequeue();
	}


}
