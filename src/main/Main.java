package main;

public class Main {

	public static void main(String[] args) {
		
		CPU cpu = new CPU();
		cpu.setSwitch(true);
		
		Loader loader = new Loader();
//		Process process = loader.load("**");
		Process process = new Process();
		cpu.loadProcess(process);
		cpu.run();
//		Schedular sheduler = new Schedular();
//		sheduler.execute(process);
	}
}
