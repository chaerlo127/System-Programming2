import java.util.Vector;
import java.util.concurrent.Semaphore;

public class Queue<T> extends Vector<T>{
		private static final long serialVersionUID = 1L;
		private final int MAX_NUM_ELEMENT = 10;
		
		private Semaphore fullSemaphoreReady;
		private Semaphore emptySemaphoreReady; 
		private int head, tail, currentSize, maxSize;

		public Queue() {
			try {
				this.maxSize = MAX_NUM_ELEMENT;
				this.currentSize = 0;
				this.head = 0;
				this.tail = 0;

				for (int i = 0; i < this.maxSize; i++) {
					this.add(null);
				}

				// 세마포는 queue에 들어가야 한다.
				this.fullSemaphoreReady = new Semaphore(MAX_NUM_ELEMENT, true);
				this.emptySemaphoreReady = new Semaphore(MAX_NUM_ELEMENT, true);
				this.emptySemaphoreReady.acquire(MAX_NUM_ELEMENT);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// ==> 추가 exception handling
		public void enqueue(T element) {
			if (this.currentSize < this.maxSize) { 
				this.set(this.tail, element);
				this.tail = (this.tail + 1) % this.maxSize;
				this.currentSize++;
			}
		}
		// ==> 추가 exception handling
		public T dequeue() {
			T element = null;
			if (this.currentSize > 0) {
				element = this.get(this.head);
				this.head = (this.head + 1) % this.maxSize;
				this.currentSize--;
			}
			return element;
		}
		
		// sub class로 해서 붙일 것이다. 
		public synchronized void enReadyQueue(Process process) {
			// critical section
			try {
				this.fullSemaphoreReady.acquire();
				this.readyQueue.enqueue(process);
				this.emptySemaphoreReady.release();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public synchronized Process deReadyQueue() {
			Process process = null;
			try {
				this.emptySemaphoreReady.acquire();
				process = this.readyQueue.dequeue();
				this.fullSemaphoreReady.release();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return process;
		}
	}
