import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class Process {
	private static final int MAX_REGISTER = 10;
	// CPU 관련된 내용
	private int PC;
	private int codeSize, dataSize, stackSize, heapSize;
	private Vector<Integer> registers;
	
	// 메모리 관련된 내용
	private Vector<Instruction> codeList;
	private Vector<Integer> dataSegment;
	private Vector<Integer> stackSegment;
	private Vector<Integer> heapSegment;
	
	public Vector<Instruction> getCodeList() {
		return codeList;
	}
	
	// parse가 관련된 내용
	// key value
	// labelMap --> complier
	private Map<String, String> labelMap;
	public Process() {
		this.registers = new Vector<Integer>();
		for(int i = 0; i<MAX_REGISTER; i++) {
			this.registers.add(i);
		}
		this.codeList = new Vector<Instruction>();
		this.dataSegment = new Vector<Integer>();
		this.stackSegment = new Vector<Integer>();
		this.heapSegment = new Vector<Integer>();
		
		this.labelMap = new HashMap<String, String>();// 빈 맵이 만들어진다. 
	}
	
	public void initialize() {
		
	}
	public void finish() {
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
				for(int  i = 0 ; i<this.dataSize; i++){
					this.dataSegment.add(i);
				}
			} else if (command.compareTo("stackSize") == 0) {
				this.stackSize = size;
				for(int  i = 0 ; i<this.stackSize; i++){
					this.stackSegment.add(i);
				}
			} else if (command.compareTo("heapSize") == 0) {
				this.heapSize = size;
				for(int  i = 0 ; i<this.heapSize; i++){
					this.heapSegment.add(i);
				}
			}
			command = scanner.next();
		}		
	}
	private void parseCode(Scanner scanner) {
		this.paresePhase1(scanner);
		this.paresePhase2(scanner);
	}

	private void paresePhase1(Scanner scanner) {
		String line = scanner.nextLine();
		Instruction instruction = new Instruction(line);
		while (scanner.hasNext()) {
			this.codeList.add(instruction); // parsing 을 해야한다.
			if(instruction.getCommand().compareTo("label") == 0) {
				this.labelMap.put(instruction.getOperand1(), Integer.toString(codeList.size()));// label은 내가 들어온 문장이 다음 줄을 이야기 하는 것이다. 
			}else if(instruction.getCommand().compareTo("/") == 0) {
				this.codeList.add(instruction);
			}else if(instruction.getCommand().compareTo("") == 0) {
				this.codeList.add(instruction);
			}
			line = scanner.nextLine();
		}		
	}
	
	private void paresePhase2(Scanner scanner) {
		for(Instruction instruction : this.codeList) {
			if(instruction.getCommand().compareTo("jump") == 0 || instruction.getCommand().compareTo("graterThanEqual") == 0) {
				instruction.setOperand1(this.labelMap.get(instruction.getOperand1()));
			}
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

	public boolean executeInstruction(Queue<Interrupt> interruptHandler) {
		Instruction line = this.codeList.get(this.PC);
		System.out.println(line.getCommand()
				+ line.getOperand1()
				+ line.getOperand2());
		this.PC++;
		if (line.getOperand1().compareTo("halt") == 0) { // 변경 필요
			Interrupt interrupt = new Interrupt(Interrupt.EInterrupt.eProcessTerminated, this);
			interruptHandler.enqueue(interrupt);
			return false;
		}
		return true;
	}
	
	private class Instruction{
		private String command; // 글자수 세자리로 
		private String operand1;
		private String operand2;
		
		public String getCommand() {
			return command;
		}
		public void setOperand1(String operand1) {
			this.operand1 = operand1;
		}
		public String getOperand1() {
			return operand1;
		}
		public String getOperand2() {
			return operand2;
		}
		// Lexical Analysis: complier가 컴파일 할 때 단어 단위로 뜯는다. 마침표나 쉼표를 통해 서로 뜯는다. 이를 tree로 만든다. table에다가 token으로 나눠둔다.
		// 단어 단위 == token 
		// 세개의 token을 생성해두었다. 
		public Instruction(String line) {
			String[] tokens = line.split(" ");
			this.command = tokens[0];
			// null 이 아니라 empty string으로 넣어줘야 한다. 
			this.operand1 = "";
			this.operand2 = "";
			
			if(tokens.length > 1) { //2개면?
				this.operand1 = tokens[1];
			}
			if(tokens.length > 2) { //2개면?
				this.operand2 = tokens[2];
			}
		}
	}
	
}
