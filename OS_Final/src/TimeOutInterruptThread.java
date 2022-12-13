
public class TimeOutInterruptThread extends Thread {
	private Thread currentThread;
	
	public TimeOutInterruptThread(Thread currentThread) {
		this.currentThread = currentThread;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000); // 1000 �̻��� �Ǹ�
			currentThread.interrupt(); // thread�� ���� �߻� ��, Time out Interrupt ����
			return;
		} catch (InterruptedException e) {
			return;
		} catch (Exception e) {
		}
	}
}