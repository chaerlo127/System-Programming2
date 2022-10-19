package main;

import java.util.concurrent.Semaphore;

public class Scheduler extends Thread{

	private static final int MAX_READY_COMMITS = 10;
	private boolean bPowerOn;
	
	private Semaphore fullSemaphoreReady;
	private Semaphore emptySemaphoreReady; 
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
	public Scheduler() {
		try {
			this.bPowerOn = true;
			this.readyQueue = new Queue<Process>();
			this.waitQueue = new Queue<Process>();
			
			this.interruptHandler = new InterruptHandler();
			
			this.fullSemaphoreReady = new Semaphore(MAX_READY_COMMITS, true);
			this.emptySemaphoreReady = new Semaphore(MAX_READY_COMMITS, true);
			this.emptySemaphoreReady.acquire(MAX_READY_COMMITS); // 10개를 잠가둠.
			
			this.runningProcess = null;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// execute, 실행만 한다.
	// idle process 상황 -> waitQueue 로 들어가야 함. 계속 돌아가면 cpu 점유율이 커진다.
	public void run() {
		while (bPowerOn) {
			this.runningProcess = this.deReadyQueue(); // dequeue가 없으면 block
			boolean result = this.runningProcess.executeInstruction();// execute
			this.interruptHandler.handle();
		}
	}
	
	//critical section -> ReadyQueue가 critical section은 아님.
	public synchronized void enReadyQueue(Process process) {
		// synchronized: 누가 쓰면 아무도 쓰면 안됨. 한 사람만 쓰겠다. dequeue 와 enqueue 도 겹치지 않음. 순차적으로 접근해라.
		// ex) lock도 가능, 세마포,,?
		try {
			this.fullSemaphoreReady.acquire(); // 꽉 찼는지 확인, 빈 것 있는지 확인, 꽉차면 Block
			this.readyQueue.enqueue(process);
			this.emptySemaphoreReady.release(); // 하나를 풀어준다.
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// critical section
	public synchronized Process deReadyQueue() {
		Process process = null;
		try {
			this.emptySemaphoreReady.acquire(); // 여기서 죽음. release가 되어 있지 않음
			process = this.readyQueue.dequeue();
			this.fullSemaphoreReady.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return process;
	}


}