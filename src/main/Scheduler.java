package main;

import java.util.Vector;

public class Scheduler {
	private final int MAX_NUM_PROCESS = 10;
	private boolean bPowerOn;
	private ProcessQueue processQueue;
	private Process runningProcess;
	
	public enum EInterrupt{
		etimeOut,
		eIOstarted,
		eIOcompleted
	}
	
	public Scheduler() {
		this.bPowerOn = true;
		this.processQueue = new ProcessQueue();
		
		this.runningProcess = null;
	}
	
	// interrupt�� ó���ϴ� �Լ���� �����Ǿ� �ִ� ��
	private class InterruptHandler {
		public void processInterrupt(EInterrupt eInterrupt) {
			switch (eInterrupt) {
			case etimeOut: break;
			case eIOstarted: break;
			case eIOcompleted: break;
			default: break;
			}
		}
	}
	
	// execute
	public void run() {
		while(bPowerOn) {
			// ready queue���� ���ο� ���� �̾ƿͶ�
			// process context switching
			this.runningProcess.setContext(this.cpu.getContext());
			this.runningProcess = this.processQueue.dequeue(); // ���μ��� �ϳ� queue���� ����, running Process ����
			this.cpu.setContext(this.runningProcess.getContext());
			//interrupt
//			while(this.interrupt.getStatus()) {
//				this.cpu.executeInstruction(this.runningProcess);// execute
//			}
			
		}
	}
	
	// throws exception �ʿ�
	private class ProcessQueue extends Vector<Process>{
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
