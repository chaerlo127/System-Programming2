package constraint;

public enum EMODE{
	// read open 할 것인지 -> 파일을 초기화 하지 않고 기존의 것으로 할 것임.
	eRead,
	// write open 할 것인지 -> 파일을 초기하면서 open
	eWrite,
	eClosed
}
