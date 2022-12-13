import constraint.Config;

public class Main {
	private Queue<Interrupt> interruptQueue; // UI ~ Scheduler ~ File System 에서 사용
	private Queue<Interrupt> fileIOInterruptQueue; // Scheduler에서 File System으로 interrupt 전달할 때만 사용
	private MainFrame mainFrame;
	private Scheduler scheduler;
	private UI UI;
	private FileSystem fileSystem;
	
	public Main() {	
		this.interruptQueue = new QueueSynchronized<Interrupt>();
		this.fileIOInterruptQueue = new QueueSynchronized<Interrupt>();
		this.UI = new UI(interruptQueue);		
		this.mainFrame = new MainFrame(UI);
		this.scheduler = new Scheduler(interruptQueue, fileIOInterruptQueue);
		this.fileSystem = new FileSystem(interruptQueue, fileIOInterruptQueue);	
	}
	
	private void initialize() {
		this.interruptQueue.initialize();
		this.fileIOInterruptQueue.initialize();
		this.scheduler.initialize();
		this.UI.initialize();
		this.fileSystem.initialize();
	}

	private void finish() {
		try {
			UI.join(); // break point를 걸어두고, exit btn에 의해 ui 가 꺼지면 모든 Thread 종료 후 main 종료
			this.UI.finish();
			this.scheduler.finish(); 
			this.fileSystem.finish();
			this.interruptQueue.finish();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(Config.mainStopSentence);
		System.exit(0);
	}

	private void run() {
		mainFrame.setVisible(true);
		scheduler.start();
		UI.start();
		fileSystem.start();
	}
	
	public static void main(String[] args) {
		Main main = new Main();
		main.initialize();
		main.run();
		main.finish(); 
	}
}
