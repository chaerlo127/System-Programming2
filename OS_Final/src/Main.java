import constraint.Config;

public class Main {
	private Queue<Interrupt> interruptQueue; // UI ~ Scheduler ~ File System ���� ���
	private Queue<Interrupt> fileIOInterruptQueue; // Scheduler���� File System���� interrupt ������ ���� ���
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
			UI.join(); // break point�� �ɾ�ΰ�, exit btn�� ���� ui �� ������ ��� Thread ���� �� main ����
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
