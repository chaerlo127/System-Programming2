public class Interrupt {
	public enum EInterrupt {
		eTimeOut,
		eProcessStart,
		eProcessTerminated,
		eReadStart,
		eReadTerminated,
	}
	private EInterrupt eInterrupt;
	private Process process;
		
	public Interrupt(EInterrupt eInterrupt, Process process) {
		this.eInterrupt = eInterrupt;
		this.process = process;
	}
	public EInterrupt geteInterrupt() {return eInterrupt;}
	public Process getProcess() {return process;}		
}