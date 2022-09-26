package main;

import java.util.Vector;

public class Process {
	private class ProcessControlBlock{
		// 고유 pid
		private int pid; 
		// account
		private int oid; 
		// statistics
		// io status information
		private int iodeviceid;
		
		public enum EStatus{
			eRunning, 
			eReady, 
			eWaiting, 
			eSuspended,
		}
		
		class cpuContext{
		// 모든 cpu 값들 저장
			private EStatus estatus;
			// segment register
			private int cs; // code segment register
			private int ds; // data segment register
			private int ss; // stack segment register
			private int hs; // heap segment register
			
			//cu
			private int PC; // program counter, 
		
			
			//ALU ac, computation에 관련된 것
			private int AC;
			
			// memory interface
			private int MAR;
			private int MBR;
		}
		private cpuContext cpuContext;
		
	}

	private Vector<Instruction> instructions;
	public Process() {
		this.instructions = new  Vector<Instruction>();
	}
	
	public int getLineLength() {
		return instructions.size();
	}
	void execute() {
		instructions.get(PC).execute("");;
		PC++;
	}
	private class Instruction{
		public void execute(String intruction) {
			System.out.println(intruction);
		}
	}

}
