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
//	// context : register�� ����
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
//		// ���α׷��� �����ؾ��� ��� �������� �̵������� �˰� �ȴ�. CPU�� �Ϲ������� pc�� �� ����� ����. �� ���� �����غ����� �˰� �Ǵ� ����.
//		int nextPC = this.process.run(this.context.getPC());
//		this.getContext().setPC(nextPC);
//	}
//}
