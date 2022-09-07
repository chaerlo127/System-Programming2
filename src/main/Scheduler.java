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
			// ���ͷ��Ͱ� �ֳ� ���� Ȯ����.
			// ���ͷ��� ���� : ���ͷ��� ó���� Ȯ���ϴ� �Լ�
//			checkInterrupt();
		}
	}
}
