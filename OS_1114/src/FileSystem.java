import java.util.Vector;

public class FileSystem extends Thread {
	public enum EMODE{
		// read open �� ������ -> ������ �ʱ�ȭ ���� �ʰ� ������ ������ �� ����.
		eRead,
		// write open �� ������ -> ������ �ʱ��ϸ鼭 open
		eWrite,
		eClosed
	}
	private class FileControlBlock{
		private EMODE emode;
		private Process process;
		private int currentPosition; // ���� ������ �о����� ����
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
	private Vector<FileControlBlock> fileHeaders; // ���� ������ ���� ������ �˱� ���ؼ�, file control block fcb�� ����� �ʹٸ� ������
	private Vector<Vector<Integer>> directory; // file�� ���ռ� Vector<Integer> -> file �Ѱ�
	
	// ����� disk �� ����Ű�, �����δ� open�� �� �� �����Ǵ� ���̴�. 
	// ������ open interrupt �� �־�� ��. �� �� ������ �����Ǳ� ����.
	// �ϰ� �ִ� ������ �۾��� file system�� �ڵ����� ������ �� �ְ� ����.
	
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
				Process process = interrupt.getProcess(); // � ������ open �϶�� �� �˾ƾ� �Ѵ�.
				if (interrupt.geteInterrupt().equals(Interrupt.EInterrupt.eOpenStart)) {
					// ���� � ������ open �϶����?
					int fileId = process.pop();
					// ���� ���� �ִ� �������� �ƴ��� Ȯ���� �ؾ���.
					int iMode = process.pop(); // �Ķ���� �ٶ�
					EMODE mode = EMODE.values()[iMode];
					if (fileId < this.fileHeaders.size()) { // ���� ���̵� file�� ������ ���� �۾ƾ� ��. �������� ������ ������ �ִ� ����.
						if (mode == EMODE.eRead) {
							if (this.fileHeaders.get(fileId).getEmode() == EMODE.eClosed) {
								this.fileHeaders.get(fileId).setEmode(mode); // read ���� ����
								this.fileHeaders.get(fileId).setProcess(process);
								this.fileHeaders.get(fileId).setCurrentPosition(0);// open�� �����̱� ������ 0���� �б� ����
							} else {
								// �ƴϸ� ���� �߻�, ���� �����? ��� ��, ���ÿ� ���� ��,,������
							}
						} else if (mode == EMODE.eWrite) {
							this.fileHeaders.get(fileId).setEmode(mode); // write ���� ����
							this.fileHeaders.get(fileId).setProcess(process);
							this.fileHeaders.get(fileId).setCurrentPosition(0);// write�� �� ������ ó������ ���, ������ ������ ������ ��
																				// �����η�
						} else {
							// closed ���� ���� �� ���� .
							// �ͼ��� ó���� ���� �������
						}
					} else {
						if (mode == EMODE.eWrite) {
							FileControlBlock fileControlBlock = new FileControlBlock();
							fileControlBlock.setEmode(mode); // write ���� ����
							fileControlBlock.setProcess(process);
							fileControlBlock.setCurrentPosition(0);
							this.fileHeaders.add(fileControlBlock);
						} else {
							// read ����� ��� ������.
						}
					}
				} else if (interrupt.geteInterrupt().equals(Interrupt.EInterrupt.eWriteStart)) {

				} else if (interrupt.geteInterrupt().equals(Interrupt.EInterrupt.eReadStart)) { // ������ ���� �ִ� ������ Ȯ���� �ʿ�
					// data�� ����. �ʱ�ȭ �Ҷ� ������ ������ �ϳ� ��������. okay 
				}
			}
		}
	}

}
