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
	
	// interrupt를 처리하는 함수들로 구성되어 있는 것
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
			// ready queue에서 새로운 것을 뽑아와라
			// process context switching
			this.runningProcess.setContext(this.cpu.getContext());
			this.runningProcess = this.processQueue.dequeue(); // 프로세스 하나 queue에서 빠짐, running Process 변경
			this.cpu.setContext(this.runningProcess.getContext());
			//interrupt
//			while(this.interrupt.getStatus()) {
//				this.cpu.executeInstruction(this.runningProcess);// execute
//			}
			
		}
	}
	
	// throws exception 필요
	private class ProcessQueue extends Vector<Process>{
		//circular queue
		private int head, tail, currentSize, maxSize;
		public ProcessQueue() {
			// 한 번에 떠있을 수 있는 Process의 개수
			this.maxSize = MAX_NUM_PROCESS;
			this.currentSize = 0;
			this.head = 0;
			this.tail = 0;
			
			//vector에 element 10개를 잡아둠. 
			for(int i = 0; i<this.maxSize; i++) {
				this.add(null);
			}
		}
		
		// => 추가 exception handling
		public void enqueue(Process process) {
			if(this.currentSize < this.maxSize) {
				// process 하나 세팅
				this.set(this.tail, process);
				// 실제로 메모리 주소 들어가는 것
				this.tail = (this.tail + 1)%maxSize;
				this.currentSize ++;
			}
		}
		
		// => 추가 exception handling
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
