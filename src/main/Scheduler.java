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
	
	//os로 생각
	public Scheduler() {
		this.bPowerOn = true;
		this.readyQueue = new ProcessQueue();
		this.waitQueue = new ProcessQueue();
		this.interruptHandler = new InterruptHandler();
		
		this.runningProcess = null;
	}
	
	// enqueue <- ready queue
	// 메모리 주소
	public void enqueueReadyQueue(Process process) {
		this.readyQueue.enqueue(process);
	}
	public void dequeueReadyQueue() {
		this.readyQueue.dequeue();
	}
	// enqueue <- wait queue
	// 메모리 주소
	public void enqueueWaitQueue(Process process) {
		this.waitQueue.enqueue(process);
	}
	public void dequeueWaitQueue() {
		this.waitQueue.dequeue();
	}
	
	// execute, 실행만 한다.
	public void run() {
		while (bPowerOn) {
			this.interruptHandler.handle(); // interrupt
			if (this.runningProcess != null) {
				this.cpu.executeInstruction();// execute
			}
		}
	}
	
	// ready queue에서 새로운 것을 뽑아와라
	// process context switching
	// waiting queue 넘김
	private class InterruptHandler{
		// interrupt는 종류가 늘어날 수도 있고 줄어들 수 있어서, 객체를 만드는 것이 더 좋긴 함. 
		// interrupt는 timer, io controller, loader가 발생시킴.
		
		// Loader가 올렸을 때
		private void HandleProcessStart() {
			// enque를 해야하는 데 미리 해둠.
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
				readyQueue.enqueue(runningProcess); // 돌아가고 있는 process context를 저장
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
	
	// throws exception 필요
	private class ProcessQueue extends Vector<Process>{
		private static final long serialVersionUID = 1L;
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
