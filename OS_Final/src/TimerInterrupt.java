
public class TimerInterrupt extends Thread {
	private Thread currentThread;
	
	public TimerInterrupt(Thread currentThread) {
		this.currentThread = currentThread;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(3500);
			currentThread.interrupt();
			return;
		} catch (InterruptedException e) {
			return;
		} catch (Exception e) {
		}
	}
}