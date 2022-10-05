package main;

//ready queue���� ���ο� ���� �̾ƿͶ�
// process context switching
// waiting queue �ѱ�
public class InterruptHandler {
	public enum EInterrupt{
		eTimeOut,
		eIOstarted,
		eIOTerminated,
		eProcessStarted,
		eProcessTerminated
	}
	// interrupt�� ������ �þ ���� �ְ� �پ�� �� �־, ��ü�� ����� ���� �� ���� ��.
	// interrupt�� timer, io controller, loader�� �߻���Ŵ.

	// Loader�� �÷��� ��
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