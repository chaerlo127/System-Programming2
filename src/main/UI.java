package main;

import java.util.Scanner;

public class UI {
	public void run() {
		Loader loader = new Loader();
		Scheduler scheduler = new Scheduler();
		// console command
		// "r fileName" : run -> execute fileName
		// "q" -> quit program
		
		Scanner scanner = new Scanner(System.in);
		String command = scanner.next();
		while(command.compareTo("q") != 0) {
			if(command.compareTo("r") == 0) {
				String fileName = scanner.next();
				Process process =loader.load(fileName); // process 생성
				scheduler.getReadyQueue().enqueue(process);
			}
		}
		scanner.close();
	}
}
