package main;

import java.util.Vector;

// template class -> new 할 때, data type도 같이 선언을 해준다.
// throws exception 필요
public class Queue<T> extends Vector<T> {
    private final int MAX_NUM_ELEMENT = 10;
    private static final long serialVersionUID = 1L;

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

    // => 추가 exception handling
    public void enqueue(T element) {
        if(this.currentSize < this.maxSize) {
            // process 하나 세팅
            this.set(this.tail, element);
            // 실제로 메모리 주소 들어가는 것
            this.tail = (this.tail + 1)%maxSize;
            this.currentSize ++;
        }
    }

    // => 추가 exception handling
    public T dequeue() {
        T element = null;
        if(this.currentSize>0) {
            element = this.get(head);
            this.head = (this.head + 1)%maxSize;
            this.currentSize --;
            return element;
        }
        return element;
    }

}