
public class TimeOutInterruptThread extends Thread {
	private Thread currentThread;
	
	public TimeOutInterruptThread(Thread currentThread) {
		this.currentThread = currentThread;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(1000); // 1000 이상이 되면
			currentThread.interrupt(); // thread의 에러 발생 후, Time out Interrupt 전송
			return;
		} catch (InterruptedException e) {
			return;
		} catch (Exception e) {
		}
	}
}