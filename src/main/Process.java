package main;

import java.util.Scanner;
import java.util.Vector;

import main.InterruptHandler.EInterrupt;
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

	// attribute
	private int PC;
	private int codeSize, dataSize, stackSize, heapSize;
	private Vector<String> codeList;
	private int proNum;
	private String[] dataSegment; // dataSegment
	private String r; // interrupt read variable
	private int compare; // compare -> GraterThan
	private int realCompare; // 숫자가 커야 하는지 작아야하는지
	private int loopPC; // change PC for return 
	private int endLoopPC;
	
	
	
	//getters
	public int getPC() {return PC;}
	public int getCodeSize() {return codeSize;}
	public int getDataSize() {return dataSize;}
	public int getStackSize() {return stackSize;}
	public int getHeapSize() {return heapSize;}
	public Vector<String> getCodeList() {return codeList;}
	public int getProNum() {return this.proNum;}
	
	//setter
	public void setProNum(int proNum) {this.proNum = proNum;}
	public void setPC(int PC) {this.PC = PC;}

	// constructor
	public Process() {
		this.codeList = new Vector<String>();
		this.compare =-99;
		this.endLoopPC = -99;
	}
	
	private void parseData(Scanner scanner) {
		String command = scanner.next(); // 독립적으로 떨어진 것
		while (command.compareTo(".end") != 0) {
			String operand = scanner.next();// codeSize
			int size = Integer.parseInt(operand);
			if(command.compareTo("codeSize") == 0) this.codeSize = size;
			else if(command.compareTo("dataSize") == 0) { 
				this.dataSize = size;
				this.dataSegment = new String[this.dataSize];
			}
			else if(command.compareTo("stackSize") == 0) this.stackSize = size;
			else if(command.compareTo("heapSize") == 0) this.heapSize = size;
			command = scanner.next();
		}
	}
	
	private void parseCode(Scanner scanner) {
		String line = scanner.nextLine();
		while (line.compareTo(".end") != 0) {
			this.codeList.add(line);
			line = scanner.nextLine();
		}
	}

	public void parse(Scanner scanner){
		while (scanner.hasNext()) {
			String token = scanner.next();
			if(token.compareTo(".program") == 0);
			else if(token.compareTo(".code") == 0) this.parseCode(scanner);
			else if(token.compareTo(".data") == 0)	this.parseData(scanner);
			else if(token.compareTo(".end") == 0);

		}
	}
	
	Thread currentThread;
	public boolean executeInstruction(InterruptHandler interruptHandler) {
		System.out.println("Process: " + this.getProNum());
		this.currentThread =  Thread.currentThread();
		TimerInterrupt kilInterrupt = new TimerInterrupt();
		try {
			kilInterrupt.start();
			String instruction = "";
			while (instruction != null) {
				instruction = this.codeList.get(this.getPC());
				translateInstruction(instruction);
				System.out.println(instruction);
				if(!checkInterrupt(instruction, interruptHandler)) return false;
				Thread.sleep(700);
			}
			return true;
		} catch (Exception e) {
			interruptHandler.set(interruptHandler.makeInterrupt(EInterrupt.eTimeOut, this));

		} finally {
			try {
				kilInterrupt.interrupt();
				return true;
			} catch (Exception e) {
			}
		}
		return false;
	}

	private void translateInstruction(String instruction) {
		String[] str = instruction.split(" ");
		
		if(str[0].equals("move")) { // move
			this.replaceForArray(str, 1);
			if(str[2].indexOf("r")>=0) this.dataSegment[Integer.parseInt(str[1])/4] = r; // move @4, r0
			else this.dataSegment[Integer.parseInt(str[1])/4] = str[2]; // move @8, 0
		}else if(str[0].equals("add")) { // add
			//add @8, 1
			this.replaceForArray(str, 1);
			this.dataSegment[Integer.parseInt(str[1])/4]
					= String.valueOf(Integer.parseInt(dataSegment[Integer.parseInt(str[1])/4]) 
							+ Integer.parseInt(str[2]));
		}else if(str[0].equals("interrupt")) { // interrupt
			if(str[1].equals("read")) {
				r = "5"	; // 실제로는 read를 해야하지만 interrupt가 발생하기 때문에 이를 5로 그냥 저장하고 return을 해준다.
			}else if(str[1].equals("write")) {
				// System.out.println으로 해주기 -> 여기에 작성해야 하는 것을 halt 부분으로 옮겨줌. write interrupt 후에 써와야 하기 때문
			}
		}else if(str[0].equals("compare")) {
			this.replaceForArray(str, 1);
			this.replaceForArray(str, 2);
			
			int index1 = Integer.parseInt(str[1])/4; // index 1
			int index2 = Integer.parseInt(str[2])/4; // index 2
			this.compare = Integer.compare(Integer.parseInt(this.dataSegment[index1]), 
					Integer.parseInt(this.dataSegment[index2]));
		}else if(str[0].equals(".label")) {
			if(str[1].equals("loop")) {
				this.loopPC = this.PC;
			}
		}else if(str[0].equals("jumpGraterThan")) {
			realCompare = 1;
			if(compare==realCompare) { // left > right 인 경우
				this.PC = this.endLoopPC; // 저장했던 PC로 변경해서 다시 loop을 실행한다. 
			}
		}else if(str[0].equals("jump")) {
			this.endLoopPC = this.getPC() + 1;
			if(this.compare != realCompare) {
				this.PC = this.loopPC;
			}
		}else if(str[0].equals("halt")) {
			int a = 0; 
			System.out.print("DataSegment Variable: ");
			while(dataSegment[a] != null) {
				System.out.print(dataSegment[a++] + ", ");
			}
			System.out.println();
		}
	}
	
	private void replaceForArray(String[] str, int index) {
		str[index] = str[index].replace("@", "");
		str[index] = str[index].replace(",", "");
	}
	
	// interrupt 를 확인한다.
	private boolean checkInterrupt(String instruction, InterruptHandler interruptHandler) {
		this.setPC(this.getPC() + 1);
		if(instruction.indexOf("interrupt") >= 0) { // interrupt 라는 명령어가 있다면?
			interruptHandler.set(interruptHandler.makeInterrupt(EInterrupt.eIOStarted, this));
			System.out.println("------------------  [" +this.getProNum()+  "] IO Interrupt Start ------------------");
			System.out.println("Interrupt Start");
			return false;
		}
		
		if (instruction.compareTo("halt") == 0)	{
			interruptHandler.set(interruptHandler.makeInterrupt(EInterrupt.eProcessTerminated, this));
			return false;
		}
		return true;
	}

	public class TimerInterrupt extends Thread {
		@Override
		public void run() {
			try {
				Thread.sleep(3500);
			} catch (InterruptedException e) {
				return;
			} catch (Exception e) {
			}
			try {
				System.out.println("------------------  [" +getProNum()+  "] Time Out Interrupt ------------------");
				System.out.println("Time Out Interrupt");
				currentThread.interrupt();
				return;
			} catch (Exception e) {
			}
		}
	}
}
