import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class Process {
	// CPU
	private static final int MAX_REGISTERS = 10;
	private int PC;
	private int codeSize, dataSize, stackSize, heapSize;
	private Vector<Integer> registers;
	
	// Memory
	private Vector<Instruction> codeList;
	private Vector<Integer> dataSegment;
	private Vector<Integer> stackSegment;
	private Vector<Integer> heapSegment;
	
	// Parser
	private Map<String, String> labelMap;
	 
	public Process() {
		this.registers = new Vector<Integer>();
		for (int i=0; i<MAX_REGISTERS; i++) {
			this.registers.add(i);
		}
		
		this.codeList = new Vector<Instruction>();
		this.dataSegment = new Vector<Integer>();
		this.stackSegment = new Vector<Integer>();
		this.heapSegment = new Vector<Integer>();
		
		this.labelMap = new HashMap<String, String>();
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
				for (int i=0; i<dataSize; i++) {
					this.dataSegment.add(i);
				}
			} else if (command.compareTo("stackSize") == 0) {
				this.stackSize = size;
				for (int i=0; i<stackSize; i++) {
					this.stackSegment.add(i);
				}
			} else if (command.compareTo("heapSize") == 0) {
				this.heapSize = size;
				for (int i=0; i<heapSize; i++) {
					this.heapSegment.add(i);
				}
			}
			command = scanner.next();
		}		
	}
	
	// symbol table과 비슷하게 생성한다.
	// 'token: 문법 기본 구성 요소' 를 뜯어낸다.
	// readInt는 interrupt [DMA, CPU와 독립적으로 돌아간다.]
	// stack에 저장을 해서 메모리에 저장을 해야한다. 
	private void parsePhaseI(Scanner scanner) {
		String line = scanner.nextLine();
		while (scanner.hasNext()) {
			Instruction instruction = new Instruction(line); // parsing
			// 정의한 것은 없애고 실제로 실행할 코드만 남아서 저장한다. 
			if (instruction.getCommand().compareTo("label") == 0) {
				// while line의 몇 번째 라인인지 저장해 둔 것임. 
				this.labelMap.put(
					instruction.getOperand1(), 
					Integer.toString(this.codeList.size())
				);
			} else if (instruction.getCommand().compareTo("") == 0) {				
			} else if (instruction.getCommand().compareTo("//") == 0) {
			} else {
				this.codeList.add(instruction);
			}
			line = scanner.nextLine();
		}
	}
	
	private void parsePhaseII() {
		for (Instruction instruction: this.codeList) {
			if ((instruction.getCommand().compareTo("jump")==0) ||
				(instruction.getCommand().compareTo("greaterThanEqual")==0)
			   ) {
				instruction.setOperand1(this.labelMap.get(instruction.getOperand1()));
			}
		}
	}
	
	
	private void parseCode(Scanner scanner) {
		this.parsePhaseI(scanner); // lexical analysis + parsing --> 어떤 타입인지 확인, label 생성[symbol table]
		this.parsePhaseII(); // code generation, assembly 언어
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

	public void executeInstruction(Queue<Interrupt> interruptQueue) {
		Instruction instruction = this.codeList.get(this.PC);
		System.out.println(this.PC + ": " + 
				instruction.getCommand() + " "
				+instruction.getOperand1()+ " " 
				+instruction.getOperand2());
		this.PC = PC +1;
		if (instruction.getOperand1().compareTo("halt") == 0) {
			Interrupt interrupt =
					new Interrupt(Interrupt.EInterrupt.eProcessTerminated, this);
			interruptQueue.enqueue(interrupt);
		}else if (instruction.getCommand().compareTo("movec") == 0) {
			int value = Integer.parseInt(instruction.getOperand2());
			this.registers.set(Integer.parseInt(instruction.getOperand1().substring(1)), value);// 번지를 받아서 register에 value를 저장한다.
		}else if (instruction.getCommand().compareTo("load") == 0) { // memory 주소 -> register
			int value = this.dataSegment.get(Integer.parseInt(instruction.getOperand2())); // 메모리에 저장된 value의 값을 불러오기 위해 메모리 주소를 불러온다. 
			this.registers.set(Integer.parseInt(instruction.getOperand1().substring(1)), value);// 번지를 받아서 register에 value를 저장한다.
			
		} else if (instruction.getCommand().compareTo("store") == 0) {
			// store를 해서 register를 segment에 저장해라. 
			// register 0번지에 저장된 값을 data segment에 저장한다.
			int value = this.registers.get(Integer.parseInt(instruction.getOperand2().substring(1)));
			this.dataSegment.set(Integer.parseInt(instruction.operand1), value);
		}else if (instruction.getCommand().compareTo("push") == 0) {
			// interrupt
		}else if (instruction.getCommand().compareTo("add") == 0) {
			int a = this.registers.get(Integer.parseInt(instruction.getOperand1().substring(1)));
			int b = this.registers.get(Integer.parseInt(instruction.getOperand2().substring(1)));
			this.registers.set(Integer.parseInt(instruction.getOperand1().substring(1)), a+b);
		}else if (instruction.getCommand().compareTo("addc") == 0) {
			int a = this.registers.get(Integer.parseInt(instruction.getOperand1().substring(1)));
			int b = this.registers.get(Integer.parseInt(instruction.getOperand2()));
			this.registers.set(Integer.parseInt(instruction.getOperand1().substring(1)), a+b);
		}else if (instruction.getCommand().compareTo("subtract") == 0) {
			int a = this.registers.get(Integer.parseInt(instruction.getOperand1().substring(1)));
			int b = this.registers.get(Integer.parseInt(instruction.getOperand2().substring(1)));
			this.registers.set(Integer.parseInt(instruction.getOperand1().substring(1)), a-b);
		}
		// 곱셈, 나눗셈필요
		// move도 필요할 것 같음. 
	}
	
	private class Instruction {
		
		private String command;
		private String operand1;
		private String operand2;
		
		public String getCommand() {
			return command;
		}
		public String getOperand1() {
			return operand1;
		}
		public void setOperand1(String operand1) {
			this.operand1 = operand1;			
		}
		public String getOperand2() {
			return operand2;
		}
		public Instruction(String line) {
			String[] tokens = line.split(" ");
			this.command = tokens[0];
			this.operand1 = "";
			this.operand2 = "";
			
			if (tokens.length > 1) {
				this.operand1 = tokens[1];
			}
			if (tokens.length > 2) {
				this.operand2 = tokens[2];
			}
		}
	}
}
