package main;

public class Main {

	public static void main(String[] args) {
		
		// 로딩을 하면 sheduler를 알고 있어야 함.
		Loader loader = new Loader();
		Process process1 =loader.load("process1");
		Process process2 =loader.load("process2");
		Process process3 =loader.load("process3");
		
		// ready queue에 enqueue
		Scheduler scheduler = new Scheduler();
		scheduler.enqueueReadyQueue(process1);
		scheduler.enqueueReadyQueue(process2);
		scheduler.enqueueReadyQueue(process3);
		scheduler.run();
	}
}
