package main;

import java.util.Vector;

public class Scheduler {
	private final int MAX_NUM_PROCESS = 10;
	private boolean bPowerOn;
	private ProcessQueue readyQueue;
	private ProcessQueue waitQueue;
	private InterruptHandler interruptHandler;
	private Process runningProcess;
	
	public enum EInterrupt{
		eTimeOut,
		eIOstarted,
		eIOTerminated,
		eProcessStarted,
		eProcessTerminated
	}
	
	//os�� ����
	public Scheduler() {
		this.bPowerOn = true;
		this.readyQueue = new ProcessQueue();
		this.waitQueue = new ProcessQueue();
		this.interruptHandler = new InterruptHandler();
		
		this.runningProcess = null;
	}
	
	// enqueue <- ready queue
	// �޸� �ּ�
	public void enqueueReadyQueue(Process process) {
		this.readyQueue.enqueue(process);
	}
	public void dequeueReadyQueue() {
		this.readyQueue.dequeue();
	}
	// enqueue <- wait queue
	// �޸� �ּ�
	public void enqueueWaitQueue(Process process) {
		this.waitQueue.enqueue(process);
	}
	public void dequeueWaitQueue() {
		this.waitQueue.dequeue();
	}
	
	// execute, ���ุ �Ѵ�.
	public void run() {
		while (bPowerOn) {
			this.interruptHandler.handle(); // interrupt
			if (this.runningProcess != null) {
				this.cpu.executeInstruction();// execute
			}
		}
	}
	
	// ready queue���� ���ο� ���� �̾ƿͶ�
	// process context switching
	// waiting queue �ѱ�
	private class InterruptHandler{
		// interrupt�� ������ �þ ���� �ְ� �پ�� �� �־, ��ü�� ����� ���� �� ���� ��. 
		// interrupt�� timer, io controller, loader�� �߻���Ŵ.
		
		// Loader�� �÷��� ��
		private void HandleProcessStart() {
			// enque�� �ؾ��ϴ� �� �̸� �ص�.
			if(!readyQueue.isEmpty()) {
				// dequeue
				runningProcess = readyQueue.dequeue(); 
				// switch context
				cpu.setContext(runningProcess);
			}
		}
		private void HandleProcessTerminate() {
			
		}
		private void HandleTimeOut() {
			if(runningProcess!=null) {
				// save current cpu Contxt to running Process
				runningProcess.setContext(cpu.getcontext());
				// enqueue to the end
				readyQueue.enqueue(runningProcess); // ���ư��� �ִ� process context�� ����
			}
			if(!readyQueue.isEmpty()) {
				// get next process
				runningProcess = readyQueue.dequeue(); 
				// switch context
				cpu.setContext(runningProcess);
			}
		}
		private void HandleIOStart() {
			
		}
		private void HandleIOTerminate() {
			
		}

		public void handle() {
			EInterrupt eInterrupt = this.cpu.checkInterrupt();
			switch(eInterrupt) {
			case eProcessStarted:
				HandleProcessStart();
				break;
			case eProcessTerminated:
				HandleProcessTerminate();
				break;
			case eIOstarted:
				HandleIOStart();
				break;
			case eIOTerminated:
				HandleIOTerminate();
				break;
			case eTimeOut:
				HandleTimeOut();
				break;
			default: break;
			}
		}

	}
	
	// throws exception �ʿ�
	private class ProcessQueue extends Vector<Process>{
		private static final long serialVersionUID = 1L;
		//circular queue
		private int head, tail, currentSize, maxSize;
		public ProcessQueue() {
			// �� ���� ������ �� �ִ� Process�� ����
			this.maxSize = MAX_NUM_PROCESS;
			this.currentSize = 0;
			this.head = 0;
			this.tail = 0;
			
			//vector�� element 10���� ��Ƶ�. 
			for(int i = 0; i<this.maxSize; i++) {
				this.add(null);
			}
		}
		
		// => �߰� exception handling
		public void enqueue(Process process) {
			if(this.currentSize < this.maxSize) {
				// process �ϳ� ����
				this.set(this.tail, process);
				// ������ �޸� �ּ� ���� ��
				this.tail = (this.tail + 1)%maxSize;
				this.currentSize ++;
			}
		}
		
		// => �߰� exception handling
		public Process dequeue() {
			Process process = null;
			if(this.currentSize>0) {
				process = this.get(head);
				this.head = (this.head + 1)%maxSize;
				this.currentSize --;
				return process;
			}
			return process;
		}
		
	}


}
