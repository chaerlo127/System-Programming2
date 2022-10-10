package main;

import java.util.Vector;

// throws exception 필요
public class ProcessQueue extends Vector<Process> {
    private final int MAX_NUM_PROCESS = 10;
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