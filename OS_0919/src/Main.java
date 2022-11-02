import java.util.Vector;

public class Main {

	
	public Main() {}
	
	private void finish() {}
	private void initialize() {
	}
	
	public void run() {
		// queue를 공유 자원으로 만들기
		Queue<Process> readyQueue = new Queue<Process>();
		Queue<Process> waitQueue = new Queue<Process>();
		Queue<Interrupt> interruptQueue = new Queue<Interrupt>();
		Queue<FileIOCommand> fileIOCommandQueue = new Queue<FileIOCommand>(); // 명령어, offset, file ID
		Vector<Integer> filesystemBuffer = new Vector<Integer>();

		Scheduler scheduler = new Scheduler(readyQueue, waitQueue, interruptQueue, fileIOCommandQueue, filesystemBuffer);
		scheduler.start();

		UI ui = new UI(readyQueue);
		ui.start();

		FileSystem fileSystem = new FileSystem(interruptQueue, filesystemBuffer); // buffer를 아직 연결하지 않았음.
		fileSystem.start();
	}
	
	public static void main(String[] args) {	
		Main main = new Main();
		main.initialize();
		main.run();
		main.finish();
	}


}
