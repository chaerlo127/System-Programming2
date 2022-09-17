package main;

public class CPU {
	public enum EInterrupt{
		eNone,
		eTimeout;
	}
	private boolean bSwitch;
	private Process process;
	private EInterrupt interrupt;
	public CPU() {
		this.interrupt = EInterrupt.eNone;
	}
	public void setSwitch(boolean bSwitch) {
		this.bSwitch = bSwitch;
	}
	
	// ������, ����� �ָ� �����ϴ� ���� cpu
	private void processInterrupt() {
		switch(interrupt) {
		case eTimeout:
			break;
		case eNone:
			break;
		default: 
			break;
		}
	}
	void loadProcess(Process process) {
		this.process = process;
	}
	public void run() {
		while(bSwitch == true) {
			// program counter
			process.execute();
			this.processInterrupt();
		}
	}
}
