import java.util.Vector;

public class FileSystem extends Thread {
	public enum EMODE{
		// read open 할 것인지 -> 파일을 초기화 하지 않고 기존의 것으로 할 것임.
		eRead,
		// write open 할 것인지 -> 파일을 초기하면서 open
		eWrite,
		eClosed
	}
	private class FileControlBlock{
		private EMODE emode;
		private Process process;
		private int currentPosition; // 현재 어디까지 읽었는지 저장
		public EMODE getEmode() {
			return emode;
		}
		public void setEmode(EMODE emode) {
			this.emode = emode;
		}
		public Process getProcess() {
			return process;
		}
		public void setProcess(Process process) {
			this.process = process;
		}
		public int getCurrentPosition() {
			return currentPosition;
		}
		public void setCurrentPosition(int currentPosition) {
			this.currentPosition = currentPosition;
		}
	}
	private Vector<FileControlBlock> fileHeaders; // 현재 파일이 실행 중인지 알기 위해서, file control block fcb로 만들고 싶다면 만들어라
	private Vector<Vector<Integer>> directory; // file의 집합소 Vector<Integer> -> file 한개
	
	// 현재는 disk 로 만든거고, 실제로는 open을 할 때 생성되는 것이다. 
	// 원래는 open interrupt 가 있어야 함. 이 때 파일이 생성되기 때문.
	// 하고 있던 마지막 작업을 file system이 자동으로 수행할 수 있게 해줌.
	
	private Queue<Interrupt> interruptQueue;
	private Queue<Interrupt> fileIOInterruptQueue;
	
	public FileSystem(Queue<Interrupt> interruptQueue, Queue<Interrupt> fileIOInterruptQueue) {
		this.fileHeaders = new Vector<FileControlBlock>();
		this.directory = new Vector<Vector<Integer>>();
		
		this.interruptQueue = interruptQueue;
		this.fileIOInterruptQueue = fileIOInterruptQueue;
	}
	
	public void initialize() {
	}
	public void finish() {
	}


	public void run() {
		while (true) {
			Interrupt interrupt = this.fileIOInterruptQueue.dequeue();
			if (interrupt != null) {
				Process process = interrupt.getProcess(); // 어떤 파일을 open 하라는 지 알아야 한다.
				if (interrupt.geteInterrupt().equals(Interrupt.EInterrupt.eOpenStart)) {
					// 현재 어떤 파일을 open 하라는지?
					int fileId = process.pop();
					// 먼저 쓰고 있는 파일인지 아닌지 확인을 해야함.
					int iMode = process.pop(); // 파라미터 줄때
					EMODE mode = EMODE.values()[iMode];
					if (fileId < this.fileHeaders.size()) { // 파일 아이디가 file의 개수들 보다 작아야 함. 정상적인 파일이 뭔가가 있는 것임.
						if (mode == EMODE.eRead) {
							if (this.fileHeaders.get(fileId).getEmode() == EMODE.eClosed) {
								this.fileHeaders.get(fileId).setEmode(mode); // read 모드로 변경
								this.fileHeaders.get(fileId).setProcess(process);
								this.fileHeaders.get(fileId).setCurrentPosition(0);// open은 시작이기 때문에 0부터 읽기 시작
							} else {
								// 아니면 에러 발생, 같이 열까요? 라는 것, 동시에 여는 것,,하지마
							}
						} else if (mode == EMODE.eWrite) {
							this.fileHeaders.get(fileId).setEmode(mode); // write 모드로 변경
							this.fileHeaders.get(fileId).setProcess(process);
							this.fileHeaders.get(fileId).setCurrentPosition(0);// write는 다 날리고 처음부터 써라, 기존의 파일이 있으면 다
																				// 날려부랴
						} else {
							// closed 모드는 있을 수 없음 .
							// 익셉션 처리를 윈래 해줘야함
						}
					} else {
						if (mode == EMODE.eWrite) {
							FileControlBlock fileControlBlock = new FileControlBlock();
							fileControlBlock.setEmode(mode); // write 모드로 변경
							fileControlBlock.setProcess(process);
							fileControlBlock.setCurrentPosition(0);
							this.fileHeaders.add(fileControlBlock);
						} else {
							// read 모드인 경우 에러임.
						}
					}
				} else if (interrupt.geteInterrupt().equals(Interrupt.EInterrupt.eWriteStart)) {

				} else if (interrupt.geteInterrupt().equals(Interrupt.EInterrupt.eReadStart)) { // 파일이 쓰고 있는 중인지 확인이 필요
					// data가 없음. 초기화 할때 가상의 파일을 하나 만들어봐라. okay 
				}
			}
		}
	}

}
