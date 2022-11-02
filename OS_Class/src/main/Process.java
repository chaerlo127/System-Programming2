package main;

import java.util.Scanner;
import java.util.Vector;

import main.InterruptHandler.EInterrupt;
/* exe1 code
	int a = 0;
	int b = 5;(scanner.next())
	int c = 2;	
	while(a<c){
  		a+=1;
	}
	b = c;
	System.out.println(a, b, c);

exe2 code
	int a = 0; 
	int b = 6;
	int c = 5; (scanner.next())
	while(b<c){
   		b+=c;
	}
	a = c;
	System.out.println(a, b, c);
*/

public class Process {
	// attribute
	private String processName;
	private int PC;
	private int codeSize, dataSize, stackSize, heapSize;
	private Vector<String> codeList;
	private int proNum;
	
	private String[] dataSegment; // dataSegment
	private String r; // interrupt read variable
	private int compare; // 실제 변수들끼리 비교(어느 것이 큰지) compare -> GraterThan
	private int realCompare; // 숫자가 커야 하는지 작아야하는지, 확인해주는 변수
	private int loopPC; // while 문 시작 위치
	private int endLoopPC; // while 문 끝나는 위치의 pc
	
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
	public Process(String processName) {
		this.codeList = new Vector<String>();
		this.processName = processName;
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
				this.dataSegment = new String[this.dataSize/4];
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
		System.out.println("Process: " + this.getProNum() + "[" + this.processName + "]");
		this.currentThread =  Thread.currentThread();
		TimerInterrupt kilInterrupt = new TimerInterrupt();
		try {
			kilInterrupt.start();
			String instruction = "";
			
			while (instruction != null) {
				instruction = this.codeList.get(this.getPC());
				System.out.println("PC["+this.getPC()+"] "+instruction);
				translateInstruction(instruction); // instruction의 명령을 실제로 수행하는 method
				if(!checkInterrupt(instruction, interruptHandler)) return false; // interrupt라면?
				Thread.sleep(700);
			}
			
			return true;
		} catch (Exception e) {
			interruptHandler.set(interruptHandler.makeInterrupt(EInterrupt.eTimeOut, this)); // Timeout Interrupt 발생 시
		} finally {
			try {
				kilInterrupt.interrupt();
				return true;
			} catch (Exception e) {}
		}
		return false;
	}

	// interrupt 를 확인한다.
	private boolean checkInterrupt(String instruction, InterruptHandler interruptHandler) {
		this.setPC(this.getPC() + 1);
		
		if (instruction.indexOf("interrupt") >= 0) { // interrupt라면?
			interruptHandler.set(interruptHandler.makeInterrupt(EInterrupt.eIOStarted, this));
			System.out.println("------------------  [" + this.getProNum() + "] IO Interrupt Start ------------------");
			System.out.println("Interrupt Start");
			return false;
		}

		if (instruction.compareTo("halt") == 0) { // 프로세스가 끝났다면?
			interruptHandler.set(interruptHandler.makeInterrupt(EInterrupt.eProcessTerminated, this));
			System.out.println("------------------ [" + this.getProNum() + "] Process Terminate ------------------");
			return false;
		}
		return true;
	}

	// instruction을 번역하여 실제 코드에 넣고 일을 수행한다.
	private void translateInstruction(String instruction) {
		String[] str = instruction.split(" ");

		if (str[0].equals("move")) { // move
			this.replaceForArray(str, 1);
			if (str[2].indexOf("r") >= 0)
				this.dataSegment[Integer.parseInt(str[1]) / 4] = r; // move @4, r0
			else if (str[2].indexOf("@") >= 0) {
				this.replaceForArray(str, 2);
				this.dataSegment[Integer.parseInt(str[1]) / 4] = this.dataSegment[Integer.parseInt(str[2]) / 4];
			} else
				this.dataSegment[Integer.parseInt(str[1]) / 4] = str[2]; // move @8, 0
		} else if (str[0].equals("add")) { // add
			// add @8, 1
			this.replaceForArray(str, 1);
			this.dataSegment[Integer.parseInt(str[1]) / 4] = String
					.valueOf(Integer.parseInt(dataSegment[Integer.parseInt(str[1]) / 4]) + Integer.parseInt(str[2]));
		} else if (str[0].equals("interrupt")) { // interrupt
			if (str[1].equals("read")) {
				r = "5"; // 실제로는 read를 해야하지만 interrupt가 발생하기 때문에 이를 5로 그냥 저장하고 return을 해준다.
			} else if (str[1].equals("write")) {
				// System.out.println으로 해주기 -> 여기에 작성해야 하는 것을 halt 부분으로 옮겨줌. 
				// write interrupt 후에 써와야 하기 때문
			}
		} else if (str[0].equals("compare")) {
			this.replaceForArray(str, 1);
			this.replaceForArray(str, 2);

			int index1 = Integer.parseInt(str[1]) / 4; // index 1
			int index2 = Integer.parseInt(str[2]) / 4; // index 2
			
			this.compare = Integer.compare(Integer.parseInt(this.dataSegment[index1]),
					Integer.parseInt(this.dataSegment[index2]));
			
		} else if (str[0].equals(".label")) {
			if (str[1].equals("loop")) {
				this.loopPC = this.PC;
			}
		} else if (str[0].equals("jumpGraterThan")) {
			realCompare = 1;
			if (compare == realCompare) { // left > right 인 경우
				this.PC = this.endLoopPC; // 저장했던 PC로 변경해서 다시 loop을 실행한다.
			}
		} else if (str[0].equals("jump")) {
			this.endLoopPC = this.getPC() + 1;
			if (this.compare != realCompare) {
				this.PC = this.loopPC;
			}
		} else if (str[0].equals("halt")) {
			int a = 0;
			System.out.print("DataSegment Variable: ");
			while (dataSegment[a] != null) {
				System.out.print(dataSegment[a++] + ", ");
			}
			System.out.println();
		}
	}
	
	 // instruction 특수기호 제외
	private void replaceForArray(String[] str, int index) {
		str[index] = str[index].replace("@", "");
		str[index] = str[index].replace(",", "");
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
//				System.out.println("Time Out Interrupt");
				currentThread.interrupt();
				return;
			} catch (Exception e) {}
		}
	}
}
