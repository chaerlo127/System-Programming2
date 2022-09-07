package main;

public class Scheduler {
	public enum EInterrupt{
		etimeOut,
		eIOstarted,
		eIOcompleted
	}
	private class InterruptHandler {
		public void processInterrupt(EInterrupt eInterrupt) {
			switch (eInterrupt) {
			case etimeOut: break;
			case eIOstarted: break;
			case eIOcompleted: break;
			}
		}
	}
	public void execute(Process process) {
		for(int i = 0; i<process.getLineLength(); i++) {
			process.execute(i);
			// 인터럭터가 있나 없나 확인함.
			// 인터럭터 벡터 : 인터럭터 처리를 확인하는 함수
//			checkInterrupt();
		}
	}
}
