package main;

import java.util.Vector;

public class Process {
	private Vector<Instruction> instructions;
	public Process() {
		this.instructions = new  Vector<Instruction>();
		this.instructions.add(new L0());
		this.instructions.add(new L1());
		this.instructions.add(new L2());
		this.instructions.add(new L3());
		this.instructions.add(new L4());
		this.instructions.add(new L5());
	}
	
	public int getLineLength() {
		return instructions.size();
	}
	void execute(int lineNo) {
		instructions.get(lineNo).execute();
	}
	private abstract class Instruction{
		abstract void execute();
	}
	private class L0 extends Instruction{
		void execute(){
			// 자바 바이트코드
			System.out.println(this.getClass().getSimpleName());
		}
	}
	private class L1 extends Instruction{
		void execute(){
			System.out.println(this.getClass().getSimpleName());
		}
	}
	
	private class L2 extends Instruction{
		void execute(){
			System.out.println(this.getClass().getSimpleName());
		}
	}
	
	private class L3 extends Instruction{
		void execute(){
			System.out.println(this.getClass().getSimpleName());
		}
	}
	
	private class L4 extends Instruction{
		void execute(){
			System.out.println(this.getClass().getSimpleName());
		}
	}
	
	private class L5 extends Instruction{
		void execute(){
			System.out.println(this.getClass().getSimpleName());
		}
	}
	
}
