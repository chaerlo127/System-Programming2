package main;

import java.util.Vector;

public class Scheduler {
	private final int MAX_NUM_PROCESS = 10;
	private boolean bPowerOn;
	private ProcessQueue readyQueue;
	private ProcessQueue waitQueue;
	private InterruptHandler interruptHandler;
	
	private Process runningProcess;
	
	//getters
	public ProcessQueue getReadyQueue() {
		return readyQueue;
	}

	public ProcessQueue getWaitQueue() {
		return waitQueue;
	}
	
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
			this.interruptHandler.handle(); // interrupt
			if (this.runningProcess != null) {
//				this.cpu.executeInstruction();// execute
			}
		}
	}
	
	
	
	// throws exception �ʿ�
	public class ProcessQueue extends Vector<Process>{
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
