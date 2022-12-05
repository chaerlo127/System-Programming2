import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class UI extends Thread {
	private Queue<Interrupt> interruptQueue;
	
	public UI(Queue<Interrupt> interruptQueue) {
		this.interruptQueue = interruptQueue;
	}
	public void initialize() {
	}
	public void finish() {
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
				Interrupt interrupt = new Interrupt(
						Interrupt.EInterrupt.eProcessStart, process);
				this.interruptQueue.enqueue(interrupt);
			}
			command = scanner.next();
		}
		scanner.close();
	}
	
	private class Loader {	
		public Process load(String exeName) {
			try {
				File file = new File("data" + "/" + exeName);
				Scanner scanner = new Scanner(file);
				Process process = new Process();
				process.parse(scanner);
				scanner.close();
				return process;
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
}
