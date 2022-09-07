package main;

public class Main {

	public static void main(String[] args) {
		Loader loader = new Loader();
		Process process = loader.load("**");
		Scheduler sheduler = new Scheduler();
		sheduler.execute(process);
	}
}
