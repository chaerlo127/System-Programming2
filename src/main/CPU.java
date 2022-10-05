//package main;
//
//public class CPU {
//
//	public class Context{
//		private int PC;
//		public int getPC() {
//			return this.PC;
//		}
//		public void setPC(int PC) {
//			this.PC = PC;
//		}
//		
//		
//	}
//	
////	private boolean bSwitch;
//
//	// context : register의 집합
//	private Context context;
//	public Process getContext() {
//		return this.process;
//		}
//	public void setContext(Process process) {
//		this.process = process;
//		this.context = process.getContext();
//	}
//
//	public CPU() {
//		this.context = new Context();
//	}
//	private Process process;
//
//	public void executeInstruction() {
//		// program counter
//		// 프로그램이 실행해야지 어디 라인으로 이동할지를 알게 된다. CPU가 일방적으로 pc에 알 방법이 없음. 한 줄을 실행해봐야지 알게 되는 것임.
//		int nextPC = this.process.run(this.context.getPC());
//		this.getContext().setPC(nextPC);
//	}
//}
