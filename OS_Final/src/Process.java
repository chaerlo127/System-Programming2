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
	private boolean bGratherThan;
	private boolean bEqual;
	
	// Memory
	private Vector<Instruction> codeList;
	private Vector<Integer> dataSegment;
	private Vector<Integer> stackSegment; // push ���ɾ ���� ���
	private int top; // �����Ͱ� ����ִ� ��ġ
	private Vector<Integer> heapSegment;
	
	// Parser
	private Map<String, String> labelMap;
	 
	public Process() {
		this.bEqual = false;
		this.bGratherThan = false;
		
		this.registers = new Vector<Integer>();
		for (int i=0; i<MAX_REGISTERS; i++) {
			this.registers.add(i);
		}
		
		this.codeList = new Vector<Instruction>();
		this.dataSegment = new Vector<Integer>();
		this.stackSegment = new Vector<Integer>();
		this.top = 0;
		this.heapSegment = new Vector<Integer>();
		
		this.labelMap = new HashMap<String, String>();
	}
	public void initialize() {
	}
	public void finish() {
	}
	
	public void push(int value) {
		this.stackSegment.set(top, value);
		top ++; 
	}
	public int pop() {
		int value = this.stackSegment.get(top-1);
		top = top - 1;
		return value;
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
	
	// symbol table�� ����ϰ� �����Ѵ�.
	// 'token: ���� �⺻ ���� ���' �� ����.
	// readInt�� interrupt [DMA, CPU�� ���������� ���ư���.]
	// stack�� ������ �ؼ� �޸𸮿� ������ �ؾ��Ѵ�. 
	private void parsePhaseI(Scanner scanner) {
		String line = scanner.nextLine();
		while (scanner.hasNext()) {
			Instruction instruction = new Instruction(line); // parsing
			// ������ ���� ���ְ� ������ ������ �ڵ常 ���Ƽ� �����Ѵ�. 
			if (instruction.getCommand().compareTo("label") == 0) {
				// while line�� �� ��° �������� ������ �� ����. 
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
		this.parsePhaseI(scanner); // lexical analysis + parsing --> � Ÿ������ Ȯ��, label ����[symbol table]
		this.parsePhaseII(); // code generation, assembly ���
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

	
	// enum class ����� �ʿ��մϵ�!
	public void executeInstruction(Queue<Interrupt> interruptQueue) {
		Instruction instruction = this.codeList.get(this.PC);
		System.out.println(this.PC + ": " + 
				instruction.getCommand() + " "
				+instruction.getOperand1()+ " " 
				+instruction.getOperand2());
		this.PC = PC +1;
		
		// alu ���� ���ɾ� + ���ϱ� �����⵵ �����!!!
		if (instruction.getCommand().compareTo("load") == 0) { // memory �ּ� -> register
			int value = this.dataSegment.get(Integer.parseInt(instruction.getOperand2())); // �޸𸮿� ����� value�� ���� �ҷ����� ���� �޸� �ּҸ� �ҷ��´�. 
			this.registers.set(Integer.parseInt(instruction.getOperand1().substring(1)), value);// ������ �޾Ƽ� register�� value�� �����Ѵ�.
		} else if (instruction.getCommand().compareTo("store") == 0) {
			// store�� �ؼ� register�� segment�� �����ض�. 
			// register 0������ ����� ���� data segment�� �����Ѵ�.
			int value = this.registers.get(Integer.parseInt(instruction.getOperand2().substring(1)));
			this.dataSegment.set(Integer.parseInt(instruction.getOperand1()), value);
		}else if (instruction.getCommand().compareTo("movec") == 0) {
			int value = Integer.parseInt(instruction.getOperand2());
			this.registers.set(Integer.parseInt(instruction.getOperand1().substring(1)), value);// ������ �޾Ƽ� register�� value�� �����Ѵ�.
		}else if (instruction.getCommand().compareTo("move") == 0) {
			int value = this.registers.get(Integer.parseInt(instruction.getOperand2().substring(1)));
			this.registers.set(Integer.parseInt(instruction.getOperand1().substring(1)), value);// ������ �޾Ƽ� register�� value�� �����Ѵ�.
		}else if (instruction.getCommand().compareTo("add") == 0) {
			int value1 = this.registers.get(Integer.parseInt(instruction.getOperand1().substring(1)));
			int value2 = this.registers.get(Integer.parseInt(instruction.getOperand2().substring(1)));
			this.registers.set(Integer.parseInt(instruction.getOperand1().substring(1)), value1 + value2);
		}else if (instruction.getCommand().compareTo("subtract") == 0) {
			int value1 = this.registers.get(Integer.parseInt(instruction.getOperand1().substring(1)));
			int value2 = this.registers.get(Integer.parseInt(instruction.getOperand2().substring(1)));
			this.registers.set(Integer.parseInt(instruction.getOperand1().substring(1)), value1 - value2);
			
			if(value1 == value2) this.bEqual = true;
			if(value1 > value2) this.bGratherThan = true;
		}else if (instruction.getCommand().compareTo("addc") == 0) {
			int value1 = this.registers.get(Integer.parseInt(instruction.getOperand1().substring(1)));
			int value2 = Integer.parseInt(instruction.getOperand2());
			this.registers.set(Integer.parseInt(instruction.getOperand1().substring(1)), value1 + value2);
		}
		
		// cu ���� ���ɾ� -> greaterThanEqual, jump �ʿ�
		else if (instruction.getCommand().compareTo("jump") == 0) {
			this.PC = Integer.parseInt(instruction.getOperand1());// map �� �� ��° �������� Ȯ��
		}else if (instruction.getCommand().compareTo("greaterThanEqual") == 0) {
			// interrupt
			if(this.bEqual || this.bGratherThan) {
				this.PC = Integer.parseInt(instruction.getOperand1());
			}
		}
		// interrupt ���ɾ�
		else if (instruction.getCommand().compareTo("interrupt") == 0) {
			Interrupt.EInterrupt eInterrupt = null;
			if(instruction.operand1.compareTo("readInt") ==0) {
				eInterrupt = Interrupt.EInterrupt.eReadStart;
			} else if (instruction.operand1.compareTo("writeInt") == 0) {
				eInterrupt = Interrupt.EInterrupt.eWriteStart;
			} else if(instruction.operand1.compareTo("halt") ==0) {
				eInterrupt = Interrupt.EInterrupt.eProcessTerminated;
			}
			Interrupt interrupt = new Interrupt(eInterrupt, this);
			interruptQueue.enqueue(interrupt);
		}
		
		// io Interrupt ���ɾ�
		else if (instruction.getCommand().compareTo("push") == 0) {
			push(Integer.parseInt(instruction.getOperand1())); // push 2��� 2�� push �Ѵ�. ������ int, process stack push �ϸ� file system�� copy�ؼ� �� ���̴�.  
		}else if (instruction.getCommand().compareTo("pop") == 0) {
			
		}
		// ����, �������ʿ�
		// move�� �ʿ��� �� ����. 
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