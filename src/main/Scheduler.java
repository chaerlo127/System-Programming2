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


	//os로 생각
	public Scheduler() {
		this.bPowerOn = true;
		this.readyQueue = new Queue<Process>();
		this.waitQueue = new Queue<Process>();
		this.interruptHandler = new InterruptHandler();
		
		this.runningProcess = null;
	}

	// execute, 실행만 한다.
	// idle process 상황 -> waitQueue 로 들어가야 함. 계속 돌아가면 cpu 점유율이 커진다.
	public void run() {
		while (bPowerOn) {
			// running process가 null이 아니면 instruction 을 읽는다.
			if (this.runningProcess != null) {
				boolean result = this.runningProcess.executeInstruction();// execute
				if(!result){
					// 끝났으면
					this.runningProcess = this.deReadyQueue(); // 원래는 인터럭터가 해야할 일임.
				}
			} else {
				this.runningProcess = this.deReadyQueue();
			}
			this.interruptHandler.handle();
		}
	}
	//critical section -> ReadyQueue가 critical section은 아님.
	public synchronized void enReadyQueue(Process process) {
		// synchronized: 누가 쓰면 아무도 쓰면 안됨. 한 사람만 쓰겠다. dequeue 와 enqueue 도 겹치지 않음. 순차적으로 접근해라.
		// ex) lock도 가능, 세마포,,?
		this.readyQueue.enqueue(process);
	}
	//critical section
	public synchronized Process deReadyQueue(){
		return this.readyQueue.dequeue();
	}


}