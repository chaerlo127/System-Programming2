import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import constraint.EMODE;

public class Process {
	
	private class PCB{
		private static final int MAX_REGISTERS = 10;
		
		private int codeSize, dataSize, stackSize, heapSize;
		private int PC;

		private Vector<Instruction> codeList;
		private Vector<Integer> dataSegment;
		private Vector<Integer> stackSegment; // push ��ɾ ���� ���
		private Vector<Integer> heapSegment;
		
		private Vector<Integer> registers;

		public PCB() {
			this.codeList = new Vector<Instruction>();
			this.dataSegment = new Vector<Integer>();
			this.stackSegment = new Vector<Integer>();
			this.heapSegment = new Vector<Integer>();
			
			this.registers = new Vector<Integer>();
			for (int i=0; i<MAX_REGISTERS; i++) {
				this.registers.add(i);
			}
		}
		
		public void setCodeSize(int codeSize) {this.codeSize = codeSize;}
		public int getDataSize() {return dataSize;}
		public void setDataSize(int dataSize) {this.dataSize = dataSize;}
		public int getStackSize() {	return stackSize;}
		public void setStackSize(int stackSize) {this.stackSize = stackSize;}
		public int getHeapSize() {return heapSize;}
		public void setHeapSize(int heapSize) {this.heapSize = heapSize;}
		public int getPC() {return PC;}
		public void setPC(int pC) {PC = pC;}
		public Vector<Instruction> getCodeList() {return codeList;}
		public Vector<Integer> getDataSegment() {return dataSegment;}
		public Vector<Integer> getRegisters() {return registers;}
	}
	// CPU
	private boolean bGratherThan;
	private boolean bEqual;
	private EMODE emode;
	private int fileID;
	private int writeData;
	
	// Memory
	private PCB pcb;
	private int top; // �����Ͱ� ����ִ� ��ġ
	private String processName;
	private int proNum;
	
	// Parser
	private Map<String, String> labelMap;
	 
	public Process(String processName) {
		this.processName = processName;
		
		this.pcb = new PCB();
		
		this.bEqual = false;
		this.bGratherThan = false;

		this.top = 0;
		this.labelMap = new HashMap<String, String>();
	}
	public void initialize() {
	}
	public void finish() {
	}
	
//	public void push(int value) {
//		this.stackSegment.set(top, value);
//		top ++; 
//	}
//	public int pop() {
//		int value = this.stackSegment.get(top-1);
//		top = top - 1;
//		return value;
//	}
	public EMODE mode() {
		return this.emode;
	}
	public int getFileID() {
		return this.fileID;
	}
	public void setProNum(int proNum) {
		this.proNum = proNum;
	}
	public int getProNum() {
		return this.proNum;
	}
	public int getWriteData() {
		return this.writeData;
	}
	public void inputFileData(int readData) {
		this.pcb.getDataSegment().set(this.fileID, readData);
	}
	private void parseData (Scanner scanner) {
		String command = scanner.next();
		while (command.compareTo(".end") != 0) {
			String operand = scanner.next();
			int size = Integer.parseInt(operand);
			if (command.compareTo("codeSize") == 0) {
				this.pcb.setCodeSize(size);
			} else if (command.compareTo("dataSize") == 0) {
				this.pcb.setDataSize(size);
				for (int i=0; i<this.pcb.getDataSize(); i++) {
					this.pcb.getDataSegment().add(i);
				}
			} else if (command.compareTo("stackSize") == 0) {
				this.pcb.setStackSize(size);
				for (int i=0; i<this.pcb.getStackSize(); i++) {
					this.pcb.getDataSegment().add(i);
				}
			} else if (command.compareTo("heapSize") == 0) {
				this.pcb.setHeapSize(size);
				for (int i=0; i<this.pcb.getHeapSize(); i++) {
					this.pcb.getDataSegment().add(i);
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
					Integer.toString(this.pcb.getCodeList().size())
				);
			} else if (instruction.getCommand().compareTo("") == 0) {				
			} else if (instruction.getCommand().compareTo("//") == 0) {
			} else {
				this.pcb.getCodeList().add(instruction);
			}
			line = scanner.nextLine();
		}
	}
	
