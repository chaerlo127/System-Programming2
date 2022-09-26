package main;

public class Main {

	public static void main(String[] args) {
		
		// �ε��� �ϸ� sheduler�� �˰� �־�� ��.
		Loader loader = new Loader();
		Process process1 =loader.load("process1");
		Process process2 =loader.load("process2");
		Process process3 =loader.load("process3");
		
		// ready queue�� enqueue
		Scheduler scheduler = new Scheduler();
		scheduler.enqueueReadyQueue(process1);
		scheduler.enqueueReadyQueue(process2);
		scheduler.enqueueReadyQueue(process3);
		scheduler.run();
	}
}
