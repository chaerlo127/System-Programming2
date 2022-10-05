package main;

//ready queue에서 새로운 것을 뽑아와라
// process context switching
// waiting queue 넘김
public class InterruptHandler {
	public enum EInterrupt{
		eTimeOut,
		eIOstarted,
		eIOTerminated,
		eProcessStarted,
		eProcessTerminated
	}
	// interrupt는 종류가 늘어날 수도 있고 줄어들 수 있어서, 객체를 만드는 것이 더 좋긴 함.
	// interrupt는 timer, io controller, loader가 발생시킴.

	// Loader가 올렸을 때
	private void HandleProcessStart() {
	}

	private void HandleProcessTerminate() {

	}
	private void HandleTimeOut() {
	}

	private void HandleIOStart() {

	}
	private void HandleIOTerminate() {

	}

	public void handle() {
		EInterrupt eInterrupt = null;
		switch (eInterrupt) {
		case eProcessStarted:
			HandleProcessStart();
			break;
		case eProcessTerminated:
			HandleProcessTerminate();
			break;
		case eIOstarted:
			HandleIOStart();
			break;
		case eIOTerminated:
			HandleIOTerminate();
			break;
		case eTimeOut:
			HandleTimeOut();
			break;
		default:
			break;
		}
	}

}