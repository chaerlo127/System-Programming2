public class Main {
	
	public Main() {		
	}
	private void initialize() {
	}
	private void finish() {
	}

	private void run() {
		Queue<Interrupt> interruptQueue = new QueueSynchronized<Interrupt>();
		Queue<Interrupt> fileIOCommandQueue = new QueueSynchronized<Interrupt>();
		
		Scheduler scheduler = new Scheduler(
				interruptQueue, fileIOCommandQueue);
		scheduler.start();
		
		UI ui = new UI(interruptQueue);		
		ui.start();
		
		FileSystem fileSystem = new FileSystem(interruptQueue);		
		fileSystem.start();
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		main.initialize();
		main.run();
		main.finish();
	}


}
