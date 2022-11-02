package main;

import java.util.Vector;

// template class -> new 할 때, data type도 같이 선언을 해준다.
// throws exception 필요
public class Queue<T> extends Vector<T> {
    private static final long serialVersionUID = 1L;
    private final int MAX_NUM_ELEMENT = 10;

    //circular queue
    private int head, tail, currentSize, maxSize;
    public Queue() {
        // 한 번에 떠있을 수 있는 Process의 개수
        this.maxSize = MAX_NUM_ELEMENT;
        this.currentSize = 0;
        this.head = 0;
        this.tail = 0;

        //vector에 element 10개를 잡아둠.
        for(int i = 0; i<this.maxSize; i++) {
            this.add(null);
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
			this.set(this.head, null);
			this.head = (this.head + 1) % this.maxSize;
			this.currentSize--;
		}
		return element;
	}
}