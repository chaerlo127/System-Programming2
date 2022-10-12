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


	//os�� ����
	public Scheduler() {
		this.bPowerOn = true;
		this.readyQueue = new ProcessQueue();
		this.waitQueue = new ProcessQueue();
		this.interruptHandler = new InterruptHandler();
		
		this.runningProcess = null;
	}
	


	// execute, ���ุ �Ѵ�.
	public void run() {
		while (bPowerOn) {
//			this.interruptHandler.handle(); // interrupt
			if (this.runningProcess != null) {
				this.runningProcess.executeInstruction();// execute
			}
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

	public enum EInterrupt {
		eTimeOut,
		eIOStarted,
		eIOTerminated,
		eProcessStarted,
		eProcessTerminated,
	}

	//ready queue���� ���ο� ���� �̾ƿͶ�
	// process context switching
	// waiting queue �ѱ�
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

		// interrupt�� ������ �þ ���� �ְ� �پ�� �� �־, ��ü�� ����� ���� �� ���� ��.
		// interrupt�� timer, io controller, loader�� �߻���Ŵ.

		// Loader�� �÷��� ��
		private void HandleProcessStart(Process process) {
			getReadyQueue().enqueue(process);
		}

		private void HandleProcessTerminate() {
			// ���� ����Ǵ� process�� �������ϴϱ� ���ο� Process�� ����Ǿ�� �Ѵ�.
			runningProcess = getReadyQueue().dequeue();
		}
		private void HandleIOStart() {
			getWaitQueue().enqueue(runningProcess);
			runningProcess = getReadyQueue().dequeue();
		}
		private void HandleIOTerminate(Process process) {
			// waiting queue dequeue��?
//			getWaitQueue().dequeue() -> test �غ��� �ȵ� �ÿ��� dequeue �߰� �ϱ�
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
					HandleTimeOut(); // �������� Thread �� �־�� ��.
					break;
				default:
					break;
			}
		}

	}
}
