package main;

import java.util.Scanner;

public class UI extends Thread{
	private Scheduler scheduler;

	public UI(Scheduler scheduler){
		this.scheduler = scheduler; // ui에게 Loader를 장착시켜둠.
	}
	// run override 를 하면 자동으로 run을 해줌.
	public void run() {
		Loader loader = new Loader();
		
		// console command
		// "r fileName" : run -> execute fileName
		// "q" -> quit program
		Scanner scanner = new Scanner(System.in);
		String command = scanner.next();
		while(command.compareTo("q") != 0) {
			if(command.compareTo("r") == 0) {
				String fileName = scanner.next();
				Process process =loader.load(fileName); // process 생성
				scheduler.enReadyQueue(process); // blocking 되면 문제가 발생될 수 있음.
			}
			command = scanner.next();
		}
		scanner.close();
	}
}
