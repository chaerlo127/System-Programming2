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
	
//	move @0, 0 -> hard storage address �ּ� �ϵ��ũ 0�������� ���� file �̸��� ���� �־�� �Ѵ�. 
//  move r1, -> memory address
//  move r2, 4 -> size
//	interrupt read-> ��� ����� ��? �ּҸ� ������ ����� �Ѵ�. r0�� ����־�� ��, cpu, register, io device�� ����� �صξ��� ������ �ٸ� cpu�� �ڵ带 �����ϴ� �� ����. 

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
		private String command; // ���ڼ� ���ڸ��� 
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
			this.codeList.add(line); // parsing �� �ؾ��Ѵ�.
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

	// ��ɾ �� �����´�. �⸻���� ��ɾ� ������� �ؾ��Ѵ�. 
	// Ÿ�� �ƿ��� �ɸ���, context switching�� interrupt�� �����ϰ� �Ǵ�. ������ �ɾ������. 
	public boolean executeInstruction() {
		String instruction = this.codeList.get(this.getPC());
		this.setPC(this.getPC()+1);
		if (instruction.compareTo("halt") == 0) {
			return false;
		}
		return true;
	}
}
