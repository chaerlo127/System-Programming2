public class Main {

	
	public Main() {}
	
	private void finish() {}
	private void initialize() {
	}
	
	public void run() {
		Queue<Interrupt> interruptQueue = new QueueSynchronized<Interrupt>(); // scheduler의 interrupt Queue
		Queue<Interrupt> fileIOCommandQueue = new QueueSynchronized<Interrupt>(); // fileSystem의 interrupt Queue

		Scheduler scheduler = new Scheduler(interruptQueue, fileIOCommandQueue);
		scheduler.start();

		UI ui = new UI(interruptQueue);
		ui.start();

		FileSystem fileSystem = new FileSystem(interruptQueue); // buffer를 아직 연결하지 않았음.
		fileSystem.start();
	}
	
	public static void main(String[] args) {	
		Main main = new Main();
		main.initialize();
		main.run();
		main.finish();
	}


}
