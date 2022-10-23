package main;

import java.util.concurrent.Semaphore;

import main.InterruptHandler.EInterrupt;

public class Scheduler extends Thread {
	// attribute
	private static final int MAX_READY_COMMITS = 4;

	private Semaphore fullSemaphoreReady;
	private Semaphore emptySemaphoreReady;
	
	private Queue<Process> readyQueue;
	private Queue<Process> waitQueue;
	
	private boolean bPowerOn;
	private InterruptHandler interruptHandler;
	private Process runningProcess;

	// getters
	public Queue<Process> getReadyQueue() {return readyQueue;}

	public Queue<Process> getWaitQueue() {return waitQueue;}

	// constructor
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
			e.printStackTrace();
		}
	}

	// execute
	// idle process 상황 -> waitQueue 로 들어가야 함. 계속 돌아가면 cpu 점유율이 커진다.
	public void run() {
		while (bPowerOn) {
			if(this.emptySemaphoreReady.availablePermits() == 0) {
				this.runningProcess = null;
				this.interruptHandler.handle();
			}else this.runningProcess = this.deReadyQueue(); // dequeue가 없으면 block
			
			if (this.runningProcess != null) {
				this.interruptHandler.handle();
				this.runningProcess.executeInstruction(interruptHandler);// execute
			}
		}
	}

	// critical section
	// synchronized: 엄격하게 하나만 접근이 가능
	// 세마포어: 조금 더 자유로운 시작/종료 가능
	public void enReadyQueue(Process process) {
		try {
			this.fullSemaphoreReady.acquire();
			if (process.getPC() == 0) 
				interruptHandler.set(interruptHandler.makeInterrupt(EInterrupt.eProcessStarted, process));
			this.readyQueue.enqueue(process);
			this.emptySemaphoreReady.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	// critical section
	// synchronized: 엄격하게 하나만 접근이 가능
	// 세마포어: 조금 더 자유로운 시작/종료 가능
	public Process deReadyQueue() {
		Process process = null;
		try {
			this.emptySemaphoreReady.acquire();
			process = this.readyQueue.dequeue();
			this.fullSemaphoreReady.release();
		} catch (InterruptedException e) {
			return process;
		}
		return process;
	}
}