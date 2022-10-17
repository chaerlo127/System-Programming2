package main;

import java.util.Scanner;
import java.util.Vector;
//		.program
//		.data
//		codeSize 40
//		dataSize 256
//		stackSize 4096
//		heapSize 4096
//		.code
//		move @0, 0
//		interrupt read
//		move @4, r0
//		move @8, 0
//		.label loop
//		compare @0, @4
//		jumpGraterThan loopEnd
//		add @8, 1
//		jump label
//		.label loopEnd
//		move r0, @8
//		interrupt write
//		halt
//		.end
public class Process {

	private int PC;

	private int codeSize, dataSize, stackSize, heapSize;
	private Vector<String> codeList;
	
	//getters
	public int getPC() {return PC;}

	public void setPC(int PC) {this.PC = PC;}
	public int getCodeSize() {
		return codeSize;
	}
	public int getDataSize() {
		return dataSize;
	}
	public int getStackSize() {
		return stackSize;
	}
	public int getHeapSize() {
		return heapSize;
	}
	public Vector<String> getCodeList() {
		return codeList;
	}

	public Process() {
		this.codeList = new Vector<String>();
	}
	
	private void parseData(Scanner scanner) {
		String command = scanner.next(); // 독립적으로 떨어진 것
		while (command.compareTo(".end") != 0) {
			String operand = scanner.next();// codeSize
			int size = Integer.parseInt(operand);
			if(command.compareTo("codeSize") == 0) {
				this.codeSize = size;
			}else if(command.compareTo("dataSize") == 0) {
				this.dataSize = size;
			}else if(command.compareTo("stackSize") == 0) {
				this.stackSize = size;
			}else if(command.compareTo("heapSize") == 0) {
				this.heapSize = size;
			}
			command = scanner.next();
		}
	}
	
	private void parseCode(Scanner scanner) {
		String line = scanner.nextLine(); // 독립적으로 떨어진 것
		while (line.compareTo(".end") != 0) {
			this.codeList.add(line);
			line = scanner.nextLine();
		}
	}

	public void parse(Scanner scanner){
		while (scanner.hasNext()) {
			String token = scanner.next();
			if(token.compareTo(".program") == 0){
			}else if(token.compareTo(".code") == 0){
				this.parseCode(scanner);
			}else if(token.compareTo(".data") == 0){
				this.parseData(scanner);
			}else if(token.compareTo(".end") == 0){
			}
		}
	}

	public boolean executeInstruction() {
		String instruction = this.codeList.get(this.getPC()); // pcb에서 가지고 있어야 할 내용
		this.setPC(this.getPC() + 1);
		if (instruction.compareTo("halt") == 0){
			// 프로그램 끝, 원래는 인터럭터를 걸어야 한다.
			return false;
		}
		return true;
	}
}