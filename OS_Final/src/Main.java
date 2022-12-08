public class Main {
	private Queue<Interrupt> interruptQueue; // UI ~ Scheduler ~ File System 에서 사용
	private Queue<Interrupt> fileIOInterruptQueue; // Scheduler에서 File System으로 interrupt 전달할 때만 사용
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
		// 세 개의 Thread가 모두 끝나면 main이 끝나도록 코드를 짜야 함. 
		// run이 끝나야지먄 main 이 끝나는 것을 점수로 반영할 예정임. 
	}
}
