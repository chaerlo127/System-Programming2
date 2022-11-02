import java.util.Scanner;

public class UI extends Thread {
	private Queue<Process> readyQueue;
	
	public UI(Scheduler scheduler) {}
	public UI(Queue<Process> readyQueue) {
		this.readyQueue = readyQueue;
	}
	
	public void run() {
		Loader loader = new Loader();
		
		// console command
		// "r fileName" -> execute fileName
		// "q" -> quit program
		
		Scanner scanner = new Scanner(System.in);
		String command = scanner.next();
		while (command.compareTo("q") != 0) {
			if (command.compareTo("r") == 0) {
				String fileName = scanner.next();
				Process process = loader.load(fileName);				
				readyQueue.enReadyQueue(process);
			}
			command = scanner.next();
		}
		scanner.close();
	}
}
