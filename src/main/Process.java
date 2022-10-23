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
	}
	
	private void parseData(Scanner scanner) {
		String command = scanner.next(); // 독립적으로 떨어진 것
		while (command.compareTo(".end") != 0) {
			String operand = scanner.next();// codeSize
			int size = Integer.parseInt(operand);
			if(command.compareTo("codeSize") == 0) this.codeSize = size;
			else if(command.compareTo("dataSize") == 0) this.dataSize = size;
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
				if(!checkInterrupt(instruction, interruptHandler)) return false;
				Thread.sleep(700);
				System.out.println(instruction);		
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

	// interrupt 를 확인한다.
	private boolean checkInterrupt(String instruction, InterruptHandler interruptHandler) {
		this.setPC(this.getPC() + 1);
		if(instruction.indexOf("interrupt") >= 0) { // interrupt 라는 명령어가 있다면?
			interruptHandler.set(interruptHandler.makeInterrupt(EInterrupt.eIOStarted, this));
			System.out.println("Interrupt Start");
			return false;
		}
		
		if (instruction.compareTo("halt") == 0)	{
			interruptHandler.set(interruptHandler.makeInterrupt(EInterrupt.eProcessTerminated, this));
			System.out.println("halt");
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
				System.out.println("Time Out Interrupt");
				currentThread.interrupt();
				return;
			} catch (Exception e) {
			}
		}
	}
}
