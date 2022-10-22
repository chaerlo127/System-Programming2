package main;

import java.util.concurrent.Semaphore;

import main.InterruptHandler.EInterrupt;

public class Scheduler extends Thread{
	/**
	 * 
	 * int a = 0;
	 * int b = scanner.nextInt(System.in);
	 * int c = 0;
	 * while(a>b){
	 *  c =+ 1;
	 * }
	 * b = c;
	 * System.out.println(c)

	 */
	private static final int MAX_READY_COMMITS = 4;
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
			
			this.interruptHandler = new InterruptHandler(this);
			
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
			if(this.runningProcess!= null) {
				boolean result = this.runningProcess.executeInstruction(interruptHandler);// execute
				this.interruptHandler.handle();
			}
		}
	}
	
	
	//critical section -> ReadyQueue가 critical section은 아님.
	//synchronized: 엄격하게 하나만 접근이 가능
	//세마포어: 조금 더 자유로운 시작/종료 가능
	public void enReadyQueue(Process process) {
		// critical section
		try {
			this.fullSemaphoreReady.acquire();
//			this.readyQueue.enqueue(process);
			// enqueue가 아니라, interrupt를 해서 생성한 후에 그 안에 enqueue를 한다
			if(process.getPC() == 0) interruptHandler.set(interruptHandler.makeInterrupt(EInterrupt.eProcessStarted, process));
			else this.readyQueue.enqueue(process);
			this.interruptHandler.handle();
			this.emptySemaphoreReady.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Process deReadyQueue() {
		Process process = null;
		try {
			this.emptySemaphoreReady.acquire();// 여기서 죽음. release가 되어 있지 않음
			process = this.readyQueue.dequeue();
			this.fullSemaphoreReady.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return process;
	}
}