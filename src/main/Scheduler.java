package main;


public class Scheduler extends Thread{

	private boolean bPowerOn;

	private ProcessQueue readyQueue;
	private ProcessQueue waitQueue;
	private InterruptHandler interruptHandler;
	
	private Process runningProcess;

	// critical section
	//getters
	public ProcessQueue getReadyQueue() {
		return readyQueue;
	}
	public ProcessQueue getWaitQueue() {
		return waitQueue;
	}
	///////////////////////////////////////////////////////


	//os로 생각
	public Scheduler() {
		this.bPowerOn = true;
		this.readyQueue = new ProcessQueue();
		this.waitQueue = new ProcessQueue();
		this.interruptHandler = new InterruptHandler();
		
		this.runningProcess = null;
	}
	


	// execute, 실행만 한다.
	public void run() {
		while (bPowerOn) {
//			this.interruptHandler.handle(); // interrupt
			if (this.runningProcess != null) {
				this.runningProcess.executeInstruction();// execute
			}
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

	public enum EInterrupt {
		eTimeOut,
		eIOStarted,
		eIOTerminated,
		eProcessStarted,
		eProcessTerminated,
	}

	//ready queue에서 새로운 것을 뽑아와라
	// process context switching
	// waiting queue 넘김
	public class InterruptHandler {

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
		private Interrupt interrupt;
		public Interrupt geteInterrupt() {
			return interrupt;
		}
		public void seteInterrupt(EInterrupt Interrupt) {
			this.interrupt = interrupt;
		}
		///////////////////////////////////////////////////////

		// interrupt는 종류가 늘어날 수도 있고 줄어들 수 있어서, 객체를 만드는 것이 더 좋긴 함.
		// interrupt는 timer, io controller, loader가 발생시킴.

		// Loader가 올렸을 때
		private void HandleProcessStart(Process process) {
			getReadyQueue().enqueue(process);
		}

		private void HandleProcessTerminate() {
			// 현재 실행되는 process가 끝나야하니까 새로운 Process가 실행되어야 한다.
			runningProcess = getReadyQueue().dequeue();
		}
		private void HandleIOStart() {
			getWaitQueue().enqueue(runningProcess);
			runningProcess = getReadyQueue().dequeue();
		}
		private void HandleIOTerminate(Process process) {
			// waiting queue dequeue는?
//			getWaitQueue().dequeue() -> test 해보고 안될 시에는 dequeue 추가 하기
			getReadyQueue().enqueue(process);
		}
		private void HandleTimeOut() {
			// context switching
			getReadyQueue().enqueue(runningProcess);
			runningProcess = getReadyQueue().dequeue();
		}
		public void handle() {
			switch (this.interrupt.geteInterrupt()) {
				case eProcessStarted: // Loader
					HandleProcessStart(this.interrupt.getProcess());
					break;
				case eProcessTerminated:
					HandleProcessTerminate();
					break;
				case eIOStarted:
					HandleIOStart();
					break;
				case eIOTerminated:
					HandleIOTerminate(this.interrupt.getProcess());
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
