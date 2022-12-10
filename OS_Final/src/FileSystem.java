import java.util.Vector;

import constraint.EMODE;

public class FileSystem extends Thread {

	private class FileControlBlock{
		private EMODE emode;
		private Process process;
		private int currentPosition; // ���� ������ �о����� ����
		public FileControlBlock() {
			
		}
		public FileControlBlock(EMODE mode, Process process, int currentPosition) {
			this.emode = mode;
			this.process = process;
			this.currentPosition = currentPosition;
		}
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
		// demo file ����
		Vector<Integer> intVector = new Vector<Integer>();
		for(int i = 0; i<7; i++) {
			fileHeaders.add(new FileControlBlock(EMODE.eClosed, null, 0));
			intVector.add(1);
		}
		this.directory.add(intVector);
		this.directory.add(intVector);	
		this.directory.add(intVector);	
		this.directory.add(intVector);	
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
					int fileId = process.getFileID();
					// ���� ���� �ִ� �������� �ƴ��� Ȯ���� �ؾ���.
					EMODE mode = process.mode();
					if (fileId < this.fileHeaders.size()) { // ���� ���̵� file�� ������ ���� �۾ƾ� ��. �������� ������ ������ �ִ� ����.
						if (mode == EMODE.eRead) {
							if (this.fileHeaders.get(fileId).getEmode() == EMODE.eClosed) {
								this.fileHeaders.get(fileId).setEmode(mode); // read ���� ����
								this.fileHeaders.get(fileId).setProcess(process);
								this.fileHeaders.get(fileId).setCurrentPosition(0);// open�� �����̱� ������ 0���� �б� ����
								this.fileIOInterruptQueue.enqueue(new Interrupt(Interrupt.EInterrupt.eReadStart, process));
							} else {
								// �ƴϸ� ���� �߻�, ���� �����? ��� ��, ���ÿ� ���� ��,,������
							}
						} else if (mode == EMODE.eWrite) {
							this.fileHeaders.get(fileId).setEmode(mode); // write ���� ����
							this.fileHeaders.get(fileId).setProcess(process);
							this.fileHeaders.get(fileId).setCurrentPosition(0);// write�� �� ������ ó������ ���, ������ ������ ������ ��
																				// �����η�
							this.fileIOInterruptQueue.enqueue(new Interrupt(Interrupt.EInterrupt.eWriteStart, process));
						}
						// closed ���� ���� �� ����. 
					} else {
						if (mode == EMODE.eWrite) {
							FileControlBlock fileControlBlock = new FileControlBlock();
							fileControlBlock.setEmode(mode); // write ���� ����
							fileControlBlock.setProcess(process);
//							fileControlBlock.setCurrentPosition(0);
							this.fileHeaders.add(fileControlBlock);
							this.fileIOInterruptQueue.add(new Interrupt(Interrupt.EInterrupt.eWriteStart, process));
						}
						// read ����� ��� ������. 
					}
				} 
				else if (interrupt.geteInterrupt().equals(Interrupt.EInterrupt.eWriteStart)) {
					// write ������ �� ����
					this.directory.get(interrupt.getProcess().getFileID()).set(0, interrupt.getProcess().getWriteData());
					this.fileHeaders.get(interrupt.getProcess().getFileID()).setEmode(EMODE.eClosed);
					this.interruptQueue.enqueue(new Interrupt(Interrupt.EInterrupt.eWriteTerminated, process));
					//file id�� ���� ��������� ��.
				} else if (interrupt.geteInterrupt().equals(Interrupt.EInterrupt.eReadStart)) { 
					int addData = this.fileHeaders.get(interrupt.getProcess().getFileID()).getCurrentPosition();
					// ���� ���� ���� ������ Ȯ���ؼ� ����
					interrupt.getProcess().inputFileData(this.directory.get(interrupt.getProcess().getFileID()).get(addData));
					this.fileHeaders.get(interrupt.getProcess().getFileID()).setCurrentPosition(addData++);
					this.fileHeaders.get(interrupt.getProcess().getFileID()).setEmode(EMODE.eClosed);
					this.interruptQueue.enqueue(new Interrupt(Interrupt.EInterrupt.eReadTerminated, process));
				}
			}
		}
	}

}
