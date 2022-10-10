package main;

import java.util.Vector;

public class Scheduler {

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
			this.interruptHandler.handle(); // interrupt
			if (this.runningProcess != null) {
				this.runningProcess.executeInstruction();// execute
			}
		}
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
					HandleTimeOut(); // 독립적인 Thread가 있어야 함.
					break;
				default:
					break;
			}
		}

	}
}