	private void parsePhaseII() {
		for (Instruction instruction: this.pcb.getCodeList()) {
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

	// enum class ����� �ʿ���
	public boolean executeInstruction(Queue<Interrupt> interruptQueue, Queue<Interrupt> fileIOInterruptQueue, TimerInterrupt killInterrupt) {
		try {
			Instruction instruction = this.pcb.getCodeList().get(this.pcb.getPC());
			System.out.println("Process: " + this.proNum + " [" + this.processName + "] \t" + 
			" PC -> " + this.pcb.getPC() + ": " + 
					instruction.getCommand() + " "
					+instruction.getOperand1()+ " " 
					+instruction.getOperand2());
			this.pcb.setPC(this.pcb.getPC()+1);
		
			// alu ���� ��ɾ� + ���ϱ� �����⵵ �����!!!
			if (instruction.getCommand().compareTo("load") == 0) { // memory �ּ� -> register
				int value = this.pcb.getDataSegment().get(Integer.parseInt(instruction.getOperand2())); // �޸𸮿� ����� value�� ���� �ҷ����� ���� �޸� �ּҸ� �ҷ��´�. 
				this.pcb.getRegisters().set(Integer.parseInt(instruction.getOperand1().substring(1)), value);// ������ �޾Ƽ� register�� value�� �����Ѵ�.
			} else if (instruction.getCommand().compareTo("store") == 0) {
				// store�� �ؼ� register�� segment�� �����ض�. 
				// register 0������ ����� ���� data segment�� �����Ѵ�.
				int value = this.pcb.getRegisters().get(Integer.parseInt(instruction.getOperand2().substring(1)));
				this.pcb.getDataSegment().set(Integer.parseInt(instruction.getOperand1()), value);
			}else if (instruction.getCommand().compareTo("movec") == 0) {
				int value = Integer.parseInt(instruction.getOperand2());
				this.pcb.getRegisters().set(Integer.parseInt(instruction.getOperand1().substring(1)), value);// ������ �޾Ƽ� register�� value�� �����Ѵ�.
			}else if (instruction.getCommand().compareTo("move") == 0) {
				int value = this.pcb.getRegisters().get(Integer.parseInt(instruction.getOperand2().substring(1)));
				this.pcb.getRegisters().set(Integer.parseInt(instruction.getOperand1().substring(1)), value);// ������ �޾Ƽ� register�� value�� �����Ѵ�.
			}else if (instruction.getCommand().compareTo("add") == 0) {
				int value1 = this.pcb.getRegisters().get(Integer.parseInt(instruction.getOperand1().substring(1)));
				int value2 = this.pcb.getRegisters().get(Integer.parseInt(instruction.getOperand2().substring(1)));
				this.pcb.getRegisters().set(Integer.parseInt(instruction.getOperand1().substring(1)), value1 + value2);
			}else if (instruction.getCommand().compareTo("subtract") == 0) {
				int value1 = this.pcb.getRegisters().get(Integer.parseInt(instruction.getOperand1().substring(1)));
				int value2 = this.pcb.getRegisters().get(Integer.parseInt(instruction.getOperand2().substring(1)));
				this.pcb.getRegisters().set(Integer.parseInt(instruction.getOperand1().substring(1)), value1 - value2);
				
				if(value1 == value2) this.bEqual = true;
				if(value1 > value2) this.bGratherThan = true;
			}else if (instruction.getCommand().compareTo("addc") == 0) {
				int value1 = this.pcb.getRegisters().get(Integer.parseInt(instruction.getOperand1().substring(1)));
				int value2 = Integer.parseInt(instruction.getOperand2());
				this.pcb.getRegisters().set(Integer.parseInt(instruction.getOperand1().substring(1)), value1 + value2);
			}
			
			// cu ���� ��ɾ� -> greaterThanEqual, jump �ʿ�
			else if (instruction.getCommand().compareTo("jump") == 0) {
				this.pcb.setPC(Integer.parseInt(instruction.getOperand1()));
			}else if (instruction.getCommand().compareTo("greaterThanEqual") == 0) {
				// interrupt
				if(this.bEqual || this.bGratherThan) {
					this.pcb.setPC(Integer.parseInt(instruction.getOperand1()));
				}
			}
			// interrupt ��ɾ�
			else if (instruction.getCommand().compareTo("interrupt") == 0) {
				Interrupt.EInterrupt eInterrupt = null;
				if(instruction.operand1.compareTo("readInt") ==0) {
					eInterrupt = Interrupt.EInterrupt.eOpenStart;
					this.emode = EMODE.eRead;
				} else if (instruction.operand1.compareTo("writeInt") == 0) {
					this.writeData = this.pcb.getDataSegment().get(this.fileID);
					eInterrupt = Interrupt.EInterrupt.eOpenStart;
					this.emode = EMODE.eWrite;
				} else if(instruction.operand1.compareTo("halt") ==0) {
					eInterrupt = Interrupt.EInterrupt.eProcessTerminated;
					Interrupt interrupt = new Interrupt(eInterrupt, this);
					interruptQueue.enqueue(interrupt);
					return false;
				}
				Interrupt interrupt = new Interrupt(eInterrupt, this);
				fileIOInterruptQueue.enqueue(interrupt);
				return false;
			}
			
			// io Interrupt ��ɾ�
			else if (instruction.getCommand().compareTo("push") == 0) {
				this.fileID = Integer.parseInt(instruction.getOperand1());
				// push 2��� 2�� push �Ѵ�. ������ int, process stack push �ϸ� file system�� copy�ؼ� �� ���̴�.  
			}else if (instruction.getCommand().compareTo("pop") == 0) {
			}
			
			Thread.sleep(700);
			return true;
			// ����, �������ʿ�
			// move�� �ʿ��� �� ����. 
		}catch (Exception e) {
			interruptQueue.enqueue(new Interrupt(Interrupt.EInterrupt.eTimeOut, this)); // Timeout Interrupt �߻� ��
		} 
		return false;
	
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
