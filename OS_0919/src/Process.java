import java.util.Scanner;
import java.util.Vector;

public class Process {
//	.program
//	.data
//	codeSize 40
//	dataSize 4096
//	stackSize 4096
//	heapSize 4096
//  .end
//	.code
	
//	move @0, 0 -> hard storage address 주소 하드디스크 0번지에는 원래 file 이름을 갖고 있어야 한다. 
//  move r1, -> memory address
//  move r2, 4 -> size
//	interrupt read-> 어디에 얽어야 함? 주소를 전달을 해줘야 한다. r0에 집어넣어야 함, cpu, register, io device에 약속을 해두었기 때문에 다른 cpu의 코드를 번역하는 게 힘듦. 

//	move @4, r0
//	move @8, 0
//	label loop
//	compare @0, @4
//	jumpGraterThan loopEnd
//	add @8, 1
//	jump label
//	label loopEnd
//	move r0, @8
//	interrupt write
//	halt
//	.end
	private int PC;
	private int codeSize, dataSize, stackSize, heapSize;	
	private Vector<Instruction> codeList;
	private class Instruction{
		private String command; // 글자수 세자리로 
		private String operand1;
		private String operand2;
		
		
	}
	
	// getters
	public int getPC() {
		return PC;
	}
	public void setPC(int pC) {
		PC = pC;
	}
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
	public Vector<Instruction> getCodeList() {
		return codeList;
	}
	
	public Process() {
		this.codeList = new Vector<Instruction>();
	}
	
	
	private void parseData (Scanner scanner) {
		String command = scanner.next();
		while (command.compareTo(".end") != 0) {
			String operand = scanner.next();
			int size = Integer.parseInt(operand);
			if (command.compareTo("codeSize") == 0) {
				this.codeSize = size;
			} else if (command.compareTo("dataSize") == 0) {
				this.dataSize = size;
			} else if (command.compareTo("stackSize") == 0) {
				this.stackSize = size;
			} else if (command.compareTo("heapSize") == 0) {
				this.heapSize = size;
			}
			command = scanner.next();
		}		
	}
	private void parseCode(Scanner scanner) {
		String line = scanner.nextLine();
		while (line.compareTo(".end") != 0) {
			this.codeList.add(line); // parsing 을 해야한다.
			line = scanner.nextLine();
		}		
	}
	
	public void parse(Scanner scanner) {
		while (scanner.hasNext()) {
			String token = scanner.next();
			if (token.compareTo(".program") == 0) {				
			} else if (token.compareTo(".code") == 0) {
				this.parseCode(scanner);
			} else if (token.compareTo(".data") == 0) {
				this.parseData(scanner);				
			} else if (token.compareTo(".end") == 0) {
			}
		}
	}

	// 명령어를 뜯어서 가져온다. 기말에는 명령어 실행까지 해야한다. 
	// 타임 아웃이 걸리면, context switching을 interrupt를 실행하고 건다. 슬립을 걸어버린다. 
	public boolean executeInstruction() {
		String instruction = this.codeList.get(this.getPC());
		this.setPC(this.getPC()+1);
		if (instruction.compareTo("halt") == 0) {
			return false;
		}
		return true;
	}
}
