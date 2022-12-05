public class Main {
	private Queue<Interrupt> interruptQueue; // UI ~ Scheduler ~ File System ���� ���
	private Queue<Interrupt> fileIOInterruptQueue; // Scheduler���� File System���� interrupt ������ ���� ���
	private Scheduler scheduler;
	private UI ui;
	private FileSystem fileSystem;
	
	public Main() {	
		this.interruptQueue = new QueueSynchronized<Interrupt>();
		this.fileIOInterruptQueue = new QueueSynchronized<Interrupt>();
		this.ui = new UI(interruptQueue);		
		this.scheduler = new Scheduler(interruptQueue, fileIOInterruptQueue);
		this.fileSystem = new FileSystem(interruptQueue, fileIOInterruptQueue);	
	}
	
	private void initialize() {
		this.interruptQueue.initialize();
		this.fileIOInterruptQueue.initialize();
		this.scheduler.initialize();
		this.ui.initialize();
		this.fileSystem.initialize();
	}

	private void finish() {
		this.scheduler.finish();
		this.ui.finish();
		this.fileSystem.finish();
		
		this.interruptQueue.finish();
	}

	private void run() {
		scheduler.start();
		ui.start();
		fileSystem.start();
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		main.initialize();
		main.run();
		main.finish(); 
		// �� ���� Thread�� ��� ������ main�� �������� �ڵ带 ¥�� ��. 
		// run�� ���������� main �� ������ ���� ������ �ݿ��� ������. 
	}
}
