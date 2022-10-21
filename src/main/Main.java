package main;


public class Main {

	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler();
		UI ui = new UI(scheduler);
		ui.start(); 

		scheduler.start();
	}
}
