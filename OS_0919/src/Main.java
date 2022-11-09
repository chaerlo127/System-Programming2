public class Main {

	
	public Main() {}
	
	private void finish() {}
	private void initialize() {
	}
	
	public void run() {
		Queue<Interrupt> interruptQueue = new QueueSynchronized<Interrupt>(); // scheduler�� interrupt Queue
		Queue<Interrupt> fileIOCommandQueue = new QueueSynchronized<Interrupt>(); // fileSystem�� interrupt Queue

		Scheduler scheduler = new Scheduler(interruptQueue, fileIOCommandQueue);
		scheduler.start();

		UI ui = new UI(interruptQueue);
		ui.start();

		FileSystem fileSystem = new FileSystem(interruptQueue); // buffer�� ���� �������� �ʾ���.
		fileSystem.start();
	}
	
	public static void main(String[] args) {	
		Main main = new Main();
		main.initialize();
		main.run();
		main.finish();
	}


}
